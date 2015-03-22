package subscriberpoc

/*
 * To be created by reading metadata
 * when the cron job trolls agency url
 */
class Release {

    String url
    String title
    String snippet
    Date releaseDate
    Date dateCreated
    boolean isMediaRelease = true

    static belongsTo = [site: Site]

    static mapping = {
        autoTimestamp true
    }

    static constraints = {
        title blank: false
        url url: true
        snippet maxSize:  1024

    }

    String toString(){
        return title
    }

    String toEmailFormat() {
        return "<h2><a href='" + url + "'>" + title + "</a></h2>" + "<p>" + snippet + "</p>"
    }
}
