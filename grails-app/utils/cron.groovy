import grails.converters.JSON
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.ccil.cowan.tagsoup.Parser
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
            String title = agency.title
            List<String> urls = agency.urls
            println "Agency [" + title + "]"
            for(String url: urls) {
                println "Starting crawl of URL [" + url + "]"

                def tagsoupParser = new Parser()
                def slurper = new XmlSlurper(tagsoupParser)
                def htmlParser = slurper.parse(url)
                ArrayList inputs = new ArrayList();

                htmlParser.'**'.findAll{
                    it.name() == 'input' || it.name() == 'select'
                }.each {
                    if (it.attributes().get(id) ) {
                        inputs.add(it)
                    }
                }
            }
        }


    }
}