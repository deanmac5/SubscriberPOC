class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:"subscriber", action: "create")
        "500"(view:'/error')

        "/api/agency"(resources: 'agency')
        "/api/site"(resources: 'site')
        "/api/release"(resources: 'release')
        "/api/user"(resources: 'user')
        "/agency"(resources: 'agency')
        "/site"(resources: 'site')
        "/release"(resources: 'release')
        "/user"(resources: 'user')
	}
}
