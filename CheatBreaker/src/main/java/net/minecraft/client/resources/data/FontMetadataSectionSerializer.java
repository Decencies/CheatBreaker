package net.minecraft.client.resources.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.util.JsonUtils;
import org.apache.commons.lang3.Validate;

public class FontMetadataSectionSerializer extends BaseMetadataSectionSerializer
{


    public FontMetadataSection deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
    {
        JsonObject var4 = p_deserialize_1_.getAsJsonObject();
        float[] var5 = new float[256];
        float[] var6 = new float[256];
        float[] var7 = new float[256];
        float var8 = 1.0F;
        float var9 = 0.0F;
        float var10 = 0.0F;

        if (var4.has("characters"))
        {
            if (!var4.get("characters").isJsonObject())
            {
                throw new JsonParseException("Invalid font->characters: expected object, was " + var4.get("characters"));
            }

            JsonObject var11 = var4.getAsJsonObject("characters");

            if (var11.has("default"))
            {
                if (!var11.get("default").isJsonObject())
                {
                    throw new JsonParseException("Invalid font->characters->default: expected object, was " + var11.get("default"));
                }

                JsonObject var12 = var11.getAsJsonObject("default");
                var8 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var12, "width", var8);
                Validate.inclusiveBetween(Float.valueOf(0.0F), Float.valueOf(Float.MAX_VALUE), Float.valueOf(var8), "Invalid default width", new Object[0]);
                var9 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var12, "spacing", var9);
                Validate.inclusiveBetween(Float.valueOf(0.0F), Float.valueOf(Float.MAX_VALUE), Float.valueOf(var9), "Invalid default spacing", new Object[0]);
                var10 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var12, "left", var9);
                Validate.inclusiveBetween(Float.valueOf(0.0F), Float.valueOf(Float.MAX_VALUE), Float.valueOf(var10), "Invalid default left", new Object[0]);
            }

            for (int var18 = 0; var18 < 256; ++var18)
            {
                JsonElement var13 = var11.get(Integer.toString(var18));
                float var14 = var8;
                float var15 = var9;
                float var16 = var10;

                if (var13 != null)
                {
                    JsonObject var17 = JsonUtils.getJsonElementAsJsonObject(var13, "characters[" + var18 + "]");
                    var14 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var17, "width", var8);
                    Validate.inclusiveBetween(Float.valueOf(0.0F), Float.valueOf(Float.MAX_VALUE), Float.valueOf(var14), "Invalid width", new Object[0]);
                    var15 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var17, "spacing", var9);
                    Validate.inclusiveBetween(Float.valueOf(0.0F), Float.valueOf(Float.MAX_VALUE), Float.valueOf(var15), "Invalid spacing", new Object[0]);
                    var16 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var17, "left", var10);
                    Validate.inclusiveBetween(Float.valueOf(0.0F), Float.valueOf(Float.MAX_VALUE), Float.valueOf(var16), "Invalid left", new Object[0]);
                }

                var5[var18] = var14;
                var6[var18] = var15;
                var7[var18] = var16;
            }
        }

        return new FontMetadataSection(var5, var7, var6);
    }

    /**
     * The name of this section type as it appears in JSON.
     */
    public String getSectionName()
    {
        return "font";
    }
}
