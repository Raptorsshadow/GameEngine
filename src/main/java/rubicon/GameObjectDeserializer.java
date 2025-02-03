package rubicon;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 *
 Class: GameObjectDeserializer
 Author: rapto
 CreatedDate: 2/3/2025 : 3:31 AM
 Project: GameEngine
 Description: Custom GSON Deserializer for Game Objects.

 */
public class GameObjectDeserializer implements JsonDeserializer<GameObject> {
    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("components");
        Transform transform = context.deserialize(jsonObject.get("transform"), Transform.class);
        int zIndex = context.deserialize(jsonObject.get("zIndex"), int.class);

        GameObject g = new GameObject(name, transform, zIndex);
        components.forEach(e -> g.addComponent(context.deserialize(e, Component.class)));
        return g;
    }
}
