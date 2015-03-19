package subscriberpoc

class Agency {

    String title
    String portfolio
//    List sites

    static hasMany = [sites: Site]

    static mapping = {
        sites lazy: false
    }



    static constraints = {
        title blank: false, maxSize: 100
        portfolio: blank: false
    }

    String toString() {
        return title
    }
}
