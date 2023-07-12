package rq.users;

import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import io.javalin.openapi.*;
import rq.Controller;
import rq.ReDiQuadriService;
import rq.User;
import rq.users.impl.UserControllerImpl;

public interface UserController extends Controller {

    @OpenApi(
            operationId = "UserApi::registerUser",
            path = ReDiQuadriService.BASE_URL + "/users",
            methods = {HttpMethod.POST},
            tags = {"users"},
            description = "Registers new user",
            requestBody = @OpenApiRequestBody(
                    description = "The user's data",
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
                            description = "The username of the newly created user",
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
    void postNewUser(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "UserApi::getUser",
            path = ReDiQuadriService.BASE_URL + "/users/{userId}",
            methods = {HttpMethod.GET},
            tags = {"users"},
            description = "Gets the data of a user by his id",
            pathParams = {
                    @OpenApiParam(
                            name = "userId",
                            type = String.class,
                            description = "id of user",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "user returned",
                            content = {
                                    @OpenApiContent(
                                            from = User.class,
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
    void getUserById(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "UserApi::lostLife",
            path = ReDiQuadriService.BASE_URL + "/users/{lobbyId}",
            methods = {HttpMethod.PUT},
            tags = {"users"},
            description = "Edits a user, remove a life",
            pathParams = {
                    @OpenApiParam(
                            name = "lobbyId",
                            type = String.class,
                            description = "id of lobby",
                            required = true
                    )
            },
            requestBody = @OpenApiRequestBody(
                    description = "Nickname of the user who loses the life",
                    required = true,
                    content = {
                            @OpenApiContent(
                                    from = String.class,
                                    mimeType = ContentType.JSON
                            )
                    }
            ),
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "id of user",
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
    void lostLife(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "UserApi::removeUserFromLobby",
            path = ReDiQuadriService.BASE_URL + "/users/{nickname}",
            methods = {HttpMethod.DELETE},
            tags = {"users"},
            description = "Deletes a user from a lobby",
            pathParams = {
                    @OpenApiParam(
                            name = "nickname",
                            type = String.class,
                            description = "nickname of user",
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
    void removeUserFromLobby(Context context) throws HttpResponseException;
    void removeUserById(Context context) throws HttpResponseException;


    @OpenApi(
            operationId = "UserApi::setUserLastSignal",
            path = ReDiQuadriService.BASE_URL + "/users/lastSignal/{nickname}",
            methods = {HttpMethod.PUT},
            tags = {"users"},
            description = "Set the last signal of a user",
            pathParams = {
                    @OpenApiParam(
                            name = "nickname",
                            type = String.class,
                            description = "nickname of user",
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
    void setUserLastSignal(Context context) throws HttpResponseException;

    static UserController of(String root) {
        return new UserControllerImpl(root);
    }
}
