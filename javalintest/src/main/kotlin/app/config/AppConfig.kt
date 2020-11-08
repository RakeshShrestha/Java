package app.config

import io.javalin.Javalin

import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory
import org.eclipse.jetty.http2.HTTP2Cipher
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory
import org.eclipse.jetty.server.*
import org.eclipse.jetty.util.ssl.SslContextFactory

import app.config.ModulesConfig.allModules
import app.web.Router

import org.koin.core.KoinProperties
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext
import org.koin.standalone.getProperty
import org.koin.standalone.inject

class AppConfig : KoinComponent {
    private val router: Router by inject()

    fun setup(): Javalin {
	
        StandAloneContext.startKoin(
                allModules,
                KoinProperties(true, true)
        )
		
        val app = Javalin.create { 
			it.server { createHttp2Server() }
			it.addStaticFiles("/public")		
        }.events {
            it.serverStopping {
                StandAloneContext.stopKoin()
            }
        }.start()
	
		router.register(app)
		
		app.get("/") { it.result("Hello from rakesh") }
		
		return app
    }

    private fun createHttp2Server(): Server {

		val alpn = ALPNServerConnectionFactory().apply {
			defaultProtocol = "h2"
		}

		val sslContextFactory = SslContextFactory().apply {
			keyStorePath = this::class.java.getResource("/keystore.jks").toExternalForm() // replace with your real keystore
			setKeyStorePassword("password") // replace with your real password
			cipherComparator = HTTP2Cipher.COMPARATOR
			provider = "Conscrypt"
		}

		val ssl = SslConnectionFactory(sslContextFactory, alpn.protocol)

		val httpsConfig = HttpConfiguration().apply {
			sendServerVersion = false
			secureScheme = "https"
			securePort = 443
			addCustomizer(SecureRequestCustomizer())
		}

		val http2 = HTTP2ServerConnectionFactory(httpsConfig)

		val fallback = HttpConnectionFactory(httpsConfig)

		return Server().apply {
			//HTTP/1.1 Connector
			addConnector(ServerConnector(server).apply {
				port = 80
			})
			// HTTP/2 Connector
			addConnector(ServerConnector(server, sslContextFactory).apply {
				port = 443
			})
		}
    }
}

