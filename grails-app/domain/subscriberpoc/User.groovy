package subscriberpoc

class User {

	transient springSecurityService

	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
    String email

    static hasMany = [subscriptions: Agency]

	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: true
		password blank: false, validator: {passwd, user ->
			passwd != user.username
		}
        email email: true // unique: true //TODO put this in once prototyping is complete
	}

	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role }
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}
}
