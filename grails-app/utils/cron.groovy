import grails.converters.JSON
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import subscriberpoc.Agency

def http = new HTTPBuilder('http://localhost:8080/SubscriberPOC/api/')


http.request( Method.GET, ContentType.TEXT ) { req ->
    uri.path = 'agency'
    headers.Accept = 'application/json'

    response.success = { resp, reader ->
        println "Got response: ${resp.statusLine}"
        def readerText = reader.text
        println readerText
        Agency[] agencyList = JSON.parse(readerText)
        for(Agency agency: agencyList) {
            println "Agency [" + agency.title + "]"
            println "URLs [" + agency.urls + "]"
        }


    }
}