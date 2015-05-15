package subscriberpoc

class Topic {

    String name
    String description

    static hasMany = [agencies: Agency]

    static constraints = {
        name maxSize: 100, unique: true
        description nullable: true
    }
}
