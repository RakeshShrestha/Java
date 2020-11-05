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
		
		ServerConnector sslConnector = new ServerConnector(server, getSslContextFactory());
		sslConnector.setPort(443);
        server.addConnector(sslConnector);
		
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(80);
        server.addConnector(connector);
				
		return server;
    }
	
    private static SslContextFactory getSslContextFactory() {
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(Main.class.getResource("/keystore.jks").toExternalForm());
        sslContextFactory.setKeyStorePassword("Happylion123");
        return sslContextFactory;
    }
	
}
