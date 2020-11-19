/**
# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.
 */

package app

import io.javalin.Javalin

import io.javalin.apibuilder.ApiBuilder.delete
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.ApiBuilder.put

import io.javalin.core.security.SecurityUtil.roles
import app.config.Roles

import app.controllers.ProfileController
import app.controllers.UserController

import org.koin.core.KoinComponent
import org.koin.core.inject

class Router(
    private val userController: UserController,
    private val profileController: ProfileController
) : KoinComponent {

	fun register(app: Javalin) {		
		val rolesOptionalAuthenticated = roles(Roles.ANYONE, Roles.AUTHENTICATED)
		
		app.routes {
			path("users") {
                post(userController::register, roles(Roles.ANYONE))
                post("login", userController::login, roles(Roles.ANYONE))
			}
            path("user") {
                get(userController::getCurrent, roles(Roles.AUTHENTICATED))
                put(userController::update, roles(Roles.AUTHENTICATED))
            }
            path("profiles/:username") {
                get(profileController::get, rolesOptionalAuthenticated)
                path("follow") {
                    post(profileController::follow, roles(Roles.AUTHENTICATED))
                    delete(profileController::unfollow, roles(Roles.AUTHENTICATED))
                }
            }
		}

	}
	
}
