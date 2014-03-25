package pl.burningice.hibernate.events

final class HibernateEvent<T> {

    final T entity

    final EventType eventType

    HibernateEvent(T entity, EventType eventType) {
        this.entity = entity
        this.eventType = eventType
    }
}
