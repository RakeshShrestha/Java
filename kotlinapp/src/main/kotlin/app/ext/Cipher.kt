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

import com.auth0.jwt.algorithms.Algorithm

object Cipher {
	val algorithm = Algorithm.HMAC256("something-very-secret-here")

	fun encrypt(data: String?): ByteArray = algorithm.sign(data?.toByteArray())

}