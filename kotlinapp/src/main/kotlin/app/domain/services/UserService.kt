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

package app.domain.services

import io.javalin.http.BadRequestResponse
import io.javalin.http.HttpResponseException
import io.javalin.http.NotFoundResponse
import io.javalin.http.UnauthorizedResponse

import app.config.Roles

import app.domain.Profile
import app.domain.User
import app.domain.factories.UserFactory

import app.ext.Cipher
import app.ext.JwtProvider

import org.eclipse.jetty.http.HttpStatus

import java.util.*

class UserService(private val jwtProvider: JwtProvider, private val userFactory: UserFactory) {
	private val base64Encoder = Base64.getEncoder()

	fun create(user: User): User {
		userFactory.findByEmail(user.email).takeIf { it != null }?.apply {
			throw HttpResponseException(
					HttpStatus.BAD_REQUEST_400,
					"Email already registered!"
			)
		}
		userFactory.create(user.copy(password = String(base64Encoder.encode(Cipher.encrypt(user.password)))))
		return user.copy(token = generateJwtToken(user))
	}

	fun authenticate(user: User): User {
		val userFound = userFactory.findByEmail(user.email)
		if (userFound?.password == String(base64Encoder.encode(Cipher.encrypt(user.password)))) {
			return userFound.copy(token = generateJwtToken(userFound))
		}
		throw UnauthorizedResponse("email or password invalid!")
	}

	fun getByEmail(email: String?): User {
		if (email.isNullOrBlank()) throw BadRequestResponse()
		val user = userFactory.findByEmail(email)
		user ?: throw NotFoundResponse()
		return user.copy(token = generateJwtToken(user))
	}

	fun getProfileByUsername(email: String, usernameFollowing: String?): Profile {
		if (usernameFollowing == null || usernameFollowing.isNullOrBlank()) throw BadRequestResponse()
		return userFactory.findByUsername(usernameFollowing).let { user ->
			user ?: throw NotFoundResponse()
			Profile(user.username, user.bio, user.image, user.firstname, user.lastname, user.contactmobile, user.country, user.perms, user.status, user.createdAt, userFactory.findIsFollowUser(email, user.id!!))
		}
	}

	fun update(email: String?, user: User): User? {
		email ?: throw HttpResponseException(HttpStatus.NOT_ACCEPTABLE_406, "User not found to update.")
		return userFactory.update(email, user)
	}

	private fun generateJwtToken(user: User): String? {
		return jwtProvider.createJWT(user, Roles.AUTHENTICATED)
	}

	fun follow(email: String, usernameToFollow: String): Profile {
		return userFactory.follow(email, usernameToFollow).let { user ->
			Profile(user.username, user.bio, user.image, user.firstname, user.lastname, user.contactmobile, user.country, user.perms, user.status, user.createdAt, true)
		}
	}

	fun unfollow(email: String, usernameToUnfollow: String): Profile {
		return userFactory.unfollow(email, usernameToUnfollow).let { user ->
			Profile(user.username, user.bio, user.image, user.firstname, user.lastname, user.contactmobile, user.country, user.perms, user.status, user.createdAt, false)
		}
	}
	
}