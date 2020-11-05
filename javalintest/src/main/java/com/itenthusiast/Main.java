package com.itenthisiast;

import io.javalin.Javalin;

import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;

import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;

import org.eclipse.jetty.util.ssl.SslContextFactory;

public class Main {

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.server(Main::createHttp2Server);
            config.addStaticFiles("/public");
        }).start();

        app.get("/", ctx -> ctx.result("Hello World from rakesh"));

    }

    private static Server createHttp2Server() {
		Server server = new Server();
		
		// HTTP 
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(80);
        server.addConnector(connector);
		
        // SSL Context Factory for HTTPS and HTTP/2
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(Main.class.getResource("/keystore.jks").toExternalForm()); // replace with your real keystore
        sslContextFactory.setKeyStorePassword("Happylion123"); // replace with your real password
		sslContextFactory.setIncludeProtocols("TLSv1.2");
		sslContextFactory.setIncludeCipherSuites("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");		
        
		// HTTPS Configuration
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSendServerVersion(false);
        httpConfig.setSecureScheme("https");
        httpConfig.setSecurePort(443);
        
        HttpConfiguration httpsConfig = new HttpConfiguration(httpConfig);
        httpsConfig.addCustomizer(new SecureRequestCustomizer());
        
		// HTTP/2 Connection Factory
        HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(httpsConfig);
        		
		ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
        alpn.setDefaultProtocol("h2");

        // SSL Connection Factory
        SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory, alpn.getProtocol());		
		
        // HTTP/2 Connector
        ServerConnector http2Connector = new ServerConnector(server, sslContextFactory);
        http2Connector.setPort(443);
        server.addConnector(http2Connector);
				
		return server;
    }
	
}
