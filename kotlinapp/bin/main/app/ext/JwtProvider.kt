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

package app.ext

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import io.javalin.core.security.Role

import app.domain.User

import java.util.*

class JwtProvider {

	fun decodeJWT(token: String): DecodedJWT = JWT.require(Cipher.algorithm).build().verify(token)

	fun createJWT(user: User, role: Role): String? =
		JWT.create()
		.withIssuedAt(Date())
		.withSubject(user.email)
		.withClaim("role", role.toString())
		.withExpiresAt(Date(System.currentTimeMillis() + 1 * 24 * 60 * 60 * 1000))
		.sign(Cipher.algorithm)
	
}