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

import app.domain.factories.UserFactory
import app.domain.services.UserService

import app.ext.JwtProvider

import app.Router

import app.controllers.ProfileController
import app.controllers.UserController

import org.koin.dsl.module

object Modules {
	private val config = module {
		single { App() }
		single { JwtProvider() }
		single { Auth(get()) }
        single {
            Db().getDataSource()
        }
		single { Router(get(), get()) }
	}
	private val user = module {
		single { UserController(get()) }
        single { UserService(get(), get()) }
        single { UserFactory(get()) }		
	}
	private val profile = module {
        single { ProfileController(get()) }
	}
	internal val allModules = listOf(
		Modules.config, Modules.user, Modules.profile
	)
}
