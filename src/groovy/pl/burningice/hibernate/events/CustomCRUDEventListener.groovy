package pl.burningice.hibernate.events

import groovy.util.logging.Slf4j
import org.hibernate.event.*

/**
 * Custom hook inside of the Hibernate Event System that allow us to executed custom callbacks (check {@link HibernateEventCallbacksContainer} and
 * {@link HibernateEventCallback})
 * @author Pawel Gdula
 */
@Slf4j
class CustomCRUDEventListener implements PostUpdateEventListener, PostDeleteEventListener, PostInsertEventListener {

    HibernateEventsCallbackRepository hibernateEventsCallbackRepository

    @Override
    void onPostDelete(final PostDeleteEvent postDeleteEvent) {
        triggerCallbacks(postDeleteEvent.entity, EventType.POST_DELETE, EventType.COMMITTED_DELETE)
    }

    @Override
    void onPostInsert(final PostInsertEvent postInsertEvent) {
        triggerCallbacks(postInsertEvent.entity, EventType.POST_CREATE, EventType.COMMITTED_CREATE)
    }

    @Override
    void onPostUpdate(final PostUpdateEvent postUpdateEvent) {
        triggerCallbacks(postUpdateEvent.entity, EventType.POST_UPDATE, EventType.COMMITTED_UPDATE)
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
