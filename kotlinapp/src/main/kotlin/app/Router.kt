package app

import io.javalin.Javalin

import io.javalin.core.security.SecurityUtil.roles
import app.config.Roles

import app.controllers.UserController

import org.koin.core.KoinComponent
import org.koin.core.inject

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
