package app.web.controllers

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
