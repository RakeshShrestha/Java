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

import io.javalin.Javalin
import io.javalin.util.HttpUtil
import app.config.AppConfig

import app.ErrorResponse

import org.eclipse.jetty.http.HttpStatus
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserControllerTest {
    private lateinit var app: Javalin
    private lateinit var http: HttpUtil

    @Before
    fun start() {
        app = AppConfig().setup()
        http = HttpUtil()
    }

    @After
    fun stop() {
        app.stop()
    }

    @Test
    fun `valid get users`() {
        val response = http.get("/users")

        assertEquals(response.status, HttpStatus.OK_200)
    }
	
}