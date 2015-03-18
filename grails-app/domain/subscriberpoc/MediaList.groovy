package subscriberpoc

class MediaList {

    String url
    String description
    String created

    static constraints = {
    }

    static belongsTo = [agency: Agency]

    static mapping = {
        agency lazy: false
    }
}
