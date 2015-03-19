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

    static belongsTo = [site: Site]

    static mapping = {
        autoTimestamp true
    }


    static constraints = {
        title blank: false
        url url: true
        snippet maxSize:  1024
    }
}
