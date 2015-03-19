package subscriberpoc

class Site {

    String url
    String description
    String created
    String createdRegex
    String mediaReleaseSelector

    static constraints = {
    }

    static belongsTo = [agency: Agency]
    static hasMany = [releases: Release]

    static mapping = {
        agency lazy: false
    }

    String toString(){
        return url
    }
}
