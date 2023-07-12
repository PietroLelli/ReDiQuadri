package rq;

import io.javalin.Javalin;
import rq.lobby.LobbyController;
import rq.users.UserController;
import rq.utils.Filters;
import rq.utils.Plugins;

import java.util.Timer;


public class ReDiQuadriService {
    private static final String API_VERSION = "0.1.0";
    public static final String BASE_URL = "/rediquadri/v" + API_VERSION;
    private static final int DEFAULT_PORT = 10000;
    private final int port;
    private final Javalin server;

    public ReDiQuadriService(int port) {
        this.port = port;
        server = Javalin.create(config -> {
            config.plugins.enableDevLogging();
            config.jsonMapper(new JavalinGsonAdapter(GsonUtils.createGson()));
            config.plugins.register(Plugins.openApiPlugin(API_VERSION, "/doc"));
            config.plugins.register(Plugins.swaggerPlugin("/doc", "/ui"));
            config.plugins.register(Plugins.routeOverviewPlugin("/routes"));
        });

        ReDiQuadri localReDiQuadri = new LocalReDiQuadri();
        server.before(path("/*"), Filters.putSingletonInContext(ReDiQuadri.class, localReDiQuadri));
        UserController.of(path("/users")).registerRoutes(server);
        LobbyController.of(path("/lobbies")).registerRoutes(server);

        Timer timer = new Timer();
        timer.schedule(new LocalReDiQuadri.CheckUserIsAlive(), 0, 2000);
    }

    public void start() {
        server.start(port);
    }

    public void stop() {
        server.stop();
    }

    private static String path(String subPath) {
        return BASE_URL + subPath;
    }

    public static void main(String[] args) {
        new ReDiQuadriService(args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT).start();
    }
}
