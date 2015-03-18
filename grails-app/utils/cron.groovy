import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
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
import subscriberpoc.MediaList

import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

def http = new HTTPBuilder('http://localhost:8080/SubscriberPOC/api/')

long lStartTime = new Date().getTime();

http.request( Method.GET, ContentType.TEXT ) { req ->
    uri.path = 'agency'
    headers.Accept = 'application/json'

    response.success = { resp, reader ->
        println "Got response: ${resp.statusLine}"
        def readerText = reader.text
        println readerText
        Gson gson = new Gson()
        JsonArray agencyJsonArray = new JsonParser().parse(readerText).getAsJsonArray();
        for (JsonElement agencyJson : agencyJsonArray) {
            Agency agency = gson.fromJson(agencyJson, Agency.class)
            println "Agency Title: " + agency.title
            JsonArray mediaLists = agencyJson.get("mediaLists").getAsJsonArray();

            for(JsonElement mediaListJson : mediaLists) {
                MediaList mediaList = gson.fromJson(mediaListJson, MediaList.class)
                println mediaList.id

                println "Starting crawl of URL [" + mediaList.url + "] with id [" + mediaList.id + "]"
                println "HOST [" + mediaList.url.toURI().getHost() + "]"

                String crawlStorageFolder = "/tmp/" + mediaList.url.toURI().getHost() + "/";

                int numberOfCrawlers = 1;
                CrawlConfig config = new CrawlConfig();
                config.setCrawlStorageFolder(crawlStorageFolder);

                config.setPolitenessDelay(1000);
                config.setMaxDepthOfCrawling(1);
                config.setMaxPagesToFetch(-1);

                config.setIncludeBinaryContentInCrawling(false);
                config.setResumableCrawling(false);

                PageFetcher pageFetcher = new PageFetcher(config);
                RobotstxtConfig robotsTxtConfig = new RobotstxtConfig();
                RobotstxtServer robotsTxtServer = new RobotstxtServer(robotsTxtConfig, pageFetcher);
                CrawlController controller = new CrawlController(config, pageFetcher, robotsTxtServer);
                String[] customData = new String[3];
                customData[0] = mediaList.url.toURI().getScheme() + "://" + mediaList.url.toURI().getHost();
                customData[1] = mediaList.created
                customData[2] = mediaList.description
                controller.setCustomData(customData)

                controller.addSeed(mediaList.url);

                controller.startNonBlocking(CrawlerExtender.class, numberOfCrawlers);

                controller.waitUntilFinish();
                println("Crawl of [" + mediaList.url.toURI().getHost() + "] finished");
                println "Script running for [" + getDurationBreakdown(new Date().getTime() - lStartTime) + "]"
            }
        }
        println "Time to run [" + getDurationBreakdown(new Date().getTime() - lStartTime) + "]"
    }
}

class CrawlerExtender extends WebCrawler {

    private static final Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz))\$")

    private String myCrawlDomains;
    private String createdMeta;
    private String descriptionMeta;

    @Override public void onStart() {
        String[] customData = (String[]) myController.getCustomData();
        myCrawlDomains = customData[0];
        createdMeta = customData[1];
        descriptionMeta = customData[2];
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
            if(descriptionElements != null && !descriptionElements.isEmpty() && createdElements != null && !createdElements.isEmpty()) {
                String description = descriptionElements.get(0).attr("content");
                String created = createdElements.get(0).attr("content");
                println("Created: [" + created + "]");
                println("Description: [" + description + "]");
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