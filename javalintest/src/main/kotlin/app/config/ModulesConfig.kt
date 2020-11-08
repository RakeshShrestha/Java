package app.config

import app.web.Router

import app.web.controllers.UserController

import org.koin.dsl.module.module

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
