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

import io.javalin.http.Context
import app.domain.ProfileDTO
import app.domain.services.UserService

class ProfileController(private val userService: UserService) {
	
	fun get(ctx: Context) {
		ctx.pathParam<String>("username").get().also { usernameFollowing ->
			userService.getProfileByUsername(ctx.attribute("email")!!, usernameFollowing).also { profile ->
				ctx.json(ProfileDTO(profile))
			}
		}
	}

	fun follow(ctx: Context) {
		ctx.pathParam<String>("username").get().also { usernameToFollow ->
			userService.follow(ctx.attribute("email")!!, usernameToFollow).also { profile ->
				ctx.json(ProfileDTO(profile))
			}
		}
	}

	fun unfollow(ctx: Context) {
		ctx.pathParam<String>("username").get().also { usernameToUnfollow ->
			userService.unfollow(ctx.attribute("email")!!, usernameToUnfollow).also { profile ->
				ctx.json(ProfileDTO(profile))
			}
		}
	}
	
}