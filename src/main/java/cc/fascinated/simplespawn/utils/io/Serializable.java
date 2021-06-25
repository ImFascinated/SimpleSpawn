package cc.fascinated.simplespawn.utils.io;

import cc.fascinated.simplespawn.SimpleSpawn;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Project: Gravestones
 * Created by Fascinated#4735 on 15/06/2021
 */
public interface Serializable<T> extends JsonSerializer<T>, JsonDeserializer<T> {

    default JsonElement serialize(T src, Type ignored, JsonSerializationContext ignored_) {
        return this.serialize(src, SimpleSpawn.getGSON());
    }

    default T deserialize(JsonElement json, Type ignored, JsonDeserializationContext ignored_) {
        return this.deserialize((JsonObject) json, SimpleSpawn.getGSON());
    }

    JsonElement serialize(T object, Gson gson);

    T deserialize(JsonObject data, Gson gson);
}
