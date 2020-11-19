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

package app.controllers

import app.domain.UserDTO
import app.domain.services.UserService
import app.ext.isEmailValid

import io.javalin.http.Context

class UserController(private val userService: UserService) {
	
    fun login(ctx: Context) {
        ctx.bodyValidator<UserDTO>()
            .check({ it.user?.email?.isEmailValid() ?: true })
            .check({ !it.user?.password.isNullOrBlank() })
            .get().user?.also { user ->
            	userService.authenticate(user).apply {
                ctx.json(UserDTO(this))
            }
        }
    }

    fun register(ctx: Context) {
        ctx.bodyValidator<UserDTO>()
            .check({ it.user?.email?.isEmailValid() ?: true })
            .check({ !it.user?.password.isNullOrBlank() })
            .check({ !it.user?.username.isNullOrBlank() })
            .get().user?.also { user ->
            	userService.create(user).apply {
                ctx.json(UserDTO(this))
            }
        }
    }

    fun getCurrent(ctx: Context) {
        userService.getByEmail(ctx.attribute("email")).also { user ->
            ctx.json(UserDTO(user))
        }
    }

    fun update(ctx: Context) {
        val email = ctx.attribute<String>("email")
        ctx.bodyValidator<UserDTO>()
            .check({ it.user != null })
            .check({ it.user?.email?.isEmailValid() ?: true })
            .check({ it.user?.username?.isNotBlank() ?: true })
            .check({ it.user?.password?.isNotBlank() ?: true })
            .check({ it.user?.bio?.isNotBlank() ?: true })
            .check({ it.user?.image?.isNotBlank() ?: true })
            .get()
            .user?.also { user ->
            	userService.update(email, user).apply {
                ctx.json(UserDTO(this))
            }
        }
    }
	
}