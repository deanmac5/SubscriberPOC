package subscriberpoc

class Site {

    String url
    String description
    String created

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