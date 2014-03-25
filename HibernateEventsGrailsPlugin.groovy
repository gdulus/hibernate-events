class HibernateEventsGrailsPlugin {

    def version = '0.1'

    def grailsVersion = '2.3 > *'

    def pluginExcludes = [
            'grails-app/views/error.gsp'
    ]

    def title = 'Hibernate Events Plugin'

    def author = 'Pawel Gdula'

    def authorEmail = 'pawel.gdula@burningice.pl'

    def description = 'Brief summary/description of the plugin.'

    def documentation = 'http://grails.org/plugin/hibernate-events'

    def doWithWebDescriptor = { xml ->
    }

    def doWithSpring = {
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
