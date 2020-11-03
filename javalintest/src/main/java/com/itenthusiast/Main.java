package com.itenthisiast;

import io.javalin.Javalin;

public class Main {

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
        }).start(8080);

		app.get("/", ctx -> ctx.result("Hello World from rakesh"));
		
    }
}
