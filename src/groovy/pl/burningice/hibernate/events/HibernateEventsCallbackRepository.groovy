package pl.burningice.hibernate.events

import groovy.transform.EqualsAndHashCode
import groovy.util.logging.Slf4j
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.aop.support.AopUtils
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationAdapter
import org.springframework.transaction.support.TransactionSynchronizationManager

import javax.annotation.PostConstruct
import java.lang.reflect.Method

@Slf4j
final class HibernateEventsCallbackRepository {

    private final Map<CallbackKey, List<CallbackExecutor>> callbackMapping = [:]

    GrailsApplication grailsApplication

    /**
     * Initialize object by scanning spring context for Hibernate Callbacks.
     * Executed by Spring.
     * @throws IllegalArgumentException When callback has incorrect specification (one parameter of HibernateEvent)
     */
    @PostConstruct
    public final void init() {
        Collection<Object> callbacks = grailsApplication.mainContext.getBeansWithAnnotation(HibernateEventCallbacksContainer)?.values() ?: []
        log.info('Found {} hibernate callbacks containers - registering methods', callbacks.size())

        for (Object callback : callbacks) {
            for (Method method : getTargetClass(callback).methods) {
                HibernateEventCallback annotation = method.getAnnotation(HibernateEventCallback)
                if (!annotation) {
                    continue
                }

                if (!isCallbackMethodValid(method)) {
                    throw new IllegalArgumentException("Method ${method.name} in container ${callback.class} has invalid specification. One parameter with HibernateEvent expected.")
                }

                Class domain = annotation.domain()
                EventType[] eventTypes = annotation.eventTypes()
                log.info('Found hibernate event listener {}#{} for domain {} and events {}', callback.class, method.name, domain.canonicalName, eventTypes)

                for (EventType eventType : eventTypes) {
                    CallbackKey key = new CallbackKey(domain, eventType)

                    if (!callbackMapping[key]) {
                        callbackMapping[key] = []
                    }

                    callbackMapping[key] << getCallback(callback, method, eventType)
                }
            }
        }

        log.info('Found {} hibernate callbacks containers - registering methods', callbacks.size())
    }

    private Class getTargetClass(final Object callback) {
        return (AopUtils.isAopProxy(callback) || AopUtils.isCglibProxy(callback) ? AopUtils.getTargetClass(callback) : callback.class)
    }

    private boolean isCallbackMethodValid(final Method method) {
        return (method.parameterTypes.size() == 1 && method.parameterTypes.first().equals(HibernateEvent))
    }

    private CallbackExecutor getCallback(final Object callback, final Method method, final EventType eventType) {
        CallbackExecutor callbackExecutor = new DefaultCallbackExecutor(callback, method)

        switch (eventType.scope) {
            case ExecutionScope.TRANSACTION_COMMITTED:
                callbackExecutor = new TSMCallbackExecutorWrapper(callbackExecutor, TransactionSynchronization.STATUS_COMMITTED)
                break;
            case ExecutionScope.TRANSACTION_ROLLBACK:
                callbackExecutor = new TSMCallbackExecutorWrapper(callbackExecutor, TransactionSynchronization.STATUS_ROLLED_BACK)
                break;
        }

        return callbackExecutor
    }

    /**
     * Allows to get list of callbacks registered for specific domain and event type
     * @param domain Domain object class for which we want to listen of hibernate events
     * @param eventType Type of a event for which we want to listen of hibernate events
     * @return List of callbacks registered for specific domain object and event type
     */
    public final List<CallbackExecutor> getCallbacksExecutorsFor(final Class<?> domain, final EventType eventType) {
        CallbackKey key = new CallbackKey(domain, eventType)
        callbackMapping[key] ?: Collections.emptyList()
    }

    /**
     * Allows to execute callback
     */
    public static interface CallbackExecutor {

        public void execute(HibernateEvent event)

    }

    /**
     * Default implementation of CallbackExecutor, which dispatch it with usage
     * groovy dynamic features
     */
    private static class DefaultCallbackExecutor implements CallbackExecutor {

        private final Object callback

        private final String methodName

        DefaultCallbackExecutor(final Object callback, final Method method) {
            this.callback = callback
            this.methodName = method.name
        }

        @Override
        public void execute(final HibernateEvent event) {
            callback."${methodName}"(event)
        }
    }

    /**
     * Wrapper around CallbackExecutor which provide transaction synchronization
     */
    private static class TSMCallbackExecutorWrapper implements CallbackExecutor {

        private final CallbackExecutor callbackExecutor

        private final int expectedStatus

        TSMCallbackExecutorWrapper(final CallbackExecutor callbackExecutor, final int expectedStatus) {
            this.callbackExecutor = callbackExecutor
            this.expectedStatus = expectedStatus
        }

        @Override
        void execute(final HibernateEvent event) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                void afterCompletion(int status) {
                    if (expectedStatus == status) {
                        callbackExecutor.execute(event)
                    }
                }
            })
        }
    }

    @EqualsAndHashCode
    private class CallbackKey {

        final String domainName

        final EventType eventType

        CallbackKey(Class<?> domainName, EventType eventType) {
            this.domainName = domainName.name
            this.eventType = eventType
        }
    }
}
