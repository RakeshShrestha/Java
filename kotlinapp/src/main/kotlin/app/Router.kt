# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.

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
