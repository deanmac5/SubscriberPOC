package subscriberpoc

class Agency {

    String title
    String portfolio
    List urls

    static hasMany = [releases: Release, urls: String]



    static constraints = {
        title blank: false, maxSize: 100
        portfolio: blank: false
    }
}
