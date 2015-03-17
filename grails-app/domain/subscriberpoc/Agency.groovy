package subscriberpoc

class Agency {

    String title
    String portfolio
    List mediaLists

    static hasMany = [releases: Release, mediaLists: MediaList]



    static constraints = {
        title blank: false, maxSize: 100
        portfolio: blank: false
    }
}
