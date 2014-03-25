package pl.burningice.hibernate.events

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface HibernateEventCallback {

    Class domain()

    EventType[] eventTypes()

}