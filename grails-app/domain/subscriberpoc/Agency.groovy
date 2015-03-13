package subscriberpoc

class Agency {

    String title
    String portfolio

    String domain

    static constraints = {
        title blank: false, maxSize: 300
        portfolio: blank: false
    }
}
