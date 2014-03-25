grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
    // inherit Grails" default dependencies
    inherits("global") {
    }

    log "warn" // log level of Ivy resolver, either "error", "warn", "info", "debug" or "verbose"
    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }
    dependencies {
    }

    plugins {
        build(":release:3.0.1", ":rest-client-builder:1.0.3", ":tomcat:7.0.47") {
            export = false
        }

        build ":tomcat:7.0.47"
        runtime ":hibernate:3.6.10.6"
    }
}
