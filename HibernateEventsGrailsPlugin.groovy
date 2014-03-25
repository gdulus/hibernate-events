import grails.util.Environment
import org.codehaus.groovy.grails.orm.hibernate.HibernateEventListeners
import pl.burningice.hibernate.events.CustomEventListener
import pl.burningice.hibernate.events.HibernateEventsCallbackRepository
import pl.burningice.hibernate.events.test.TestCallbackContainer

class HibernateEventsGrailsPlugin {

    def version = '0.1'

    def grailsVersion = '2.3 > *'

    def pluginExcludes = [
        'grails-app/views/error.gsp',
        'src/groovy/pl/burningice/hibernate/events/test/*'
    ]

    def title = 'Hibernate Events Plugin'

    def author = 'Pawel Gdula'

    def authorEmail = 'pawel.gdula@burningice.pl'

    def doWithWebDescriptor = { xml ->
    }

    def doWithSpring = {
        hibernateEventsCallbackRepository(HibernateEventsCallbackRepository) {
            grailsApplication = ref('grailsApplication')
        }

        customEventListener(CustomEventListener) {
            hibernateEventsCallbackRepository = ref('hibernateEventsCallbackRepository')
        }

        hibernateEventListeners(HibernateEventListeners) {
            listenerMap = [
                'post-insert': customEventListener,
                'post-update': customEventListener,
                'post-delete': customEventListener,
                'pre-insert': customEventListener,
                'pre-update': customEventListener,
                'pre-delete': customEventListener
            ]
        }

        if (Environment.current == Environment.TEST) {
            testCallbackContainer(TestCallbackContainer)
        }
    }

    def doWithDynamicMethods = { ctx ->
    }

    def doWithApplicationContext = { ctx ->
    }

    def onChange = { event ->
    }

    def onConfigChange = { event ->
    }

    def onShutdown = { event ->
    }
}
