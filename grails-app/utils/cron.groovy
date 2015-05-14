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
import subscriberpoc.Subscriber

import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

String ANSI_RESET = "\u001B[0m";
String ANSI_RED = "\u001B[31m";
String ANSI_GREEN = "\u001B[32m";
String ANSI_YELLOW = "\u001B[33m";
String ANSI_BLUE = "\u001B[34m";
String ANSI_PURPLE = "\u001B[35m";

String user = "Pablo"
String pass = "password"

String host = "localhost"
String port = "25"
String from = "noreply@localhost";

Properties properties = System.getProperties();
properties.setProperty("mail.smtp.host", host);
properties.setProperty("mail.smtp.port", port)
properties.put("mail.debug", "false");

Session session = Session.getInstance(properties);

String base_url = "http://localhost:8080/SubscriberPOC/";
String url = base_url + "api/";
Date startDate = new Date();
long lStartTime = startDate.getTime();

println(ANSI_RED + "Running Media Release Crawler" + ANSI_RESET)
println(ANSI_PURPLE + "Start time: " + ANSI_YELLOW + startDate.toString() + ANSI_RESET)
println(ANSI_PURPLE + "URL: " + ANSI_YELLOW + url + ANSI_RESET)
println(ANSI_PURPLE + "Email Server: " + ANSI_YELLOW + host + ":" + port + " - " + from + ANSI_RESET)


def http = new HTTPBuilder(url)
def http_base = new HTTPBuilder(base_url)
Gson gson = new Gson()
List<Agency> agencyList = new ArrayList<>(0)
List<Agency> subscribers = new ArrayList<>(0)
List<Site> sitesList = new ArrayList<>(0);
List<Release> releasesList = new ArrayList<>(0)
List<Release> existingReleases = new ArrayList<>(0)
def cookies = []

//Authenticate
http_base.request(Method.POST) {
    uri.path = 'j_spring_security_check'
    requestContentType = ContentType.URLENC
    body = [j_username: user, j_password: pass]
    response.'302' = { resp, reader ->
        print(ANSI_PURPLE + "Response: " + ANSI_YELLOW + resp.statusLine.toString() + ANSI_RESET)
        println(ANSI_GREEN + "Authenticated" + ANSI_RESET)
        resp.getHeaders('Set-Cookie').each {
            String cookie = it.value.split(';')[0]
            cookies.add(cookie)
        }
    }
    response.failure = { resp ->
        println(ANSI_RED + "Unexpected error: ${resp}" + ANSI_RESET)
    }
}

println(ANSI_RED + "Performing GET on: " + ANSI_YELLOW + 'site...' + ANSI_RESET)
http.request( Method.GET, ContentType.TEXT ) { req ->
    uri.path = 'site'
    headers.Accept = 'application/json'
    headers.Cookie = cookies.join(';')

    response.success = { resp, reader ->
        print(ANSI_PURPLE + "Response: " + ANSI_YELLOW + resp.statusLine.toString() + ANSI_RESET)
        def readerText = reader.text
        JsonArray sitesListJsonArray = new JsonParser().parse(readerText).getAsJsonArray();

        for (JsonElement siteListJson : sitesListJsonArray) {
            Site siteList = gson.fromJson(siteListJson, Site.class)
            sitesList.add(siteList)
        }
        println(ANSI_PURPLE + "Number of sites: " + ANSI_YELLOW + sitesList.size() + ANSI_RESET)
    }
}


println(ANSI_RED + "Performing GET on: " + ANSI_YELLOW + 'agency...' + ANSI_RESET)
http.request( Method.GET, ContentType.TEXT ) { req ->
    uri.path = 'agency'
    headers.Accept = 'application/json'
    headers.Cookie = cookies.join(';')

    response.success = { resp, reader ->
        print(ANSI_PURPLE + "Response: " + ANSI_YELLOW + resp.statusLine.toString() + ANSI_RESET)
        def readerText = reader.text
        JsonArray agencyJsonArray = new JsonParser().parse(readerText).getAsJsonArray();
        for (JsonElement agencyJson : agencyJsonArray) {
            Agency agency = gson.fromJson(agencyJson, Agency.class)
            agencyList.add(agency)
            println(ANSI_PURPLE + "Agency Title: " + ANSI_YELLOW + agency.title + ANSI_RESET)
            JsonArray siteLists = agencyJson.get("sites").getAsJsonArray();

            for(JsonElement siteListJson : siteLists) {
                Site siteList = gson.fromJson(siteListJson, Site.class)
                siteList = sitesList.find{ ( it.id == siteList.id ) }
                println(ANSI_PURPLE + "Starting Crawl on: " + ANSI_YELLOW + siteList.url + ANSI_RESET)

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

                println(ANSI_PURPLE + "Finished Crawl on: " + ANSI_YELLOW + siteList.url + ANSI_RESET)
                println(ANSI_PURPLE + "Number of Media Releases Found: " + ANSI_YELLOW + releases.size() + ANSI_RESET)
                println(ANSI_RED + "Script running for [" + getDurationBreakdown(new Date().getTime() - lStartTime) + "]" + ANSI_RESET)
                println()
                println()
            }
        }
        println(ANSI_RED + "Complete Crawl finished in [" + getDurationBreakdown(new Date().getTime() - lStartTime) + "]")
    }
}

println(ANSI_PURPLE + "Total number of Media Releases Found: " + ANSI_YELLOW + releasesList.size() + ANSI_RESET)

println(ANSI_RED + "Performing GET on: " + ANSI_YELLOW + 'release...' + ANSI_RESET)
http.request( Method.GET, ContentType.TEXT ) { req ->
    uri.path = 'release'
    headers.Accept = 'application/json'
    headers.Cookie = cookies.join(';')

    response.success = { resp, reader ->
        print(ANSI_PURPLE + "Response: " + ANSI_YELLOW + resp.statusLine.toString() + ANSI_RESET)
        def readerText = reader.text
        JsonArray releasesListJsonArray = new JsonParser().parse(readerText).getAsJsonArray();
        for (JsonElement releaseListJson : releasesListJsonArray) {
            Release releaseList = gson.fromJson(releaseListJson, Release.class)
            existingReleases.add(releaseList)
        }
    }
}

List<Release> releasesAdded = new ArrayList<>(0);

println(ANSI_PURPLE + "Existing Media Releases found: " + ANSI_YELLOW + existingReleases.size() + ANSI_RESET)
println(ANSI_PURPLE + "Number of New Media Releases been added: " + ANSI_YELLOW + (releasesList.size() - existingReleases.size()) + ANSI_RESET)
for(Release release: releasesList) {
    Closure closureRelease = { it.title == release.title && it.snippet == release.snippet && it.site.url == release.site.url }
    Release existingRelease = existingReleases.find { closureRelease }

    if( existingRelease == null ) {
        println(ANSI_PURPLE + "Adding release: " + ANSI_YELLOW + release.title + ANSI_RESET)
        println(ANSI_RED + "Performing POST on: " + ANSI_YELLOW + 'release...' + ANSI_RESET)
        http.request(Method.POST, ContentType.JSON) { req ->
            uri.path = 'release'
            headers.Cookie = cookies.join(';')

            def attr = ["title": release.title, "url": release.url, "snippet": release.snippet, "dateCreated": release.dateCreated, "releaseDate": release.releaseDate, "site": release.site]
            body = (attr as JSON).toString()
            response.success = { resp, reader ->
                print(ANSI_PURPLE + "Response: " + ANSI_YELLOW + resp.statusLine.toString() + ANSI_RESET)
                println(ANSI_GREEN + "Media Release: " + release.title + " added" + ANSI_RESET)
                releasesAdded.add(release)
            }
            response.failure = { resp ->
                println(ANSI_RED + "Unexpected error: ${resp}" + ANSI_RESET)
            }
        }
    } else {
        println(ANSI_BLUE + "Release [" + release.title + "] already exists" + ANSI_RESET)
    }

    println("=============");

}

if(!releasesAdded.isEmpty()) {
    println(ANSI_BLUE + "Sending test email with [" + releasesAdded.size() + "] media releases" + ANSI_RESET)
    //Get Subscribers
    println(ANSI_RED + "Performing GET on: " + ANSI_YELLOW + 'subscriber...' + ANSI_RESET)
    http.request( Method.GET, ContentType.TEXT ) { req ->
        uri.path = 'subscriber'
        headers.Accept = 'application/json'
        headers.Cookie = cookies.join(';')

        response.success = { resp, reader ->
            print(ANSI_PURPLE + "Response: " + ANSI_YELLOW + resp.statusLine.toString() + ANSI_RESET)
            def readerText = reader.text
            JsonArray subscriberListJsonArray = new JsonParser().parse(readerText).getAsJsonArray();
            for (JsonElement subscriberListJson : subscriberListJsonArray) {
                Subscriber subscriberList = gson.fromJson(subscriberListJson, Subscriber.class)
                JsonArray subscriptionsLists = subscriberListJson.get("subscriptions").getAsJsonArray();
                for(JsonElement subscriptionListJson : subscriptionsLists) {
                    Agency agencyListItem = gson.fromJson(subscriptionListJson, Agency.class)
                    agencyListItem = agencyList.find{ ( it.id == agencyListItem.id ) }
                    subscriberList.addToSubscriptions(agencyListItem)
                    subscribers.add(subscriberList)
                }
            }
        }
    }

    for(Subscriber subscriber: subscribers) {
        println(ANSI_BLUE + "Checking user " + subscriber.email + ANSI_RESET)
        List<Release> releases = new ArrayList<>(0)
        for(Release allReleases: releasesList) {
            if(subscriber.subscriptions.find( { it.id == allReleases.site.agency.id } )) {
                releases.add(allReleases)
            }
        }

        if(!releases.isEmpty()) {
            println(ANSI_GREEN + "Sending email to [" + ANSI_YELLOW + subscriber.email + ANSI_GREEN + "]" + ANSI_RESET)

            String releaseString = "<h1>Media Releases</h1>"
            releaseString += "<h2>Your Subscriptions</h2>"
            releaseString += "<ul>"
            subscriber.subscriptions.each { if(it.portfolio != null && !it.portfolio.equals("")) {releaseString = releaseString + "<li>" + it.portfolio + "</li>"} };
            releaseString += "</ul>"
            releaseString += "<p>To modify your subscription click here</p>"
            releaseString += "<p>To unsubscribe click here</p>"
            releases.each { releaseString = releaseString + it.toEmailFormat() };

            String to = subscriber.email;
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Media Releases");
            message.setText(releaseString, "utf-8", "html");

            Transport.send(message);

        } else {
            println(ANSI_RED + "Don't send" + ANSI_RESET)
        }
    }
} else {
    println(ANSI_RED + "No new releases to send email for" + ANSI_RESET)
}

class CrawlerExtender extends WebCrawler {

    private static final Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz))\$")

    private String myCrawlDomains;
    private String createdMeta;
    private String descriptionMeta;
    private String agency;
    private String createRegex;
    private String mediaReleaseSelector

    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";
    String ANSI_YELLOW = "\u001B[33m";
    String ANSI_PURPLE = "\u001B[35m";

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
    public boolean shouldVisit(Page page, WebURL url) {
        String href = url.getURL().toLowerCase();
        if (FILTERS.matcher(href).matches()) {
            return false;
        }

        if (href.startsWith(myCrawlDomains)) {
            return true;
        }
        return false;
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        println(ANSI_PURPLE + "URL: [" + ANSI_YELLOW + url + ANSI_PURPLE + "]" + ANSI_RESET);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            println(ANSI_PURPLE + "Title: [" + ANSI_YELLOW + htmlParseData.getTitle() + ANSI_PURPLE + "]" + ANSI_RESET);

            Document doc = Jsoup.parse(html);
            Elements descriptionElements = doc.select("meta[name=" + descriptionMeta + "]");
            Elements createdElements = doc.select("meta[name=" + createdMeta + "]")
            if(mediaReleaseSelector != null) {
                Elements mediaReleaseSelector = doc.select(mediaReleaseSelector)
                if(mediaReleaseSelector == null || mediaReleaseSelector.isEmpty()) {
                    println(ANSI_RED + "Not a media release" + ANSI_RESET)
                    println("=============");
                    return
                }
            }
            if(descriptionElements != null && !descriptionElements.isEmpty() && createdElements != null && !createdElements.isEmpty()) {
                String description = descriptionElements.get(0).attr("content");
                String created = createdElements.get(0).attr("content");
                Date createdDate = Date.parse(createRegex, created)
                Release release = new Release(title: htmlParseData.getTitle(), snippet: description, url: page.getWebURL().getURL(), releaseDate: createdDate);

                ((List<Release>)myController.getCustomData()).add(release)
                println(ANSI_GREEN + "Media Release found" + ANSI_RESET)
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
