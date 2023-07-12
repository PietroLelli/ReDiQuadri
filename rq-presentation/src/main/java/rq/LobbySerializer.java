package rq;

import com.google.gson.*;

import java.lang.reflect.Type;

public class LobbySerializer implements JsonSerializer<Lobby> {

    @Override
    public JsonElement serialize(Lobby src, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();
        object.addProperty("id", src.getId());
        object.addProperty("available", src.getAvailable());
        var users = new JsonArray();
        for (var u : src.getUsers()) {
            users.add(new JsonPrimitive(u.getNickname()));
        }
        object.add("users", users);
        return object;
    }
}
