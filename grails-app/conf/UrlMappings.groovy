class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')

        "/api/agency"(resources: 'agency')
        "/api/site"(resources: 'site')
        "/api/release"(resources: 'release')
        "/agency"(resources: 'agency')
        "/site"(resources: 'site')
        "/release"(resources: 'release')
	}
}
