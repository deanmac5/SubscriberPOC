import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7' )

def http = new HTTPBuilder('http://localhost:8080/SubscriberPOC/api/')


http.request( Method.GET, ContentType.TEXT ) { req ->
    uri.path = 'agency'
    headers.Accept = 'application/json'

    response.success = { resp, reader ->
        println "Got response: ${resp.statusLine}"
        println "Content-Type: ${resp.headers.'Content-Type'}"
        print reader.text
    }
}