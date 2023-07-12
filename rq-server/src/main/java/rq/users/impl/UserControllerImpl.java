package rq.users.impl;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import rq.AbstractController;
import rq.User;
import rq.users.UserApi;
import rq.users.UserController;
import rq.utils.Filters;


public class UserControllerImpl extends AbstractController implements UserController {
    public UserControllerImpl(String path) {
        super(path);
    }

    private UserApi getApi(Context context) { return UserApi.of(getReDiQUadriInstance(context)); }

    @Override
    public void postNewUser(Context context) throws HttpResponseException {
        UserApi api = getApi(context);
        var newUser = context.bodyAsClass(User.class);
        var futureResult = api.registerUser(newUser);
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void getUserById(Context context) throws HttpResponseException {
        UserApi api = getApi(context);
        var userId = context.pathParam("{userId}");
        var futureResult = api.getUserById(userId);
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void lostLife(Context context) throws HttpResponseException {
        UserApi api = getApi(context);
        var nickname = context.bodyAsClass(String.class);
        var lobbyId = context.pathParam("{lobbyId}");
        var futureResult = api.lostLife(nickname, lobbyId);
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void removeUserFromLobby(Context context) throws HttpResponseException {
        UserApi api = getApi(context);
        var nickname = context.pathParam("{nickname}");
        var futureResult = api.removeUserFromLobby(nickname);
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void removeUserById(Context context) throws HttpResponseException {
        UserApi api = getApi(context);
        var nickname = context.pathParam("{nickname}");
        var futureResult = api.removeUserById(nickname);
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void setUserLastSignal(Context context) throws HttpResponseException {
        UserApi api = getApi(context);
        var nickname = context.pathParam("{nickname}");
        var futureResult = api.setUserLastSignal(nickname);
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }


    @Override
    public void registerRoutes(Javalin app) {
        app.before(path("*"), Filters.ensureClientAcceptsMimeType("application", "json"));
        app.post(path("/"), this::postNewUser);
        app.get(path("/{userId}"), this::getUserById);
        app.put(path("/{lobbyId}"), this::lostLife);
        app.put(path("/lastSignal/{nickname}"), this::setUserLastSignal);
        app.delete(path("/{nickname}"), this::removeUserFromLobby);
        app.delete(path("/remove/{nickname}"), this::removeUserById);

    }
}
