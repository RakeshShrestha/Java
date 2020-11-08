package app.web

import io.javalin.Javalin

import io.javalin.core.security.SecurityUtil.roles
import app.config.Roles

import app.web.controllers.UserController

import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class Router() : KoinComponent {
	val userController: UserController by inject()

	fun register(app: Javalin) {
		app.get("users") { ctx ->
			userController.register(ctx)
		}
		app.get("user") { ctx ->
			userController.login(ctx)
		}
	}
}
