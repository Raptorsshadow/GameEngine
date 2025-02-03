package component;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 *
 Class: ComponentDeserializer
 Author: rapto
 CreatedDate: 2/3/2025 : 3:30 AM
 Project: GameEngine
 Description: Custom GSON Serializer and Deserializer for Components.

 */
public class ComponentDeserializer implements JsonSerializer<Component>, JsonDeserializer<Component> {
    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");
        try {
            return context.deserialize(element, Class.forName(type));
        } catch(Exception e) {
            throw new JsonParseException("Unknown element type definition: " + type, e);
        }
    }

    @Override
    public JsonElement serialize(Component src, Type type, JsonSerializationContext context) {
        JsonObject res = new JsonObject();
        res.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        res.add("properties", context.serialize(src, src.getClass()));
        return res;
    }
}
