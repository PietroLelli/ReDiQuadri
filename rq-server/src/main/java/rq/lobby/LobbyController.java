package rq.lobby;

import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import io.javalin.openapi.*;
import rq.Controller;
import rq.Lobby;
import rq.ReDiQuadriService;
import rq.User;
import rq.lobby.impl.LobbyControllerImpl;

public interface LobbyController extends Controller {


    @OpenApi(
            operationId = "LobbyApi::createLobby",
            path = ReDiQuadriService.BASE_URL + "/lobbies",
            methods = {HttpMethod.POST},
            tags = {"lobbies"},
            description = "Create a new Lobby",
            requestBody = @OpenApiRequestBody(
                    description = "The lobbies's data",
                    required = true,
                    content = {
                            @OpenApiContent(
                                    from = Lobby.class,
                                    mimeType = ContentType.JSON
                            )
                    }
            ),
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "The id of the newly created lobby",
                            content = {
                                    @OpenApiContent(
                                            from = Lobby[].class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request: some field is missing or invalid"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided identifier corresponds to no known element"
                    ),
                    @OpenApiResponse(
                            status = "409",
                            description = "Conflict: some identifier has already been taken"
                    )
            }
    )
    void postNewLobby(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "LobbyApi::getLobbiesAvailable",
            path = ReDiQuadriService.BASE_URL + "/lobbies/",
            methods = {HttpMethod.GET},
            tags = {"lobbies"},
            description = "Retrieves all lobbies",
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "A List containing  the available lobbies.",
                            content = {
                                    @OpenApiContent(
                                            from = Lobby[].class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request: some field is missing or invalid"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided identifier corresponds to no known element"
                    ),
                    @OpenApiResponse(
                            status = "409",
                            description = "Conflict: some identifier has already been taken"
                    )
            }
    )
    void getLobbiesAvailable(Context context) throws HttpResponseException;
    @OpenApi(
            operationId = "LobbyApi::setLobbyUnavailable",
            path = ReDiQuadriService.BASE_URL + "/lobbies//setUnavailable/{lobbyId}",
            methods = {HttpMethod.PUT},
            tags = {"lobbies"},
            description = "Edits a lobby",
            pathParams = {
                    @OpenApiParam(
                            name = "lobbyId",
                            type = String.class,
                            description = "Id of the lobby is being requested",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "Id of lobby",
                            content = {
                                    @OpenApiContent(
                                            from = String.class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request: some field is missing or invalid"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided identifier corresponds to no known element"
                    ),
                    @OpenApiResponse(
                            status = "409",
                            description = "Conflict: some identifier has already been taken"
                    )
            }
    )
    void setLobbyUnavailable(Context context) throws HttpResponseException;
    @OpenApi(
            operationId = "LobbyApi::getLobby",
            path = ReDiQuadriService.BASE_URL + "/lobbies/{lobbyId}",
            methods = {HttpMethod.GET},
            tags = {"lobbies"},
            description = "get lobby from Id",
            pathParams = {
                    @OpenApiParam(
                            name = "lobbyId",
                            type = String.class,
                            description = "id of lobby",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "The provided identifier corresponds to a user, whose data is thus returned",
                            content = {
                                    @OpenApiContent(
                                            from = Lobby.class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request: some field is missing or invalid"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided identifier corresponds to no known element"
                    ),
                    @OpenApiResponse(
                            status = "409",
                            description = "Conflict: some identifier has already been taken"
                    )
            }
    )
    void getLobby(Context context) throws HttpResponseException;
    @OpenApi(
            operationId = "LobbyApi::addUserInLobby",
            path = ReDiQuadriService.BASE_URL + "/lobbies/addUser/{lobbyId}",
            methods = {HttpMethod.POST},
            tags = {"lobbies"},
            description = "Put the user corresponding to the nickname passed, in the lobby corresponding to the id passed.",
            pathParams = {
                    @OpenApiParam(
                            name = "lobbyId",
                            type = String.class,
                            description = "id of lobby",
                            required = true
                    )
            },
            requestBody = @OpenApiRequestBody(
                    description = "the user",
                    required = true,
                    content = {
                            @OpenApiContent(
                                    from = User.class,
                                    mimeType = ContentType.JSON
                            )
                    }
            ),
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "The provided id corresponds to a lobby, the user is now connected to the lobby, nothing is returned"
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request: some field is missing or invalid"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided identifier corresponds to no known element"
                    ),
                    @OpenApiResponse(
                            status = "409",
                            description = "Conflict: some identifier has already been taken"
                    )
            }
    )
    void addUserInLobby(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "LobbyApi::addNumber",
            path = ReDiQuadriService.BASE_URL + "/lobbies/game/{number}",
            methods = {HttpMethod.POST},
            tags = {"lobbies"},
            description = "add the choosen number",
            pathParams = {
                    @OpenApiParam(
                            name = "number",
                            type = Integer.class,
                            description = "the choosen number",
                            required = true
                    )
            },
            requestBody = @OpenApiRequestBody(
                    description = "the user who chose the number\n",
                    required = true,
                    content = {
                            @OpenApiContent(
                                    from = User.class,
                                    mimeType = ContentType.JSON
                            )
                    }
            ),
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "The id of the newly created lobby"
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request: some field is missing or invalid"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided identifier corresponds to no known element"
                    ),
                    @OpenApiResponse(
                            status = "409",
                            description = "Conflict: some identifier has already been taken"
                    )
            }
    )
    void addNumber(Context context) throws HttpResponseException;
    @OpenApi(
            operationId = "LobbyApi::getPuntateUser",
            path = ReDiQuadriService.BASE_URL + "/lobbies/puntate/{lobbyId}",
            methods = {HttpMethod.GET},
            tags = {"lobbies"},
            description = "get numbers chosen by the players)",
            pathParams = {
                    @OpenApiParam(
                            name = "lobbyId",
                            type = String.class,
                            description = "id of the lobby",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "numbers chosen by users",
                            content = {
                                    @OpenApiContent(
                                            from = Integer[].class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request: some field is missing or invalid"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided identifier corresponds to no known element"
                    ),
                    @OpenApiResponse(
                            status = "409",
                            description = "Conflict: some identifier has already been taken"
                    )
            }
    )
    void getPuntateUsers(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "LobbyApi::getWinnerOfLobby",
            path = ReDiQuadriService.BASE_URL + "/lobbies/game/{lobbyId}",
            methods = {HttpMethod.GET},
            tags = {"lobbies"},
            description = "get the winner of the lobby in this round",
            pathParams = {
                    @OpenApiParam(
                            name = "lobbyId",
                            type = String.class,
                            description = "the lobby's id",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "nickname of the winner",
                            content = {
                                    @OpenApiContent(
                                            from = String.class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request: some field is missing or invalid"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided identifier corresponds to no known element"
                    ),
                    @OpenApiResponse(
                            status = "409",
                            description = "Conflict: some identifier has already been taken"
                    )
            }
    )
    void getWinnerOfLobby(Context context) throws HttpResponseException;
    @OpenApi(
            operationId = "LobbyApi::getCorrectNumberOfLobby",
            path = ReDiQuadriService.BASE_URL + "/lobbies/correctNumber/{lobbyId}",
            methods = {HttpMethod.GET},
            tags = {"lobbies"},
            description = "get the correct number",
            pathParams = {
                    @OpenApiParam(
                            name = "lobbyId",
                            type = String.class,
                            description = "id of the lobby",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "correct number",
                            content = {
                                    @OpenApiContent(
                                            from = Integer.class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request: some field is missing or invalid"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided identifier corresponds to no known element"
                    ),
                    @OpenApiResponse(
                            status = "409",
                            description = "Conflict: some identifier has already been taken"
                    )
            }
    )
    void getCorrectNumberOfLobby(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "LobbyApi::getEliminatedOfLobby",
            path = ReDiQuadriService.BASE_URL + "/lobbies/loser/{lobbyId}",
            methods = {HttpMethod.GET},
            tags = {"lobbies"},
            description = "get the eliminated users",
            pathParams = {
                    @OpenApiParam(
                            name = "lobbyId",
                            type = String.class,
                            description = "id of the lobby",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "nicknames of losers in this round",
                            content = {
                                    @OpenApiContent(
                                            from = String[].class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request: some field is missing or invalid"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided identifier corresponds to no known element"
                    ),
                    @OpenApiResponse(
                            status = "409",
                            description = "Conflict: some identifier has already been taken"
                    )
            }
    )
    void getEliminatedOfLobby(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "LobbyApi::clearWinner",
            path = ReDiQuadriService.BASE_URL + "/lobbies/{lobbyId}",
            methods = {HttpMethod.PUT},
            tags = {"lobbies"},
            description = "clear the winner for the next round",
            pathParams = {
                    @OpenApiParam(
                            name = "lobbyId",
                            type = String.class,
                            description = "the lobby id",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request: some field is missing or invalid"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided identifier corresponds to no known element"
                    ),
                    @OpenApiResponse(
                            status = "409",
                            description = "Conflict: some identifier has already been taken"
                    )
            }
    )
    void clearWinner(Context context) throws HttpResponseException;

    void removeLobbyById(Context context) throws HttpResponseException;
    void removeLosersFromLobby(Context context) throws HttpResponseException;


    void clearPuntateUsers(Context context) throws HttpResponseException;
    @OpenApi(
            operationId = "LobbyApi::resetAllLobbies",
            path = ReDiQuadriService.BASE_URL + "/lobbies/reset/all",
            methods = {HttpMethod.DELETE},
            tags = {"lobbies"},
            description = "Deletes a user, given some identifier of theirs (either a username or an email address)",

            responses = {
                    @OpenApiResponse(
                            status = "201",
                            description = "The provided identifier corresponds to a user, which is thus removed. Nothing is returned"
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request: some field is missing or invalid"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided identifier corresponds to no known element"
                    ),
                    @OpenApiResponse(
                            status = "409",
                            description = "Conflict: some identifier has already been taken"
                    )
            }
    )
    void resetAllLobbies(Context context) throws HttpResponseException;


    static LobbyController of(String root) {
        return new LobbyControllerImpl(root);
    }
}
