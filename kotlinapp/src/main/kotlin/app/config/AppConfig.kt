# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.

package app.config

import io.javalin.Javalin

import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory
import org.eclipse.jetty.http2.HTTP2Cipher
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory
import org.eclipse.jetty.server.*
import org.eclipse.jetty.util.ssl.SslContextFactory

import app.config.ModulesConfig.allModules
import app.Router

import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class AppConfig : KoinComponent {
    private val router: Router by inject()

    fun setup(): Javalin {
	
		startKoin {
			modules(allModules)
		}
		
        val app = Javalin.create { 
			it.server { createHttpServer() }
			it.enableCorsForAllOrigins()
			it.dynamicGzip = true
			/*it.enableDevLogging()*/
			it.enableWebjars()  			
			it.addStaticFiles("/public")		
        }.events {
            it.serverStopping {
                stopKoin()
            }
        }.start()

		router.register(app)

		app.get("/") { it.result("Hello from rakesh") }

		return app
    }

    private fun createHttpServer(): Server {

		ALPNServerConnectionFactory().apply {
			defaultProtocol = "h2"
		}

		val sslContextFactory = SslContextFactory().apply {
			keyStorePath = this::class.java.getResource("/keystore.jks").toExternalForm()
			setKeyStorePassword("password") // replace with your real password
			cipherComparator = HTTP2Cipher.COMPARATOR
		}

		val httpsConfig = HttpConfiguration().apply {
			sendServerVersion = false
			secureScheme = "https"
			securePort = 443
			addCustomizer(SecureRequestCustomizer())
		}

		HTTP2ServerConnectionFactory(httpsConfig)

		return Server().apply {
			addConnector(ServerConnector(server).apply {
				port = 80
			})
			addConnector(ServerConnector(server, sslContextFactory).apply {
				port = 443
			})
		}
    }
}

