package app.web

import io.javalin.Javalin

import io.javalin.apibuilder.ApiBuilder.delete
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.ApiBuilder.put

import io.javalin.core.security.SecurityUtil.roles
import app.config.Roles

import app.web.controllers.UserController

import org.koin.standalone.KoinComponent

class Router(
    private val userController: UserController
) : KoinComponent {

    fun register(app: Javalin) {
        app.routes {
            path("users") {
                post(userController::register)
                post("login", userController::login)
            }
            path("user") {
                get(userController::getCurrent)
                put(userController::update)
            }
        }
		
    }
}
