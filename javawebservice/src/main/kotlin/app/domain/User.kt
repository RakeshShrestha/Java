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

package app.domain

import java.util.*
import org.joda.time.DateTime

data class UserDTO(val user: User? = null)

data class User(
	val id: Long? = null,
	val email: String,
	val token: String? = null,
	val username: String? = null,
	val password: String? = null,
	
	val firstname: String? = null,
	val lastname: String? = null,
	val contactmobile: String? = null,

	val country: String? = null,
	val perms: String? = null,
	val status: String? = null,
	val remarks: String? = null,
	val ipregistered: String? = null,

	val bio: String? = null,
	val image: String? = null,
	
	val createdAt: Date? = null,
	val updatedAt: Date? = null	
)