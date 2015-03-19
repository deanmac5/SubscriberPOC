import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParser
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
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import subscriberpoc.Agency
import subscriberpoc.Release
import subscriberpoc.Site

import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

long lStartTime = new Date().getTime();

def http = new HTTPBuilder('http://localhost:8080/SubscriberPOC/api/')

Gson gson = new Gson()
List<Site> sitesList = new ArrayList<>(0);
List<Release> releasesList = new ArrayList<>(0)
List<Release> existingReleases = new ArrayList<>(0)

http.request( Method.GET, ContentType.TEXT ) { req ->
    uri.path = 'site'
    headers.Accept = 'application/json'

    response.success = { resp, reader ->
        println "Got response: ${resp.statusLine}"
        def readerText = reader.text
        println readerText
        JsonArray sitesListJsonArray = new JsonParser().parse(readerText).getAsJsonArray();
        for (JsonElement siteListJson : sitesListJsonArray) {
            Site siteList = gson.fromJson(siteListJson, Site.class)
            sitesList.add(siteList)
        }
    }
}

http.request( Method.GET, ContentType.TEXT ) { req ->
    uri.path = 'agency'
    headers.Accept = 'application/json'

    response.success = { resp, reader ->
        println "Got response: ${resp.statusLine}"
        def readerText = reader.text
        println readerText

        JsonArray agencyJsonArray = new JsonParser().parse(readerText).getAsJsonArray();
        for (JsonElement agencyJson : agencyJsonArray) {
            Agency agency = gson.fromJson(agencyJson, Agency.class)
            println "Agency Title: " + agency.title
            JsonArray siteLists = agencyJson.get("sites").getAsJsonArray();

            for(JsonElement siteListJson : siteLists) {
                Site siteList = gson.fromJson(siteListJson, Site.class)
                println siteList.id

                siteList = sitesList.find{ ( it.id == siteList.id ) }
                println "Starting crawl of URL [" + siteList.url + "] with id [" + siteList.id + "]"
                println "HOST [" + siteList.url.toURI().getHost() + "]"

                String crawlStorageFolder = "/tmp/" + siteList.url.toURI().getHost() + "/";

                int numberOfCrawlers = 1;
                CrawlConfig config = new CrawlConfig();
                config.setCrawlStorageFolder(crawlStorageFolder);

                config.setPolitenessDelay(1000);
                config.setMaxDepthOfCrawling(1);
                //Change this to -1 when proper testing
                config.setMaxPagesToFetch(50);

                config.setIncludeBinaryContentInCrawling(false);
                config.setResumableCrawling(false);

                PageFetcher pageFetcher = new PageFetcher(config);
                RobotstxtConfig robotsTxtConfig = new RobotstxtConfig();
                RobotstxtServer robotsTxtServer = new RobotstxtServer(robotsTxtConfig, pageFetcher);
                CrawlController controller = new CrawlController(config, pageFetcher, robotsTxtServer);
                String[] customData = new String[6];
                customData[0] = siteList.url.toURI().getScheme() + "://" + siteList.url.toURI().getHost();
                customData[1] = siteList.created
                customData[2] = siteList.description
                customData[3] = agency.title
                customData[4] = siteList.createdRegex
                customData[5] = siteList.mediaReleaseSelector
                controller.setCustomData(customData)

                controller.addSeed(siteList.url);

                controller.start(CrawlerExtender.class, numberOfCrawlers);

                releases = controller.getCustomData();
                for(Release release: releases) {
                    release.site = siteList
                    releasesList.add(release)
                }

                println("Crawl of [" + siteList.url.toURI().getHost() + "] finished with [" + releases.size() + "] possible media releases found");
                println "Script running for [" + getDurationBreakdown(new Date().getTime() - lStartTime) + "]"
            }
        }
        println "Time to run [" + getDurationBreakdown(new Date().getTime() - lStartTime) + "]"
    }
}

println "Found [" + releasesList.size() + "] media releases"
http.request( Method.GET, ContentType.TEXT ) { req ->
    uri.path = 'release'
    headers.Accept = 'application/json'

    response.success = { resp, reader ->
        println "Got response: ${resp.statusLine}"
        def readerText = reader.text
        println readerText
        JsonArray releasesListJsonArray = new JsonParser().parse(readerText).getAsJsonArray();
        for (JsonElement releaseListJson : releasesListJsonArray) {
            Release releaseList = gson.fromJson(releaseListJson, Release.class)
            existingReleases.add(releaseList)
        }
    }
}
println "Found [" + existingReleases + "] existing media releases"

for(Release release: releasesList) {
    def jsonConvert = release as JSON;
    println "JSON convert [" + jsonConvert.toString(true) + "]"

    //Do findBy snippet + title + site, if found don't add.
    Closure closureRelease = { it.title == release.title && it.snippet == release.snippet && it.site.url == release.site.url }
    Release existingRelease = existingReleases.find { closureRelease }

    if( existingRelease == null ) {
        println "Adding in release [" + release.title + "]"
        http.request(Method.POST, ContentType.JSON) { req ->
            uri.path = 'release'
            def attr = ["title": release.title, "url": release.url, "snippet": release.snippet, "dateCreated": release.dateCreated, "releaseDate": release.releaseDate, "site": release.site]
            body = (attr as JSON).toString()
            response.success = { resp, reader ->
                println "Got response: ${resp.statusLine}"
            }
            response.failure = { resp ->
                println "Unexpected error: ${resp}"
            }
        }
    } else {
        println "Release [" + release.title + "] already exists"
    }

}


class CrawlerExtender extends WebCrawler {

    private static final Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz))\$")

    private String myCrawlDomains;
    private String createdMeta;
    private String descriptionMeta;
    private String agency;
    private String createRegex;
    private String mediaReleaseSelector

    @Override public void onStart() {
        String[] customData = (String[]) myController.getCustomData();
        myCrawlDomains = customData[0];
        createdMeta = customData[1];
        descriptionMeta = customData[2];
        agency = customData[3];
        createRegex = customData[4];
        mediaReleaseSelector = customData[5]
        myController.setCustomData(new ArrayList<Release>(0));
    }

    @Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        if (FILTERS.matcher(href).matches()) {
            return false;
        }

        println "Check that [" + href + "] starts with [" + myCrawlDomains + "]"
        if (href.startsWith(myCrawlDomains)) {
            return true;
        }
        return false;
    }

    @Override
    public void visit(Page page) {
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        int parentDocid = page.getWebURL().getParentDocid();
        println("Docid [" + docid + "]");
        println("URL: [" + url + "]");
        println("Docid of parent page: [" +  parentDocid + "]");

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            println("Title: [" + htmlParseData.getTitle() + "]")
            println("HtmlHeader length: [" + html.length() + "]");
            println("Number of outgoing links: [" + links.size() + "]");

            Document doc = Jsoup.parse(html);
            Elements descriptionElements = doc.select("meta[name=" + descriptionMeta + "]");
            Elements createdElements = doc.select("meta[name=" + createdMeta + "]")
            if(mediaReleaseSelector != null) {
                Elements mediaReleaseSelector = doc.select(mediaReleaseSelector)
                if(mediaReleaseSelector == null || mediaReleaseSelector.isEmpty()) {
                    println "Doesn't match mediarelease selector"
                    return
                }
            }
            if(descriptionElements != null && !descriptionElements.isEmpty() && createdElements != null && !createdElements.isEmpty()) {
                String description = descriptionElements.get(0).attr("content");
                String created = createdElements.get(0).attr("content");
                println("Created: [" + created + "]");
                println("Description: [" + description + "]");
                Date createdDate = Date.parse(createRegex, created)
                Release release = new Release(title: htmlParseData.getTitle(), snippet: description, url: page.getWebURL().getURL(), releaseDate: createdDate);

                ((List<Release>)myController.getCustomData()).add(release)
            }
        }
        println("=============");
    }
}

/**
 * Convert a millisecond duration to a string format
 *
 * @param millis A duration to convert to a string form
 * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
 */
public static String getDurationBreakdown(long millis)
{
    if(millis < 0)
    {
        throw new IllegalArgumentException("Duration must be greater than zero!");
    }

    long days = TimeUnit.MILLISECONDS.toDays(millis);
    millis -= TimeUnit.DAYS.toMillis(days);
    long hours = TimeUnit.MILLISECONDS.toHours(millis);
    millis -= TimeUnit.HOURS.toMillis(hours);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
    millis -= TimeUnit.MINUTES.toMillis(minutes);
    long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

    StringBuilder sb = new StringBuilder(64);
    sb.append(days);
    sb.append(" Days ");
    sb.append(hours);
    sb.append(" Hours ");
    sb.append(minutes);
    sb.append(" Minutes ");
    sb.append(seconds);
    sb.append(" Seconds");

    return(sb.toString());
}