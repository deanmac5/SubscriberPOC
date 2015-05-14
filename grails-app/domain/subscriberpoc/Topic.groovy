package subscriberpoc

class Topic {

    String name
    String description

    static hasMany = [agencies: Agency]

    static constraints = {
        description blank: true
    }
}
