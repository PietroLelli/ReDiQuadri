package rq;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LobbyDeserializer implements JsonDeserializer<Lobby> {

    private String getPropertyAsString(JsonObject object, String name) {
        if (object.has(name)) {
            JsonElement value = object.get(name);
            if (value.isJsonNull()) return null;
            return value.getAsString();
        }
        return null;
    }

    private <T> T getPropertyAs(JsonObject object, String name, Class<T> type, JsonDeserializationContext context) {
        if (object.has(name)) {
            JsonElement value = object.get(name);
            if (value.isJsonNull()) return null;
            return context.deserialize(value, type);
        }
        return null;
    }

    @Override
    public Lobby deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            var object = json.getAsJsonObject();
            var id = getPropertyAsString(object, "id");
            var available = object.get("available").getAsBoolean();
            var users = object.getAsJsonArray("users");
            List<User> usersList = new ArrayList<>(users.size());
            for (var u : users) {
                if (u.isJsonNull()) continue;
                usersList.add(new User(u.getAsString()));
            }

            return new Lobby(id, available, usersList);
        } catch (ClassCastException e) {
            throw new JsonParseException("Invalid lobby: " + json, e);
        }
    }
}
