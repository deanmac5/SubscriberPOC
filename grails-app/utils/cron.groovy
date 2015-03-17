import edu.uci.ics.crawler4j.crawler.CrawlConfig
import edu.uci.ics.crawler4j.crawler.CrawlController
import edu.uci.ics.crawler4j.crawler.Page
import edu.uci.ics.crawler4j.crawler.WebCrawler
import edu.uci.ics.crawler4j.fetcher.PageFetcher
import edu.uci.ics.crawler4j.parser.HtmlParseData
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer
import edu.uci.ics.crawler4j.url.WebURL
import grails.converters.JSON
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import subscriberpoc.Agency

import java.util.regex.Pattern

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
                println "HOST [" + url.toURI().getHost() + "]"

                String crawlStorageFolder = "/tmp/" + url.toURI().getHost() + "/";

                int numberOfCrawlers = 1;
                CrawlConfig config = new CrawlConfig();
                config.setCrawlStorageFolder(crawlStorageFolder);

                config.setPolitenessDelay(1000);
                config.setMaxDepthOfCrawling(1);
                config.setMaxPagesToFetch(-1);

                config.setIncludeBinaryContentInCrawling(false);
                config.setResumableCrawling(false);

                PageFetcher pageFetcher = new PageFetcher(config);
                RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
                RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
                CrawlController controller = new CrawlController(/*url.toURI().getHost(), */config, pageFetcher, robotstxtServer);
                String[] crawlerDomain = new String[1];
                crawlerDomain[0] = url.toURI().getScheme() + "://" + url.toURI().getHost();
                controller.setCustomData(crawlerDomain)

                controller.addSeed(url);

                controller.startNonBlocking(CrawlerExtender.class, numberOfCrawlers);

                controller.waitUntilFinish();
                println("Crawl of [" + url.toURI().getHost() + "] finished");
            }
        }


    }
}

class CrawlerExtender extends WebCrawler {

    private static final Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz))\$")

    private String[] myCrawlDomains;

    @Override public void onStart() {
        myCrawlDomains = (String[]) myController.getCustomData();
    }

    @Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        if (FILTERS.matcher(href).matches()) {
            return false;
        }

        for (String crawlDomain : myCrawlDomains) {
            println "Check that [" + href + "] starts with [" + crawlDomain + "]"
            if (href.startsWith(crawlDomain)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void visit(Page page) {
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        int parentDocid = page.getWebURL().getParentDocid();
        println("Docid: {} " + docid);
        println("URL: {} " + url);
        println("Docid of parent page: {} " +  parentDocid);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            println("Text length: {} " + text.length());
            println("Html length: {} " + html.length());
            println("Number of outgoing links: {} " + links.size());
        }
        println("=============");
    }
}