package subscriberpoc

class Subscriber {

    String email
    String confirmCode
    boolean verified

    static hasMany = [topics: Topic]

    static constraints = {
        email email:true, unique: true
        confirmCode blank: true
    }
}
