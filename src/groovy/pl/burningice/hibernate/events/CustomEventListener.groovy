package pl.burningice.hibernate.events

import groovy.util.logging.Slf4j
import org.hibernate.event.*

@Slf4j
class CustomEventListener implements
    PostUpdateEventListener,
    PostDeleteEventListener,
    PostInsertEventListener,
    PreUpdateEventListener,
    PreDeleteEventListener,
    PreInsertEventListener {

    HibernateEventsCallbackRepository hibernateEventsCallbackRepository

    @Override
    boolean onPreDelete(final PreDeleteEvent event) {
        triggerCallbacks(event.entity, EventType.BEFORE_DELETE)
        return false
    }

    @Override
    boolean onPreInsert(final PreInsertEvent event) {
        triggerCallbacks(event.entity, EventType.BEFORE_CREATE)
        return false
    }

    @Override
    boolean onPreUpdate(final PreUpdateEvent event) {
        triggerCallbacks(event.entity, EventType.BEFORE_UPDATE)
        return false
    }

    @Override
    void onPostDelete(final PostDeleteEvent event) {
        triggerCallbacks(event.entity, EventType.POST_DELETE, EventType.COMMITTED_DELETE, EventType.ROLLBACK_DELETE)
    }

    @Override
    void onPostInsert(final PostInsertEvent event) {
        triggerCallbacks(event.entity, EventType.POST_CREATE, EventType.COMMITTED_CREATE, EventType.ROLLBACK_CREATE)
    }

    @Override
    void onPostUpdate(final PostUpdateEvent event) {
        triggerCallbacks(event.entity, EventType.POST_UPDATE, EventType.COMMITTED_UPDATE, EventType.ROLLBACK_UPDATE)
    }

    private void triggerCallbacks(final Object entity, final EventType... eventTypes) {
        for (EventType eventType : eventTypes) {
            HibernateEvent<?> event = new HibernateEvent<>(entity, eventType)
            List<HibernateEventsCallbackRepository.CallbackExecutor> callbacksExecutors = hibernateEventsCallbackRepository.getCallbacksExecutorsFor(entity.class, eventType)
            for (HibernateEventsCallbackRepository.CallbackExecutor callbacksExecutor : callbacksExecutors) {
                callbacksExecutor.execute(event)
            }
        }
    }
}
