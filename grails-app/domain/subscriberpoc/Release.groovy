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

    static belongsTo = [agency: Agency]



    static constraints = {
        title blank: false
        url url: true
        snippet maxSize:  1024
    }
}
