package pl.burningice.hibernate.events.test

import pl.burningice.hibernate.events.EventType
import pl.burningice.hibernate.events.HibernateEvent
import pl.burningice.hibernate.events.HibernateEventCallback
import pl.burningice.hibernate.events.HibernateEventCallbacksContainer

/**
 * Created by gdulus on 3/26/14.
 */
@HibernateEventCallbacksContainer
class TestCallbackContainer {

    public List<HibernateEvent> events = []

    @HibernateEventCallback(domain = TestDomain, eventTypes = EventType.POST_CREATE)
    public void postCreate(HibernateEvent<TestDomain> event) {
        events << event
    }

}
