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

package app.config

import com.auth0.jwt.interfaces.DecodedJWT

import io.javalin.Javalin
import io.javalin.core.security.Role
import io.javalin.http.Context
import io.javalin.http.ForbiddenResponse

import app.ext.JwtProvider

internal enum class Roles : Role {
	ANYONE, AUTHENTICATED
}

private const val headerTokenName = "Authorization"

class Auth(private val jwtProvider: JwtProvider) {
	
	fun configure(app: Javalin) {
		app.config.accessManager { handler, ctx, permittedRoles ->
			val jwtToken = getJwtTokenHeader(ctx)
			val userRole = getUserRole(jwtToken) ?: Roles.ANYONE
			permittedRoles.takeIf { !it.contains(userRole) }?.apply { throw ForbiddenResponse() }
			ctx.attribute("email", getEmail(jwtToken))
			handler.handle(ctx)
		}
	}

	private fun getJwtTokenHeader(ctx: Context): DecodedJWT? {
		val tokenHeader = ctx.header(headerTokenName)?.substringAfter("Token")?.trim() ?: return null

		return jwtProvider.decodeJWT(tokenHeader)
	}

	private fun getEmail(jwtToken: DecodedJWT?): String? {
		return jwtToken?.subject
	}

	private fun getUserRole(jwtToken: DecodedJWT?): Role? {
		val userRole = jwtToken?.getClaim("role")?.asString() ?: return null
		return Roles.valueOf(userRole)
	}
	
}