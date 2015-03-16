package subscriberpoc

class Agency {

    String title
    String portfolio
    def urlList

    static hasMany = [releases: Release]



    static constraints = {
        title blank: false, maxSize: 100
        portfolio: blank: false
    }
}
