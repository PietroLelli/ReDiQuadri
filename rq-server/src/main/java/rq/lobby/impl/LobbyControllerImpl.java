package rq.lobby.impl;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import rq.AbstractController;
import rq.Lobby;
import rq.User;
import rq.lobby.LobbyApi;
import rq.lobby.LobbyController;
import rq.utils.Filters;

public class LobbyControllerImpl extends AbstractController implements LobbyController {
    public LobbyControllerImpl(String path) {
        super(path);
    }

    private LobbyApi getApi(Context context) {
        return LobbyApi.of(getReDiQUadriInstance(context));
    }

    @Override
    public void postNewLobby(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var newLobby = context.bodyAsClass(Lobby.class);
        var futureResult = api.createLobby(newLobby);
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void getLobbiesAvailable(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);

        var futureResult = api.getLobbiesAvailable();
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void setLobbyUnavailable(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var lobbyId = context.bodyAsClass(String.class);
        var futureResult = api.setLobbyUnavailable(lobbyId);
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void getLobby(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var lobbyId = context.pathParam("{lobbyId}");
        var futureResult = api.getLobby(lobbyId);
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void addUserInLobby(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var lobby = context.pathParam("{lobbyId}");
        var user = context.bodyAsClass(User.class);
        var futureResult = api.addUserInLobby(lobby, user);
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void addNumber(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var number = context.pathParam("{number}");
        var user = context.bodyAsClass(User.class);
        var futureResult = api.addNumber(user, Integer.parseInt(number));
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void getPuntateUsers(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var lobbyId = context.pathParam("{lobbyId}");
        var futureResult = api.getPuntateUsers(lobbyId);
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void getWinnerOfLobby(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var lobbyId = context.pathParam("{lobbyId}");
        var futureResult = api.getWinnerOfLobby(lobbyId);
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void getCorrectNumberOfLobby(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var lobbyId = context.pathParam("{lobbyId}");
        var futureResult = api.getCorrectNumberOfLobby(lobbyId);
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void getEliminatedOfLobby(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var lobbyId = context.pathParam("{lobbyId}");
        var futureResult = api.getEliminatedOfLobby(lobbyId);
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void clearWinner(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var lobbyId = context.bodyAsClass(String.class);
        var futureResult = api.clearWinner(lobbyId);
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void removeLobbyById(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var lobbyId = context.pathParam("{lobbyId}");
        var futureResult = api.removeLobbyById(lobbyId);
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void removeLosersFromLobby(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var lobbyId = context.pathParam("{lobbyId}");
        var futureResult = api.removeLosersFromLobby(lobbyId);
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void clearPuntateUsers(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var lobbyId = context.pathParam("{lobbyId}");
        var futureResult = api.clearPuntateUsers(lobbyId);
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void resetAllLobbies(Context context) throws HttpResponseException{
        LobbyApi api = getApi(context);
        var futureResult = api.resetAllLobbies();
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }
    @Override
    public void registerRoutes(Javalin app) {
        app.before(path("*"), Filters.ensureClientAcceptsMimeType("application", "json"));
        app.post(path("/"), this::postNewLobby);
        app.post(path("/addUser/{lobbyId}"), this::addUserInLobby);
        app.post(path("/game/{number}"), this::addNumber);
        app.get(path("/game/{lobbyId}"), this::getWinnerOfLobby);
        app.get(path("/correctNumber/{lobbyId}"), this::getCorrectNumberOfLobby);
        app.get(path("/puntate/{lobbyId}"), this::getPuntateUsers);
        app.get(path("/loser/{lobbyId}"), this::getEliminatedOfLobby);
        app.get(path("/"), this::getLobbiesAvailable);
        app.get(path("/{lobbyId}"), this::getLobby);
        app.put(path("/"), this::clearWinner);
        app.put(path("/setUnavailable"), this::setLobbyUnavailable);
        app.delete(path("/clearPuntate/lobby/{lobbyId}"), this::clearPuntateUsers);
        app.delete(path("/remove/{lobbyId}"), this::removeLobbyById);
        app.delete(path("/removeLosers/{lobbyId}"), this::removeLosersFromLobby);
        app.delete(path("/reset/all"), this::resetAllLobbies);
    }
}
