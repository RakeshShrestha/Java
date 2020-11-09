# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.

package app.controllers

import io.javalin.http.Context

class UserController() {
	fun login(ctx: Context) {
		ctx.result("users login")
	}

	fun register(ctx: Context) {
		ctx.result("users register")
	}

	fun getCurrent(ctx: Context) {
		ctx.result("users getcurrent")
	}

	fun update(ctx: Context) {
		ctx.result("users update")
	}
}
