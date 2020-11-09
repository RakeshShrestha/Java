# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.

package app.config

import app.Router

import app.controllers.UserController

import org.koin.dsl.module

object ModulesConfig {
    private val configModule = module {
        single { AppConfig() }
        single { Router() }
    }
    private val userModule = module {
        single { UserController() }
    }
    private val profileModule = module {
    }
    internal val allModules = listOf(ModulesConfig.configModule, ModulesConfig.userModule,
    ModulesConfig.profileModule)
}
