package rq;

import io.javalin.Javalin;

public interface Controller {

    String path();

    String path(String subPath);

    void registerRoutes(Javalin app);
}
