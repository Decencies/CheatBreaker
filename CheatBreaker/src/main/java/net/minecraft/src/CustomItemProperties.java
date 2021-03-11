package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class CustomItemProperties
{
    public String name = null;
    public String basePath = null;
    public int type = 1;
    public int[] items = null;
    public String texture = null;
    public Map mapTextures = null;
    public RangeListInt damage = null;
    public boolean damagePercent = false;
    public int damageMask = 0;
    public RangeListInt stackSize = null;
    public RangeListInt enchantmentIds = null;
    public RangeListInt enchantmentLevels = null;
    public NbtTagValue[] nbtTagValues = null;
    public int blend = 1;
    public int speed = 0;
    public int rotation = 0;
    public int layer = 0;
    public int duration = 1;
    public int weight = 0;
    public IIcon textureIcon = null;
    public Map mapTextureIcons = null;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_ENCHANTMENT = 2;
    public static final int TYPE_ARMOR = 3;

    public CustomItemProperties(Properties props, String path)
    {
        this.name = parseName(path);
        this.basePath = parseBasePath(path);
        this.type = this.parseType(props.getProperty("type"));
        this.items = this.parseItems(props.getProperty("items"), props.getProperty("matchItems"));
        this.texture = parseTexture(props.getProperty("texture"), props.getProperty("tile"), props.getProperty("source"), path, this.basePath);
        this.mapTextures = parseTextures(props, this.basePath);
        String damageStr = props.getProperty("damage");

        if (damageStr != null)
        {
            this.damagePercent = damageStr.contains("%");
            damageStr.replace("%", "");
            this.damage = this.parseRangeListInt(damageStr);
            this.damageMask = this.parseInt(props.getProperty("damageMask"), 0);
        }

        this.stackSize = this.parseRangeListInt(props.getProperty("stackSize"));
        this.enchantmentIds = this.parseRangeListInt(props.getProperty("enchantmentIDs"));
        this.enchantmentLevels = this.parseRangeListInt(props.getProperty("enchantmentLevels"));
        this.nbtTagValues = this.parseNbtTagValues(props);
        this.blend = Blender.parseBlend(props.getProperty("blend"));
        this.speed = this.parseInt(props.getProperty("speed"), 0);
        this.rotation = this.parseInt(props.getProperty("rotation"), 0);
        this.layer = this.parseInt(props.getProperty("layer"), 0);
        this.weight = this.parseInt(props.getProperty("weight"), 0);
        this.duration = this.parseInt(props.getProperty("duration"), 1);
    }

    private static String parseName(String path)
    {
        String str = path;
        int pos = path.lastIndexOf(47);

        if (pos >= 0)
        {
            str = path.substring(pos + 1);
        }

        int pos2 = str.lastIndexOf(46);

        if (pos2 >= 0)
        {
            str = str.substring(0, pos2);
        }

        return str;
    }

    private static String parseBasePath(String path)
    {
        int pos = path.lastIndexOf(47);
        return pos < 0 ? "" : path.substring(0, pos);
    }

    private int parseType(String str)
    {
        if (str == null)
        {
            return 1;
        }
        else if (str.equals("item"))
        {
            return 1;
        }
        else if (str.equals("enchantment"))
        {
            return 2;
        }
        else if (str.equals("armor"))
        {
            return 3;
        }
        else
        {
            Config.warn("Unknown method: " + str);
            return 0;
        }
    }

    private int[] parseItems(String str, String str2)
    {
        if (str == null)
        {
            str = str2;
        }

        if (str == null)
        {
            return null;
        }
        else
        {
            str = str.trim();
            TreeSet setItemIds = new TreeSet();
            String[] tokens = Config.tokenize(str, " ");
            int i;
            label58:

            for (int integers = 0; integers < tokens.length; ++integers)
            {
                String ints = tokens[integers];
                i = Config.parseInt(ints, -1);

                if (i >= 0)
                {
                    setItemIds.add(new Integer(i));
                }
                else
                {
                    int id;

                    if (ints.contains("-"))
                    {
                        String[] itemObj = Config.tokenize(ints, "-");

                        if (itemObj.length == 2)
                        {
                            int item = Config.parseInt(itemObj[0], -1);
                            id = Config.parseInt(itemObj[1], -1);

                            if (item >= 0 && id >= 0)
                            {
                                int min = Math.min(item, id);
                                int max = Math.max(item, id);
                                int x = min;

                                while (true)
                                {
                                    if (x > max)
                                    {
                                        continue label58;
                                    }

                                    setItemIds.add(new Integer(x));
                                    ++x;
                                }
                            }
                        }
                    }

                    Object var16 = Item.itemRegistry.getObject(ints);

                    if (!(var16 instanceof Item))
                    {
                        Config.dbg("Item not found: " + ints);
                    }
                    else
                    {
                        Item var17 = (Item)var16;
                        id = Item.getIdFromItem(var17);

                        if (id < 0)
                        {
                            Config.dbg("Item not found: " + ints);
                        }
                        else
                        {
                            setItemIds.add(new Integer(id));
                        }
                    }
                }
            }

            Integer[] var14 = (Integer[])((Integer[])setItemIds.toArray(new Integer[setItemIds.size()]));
            int[] var15 = new int[var14.length];

            for (i = 0; i < var15.length; ++i)
            {
                var15[i] = var14[i].intValue();
            }

            return var15;
        }
    }

    private static String parseTexture(String texStr, String texStr2, String texStr3, String path, String basePath)
    {
        if (texStr == null)
        {
            texStr = texStr2;
        }

        if (texStr == null)
        {
            texStr = texStr3;
        }

        String str;

        if (texStr != null)
        {
            str = ".png";

            if (texStr.endsWith(str))
            {
                texStr = texStr.substring(0, texStr.length() - str.length());
            }

            texStr = fixTextureName(texStr, basePath);
            return texStr;
        }
        else
        {
            str = path;
            int pos = path.lastIndexOf(47);

            if (pos >= 0)
            {
                str = path.substring(pos + 1);
            }

            int pos2 = str.lastIndexOf(46);

            if (pos2 >= 0)
            {
                str = str.substring(0, pos2);
            }

            str = fixTextureName(str, basePath);
            return str;
        }
    }

    private static Map parseTextures(Properties props, String basePath)
    {
        String prefix = "texture.";
        Map mapProps = getMatchingProperties(props, prefix);

        if (mapProps.size() <= 0)
        {
            return null;
        }
        else
        {
            Set keySet = mapProps.keySet();
            LinkedHashMap mapTex = new LinkedHashMap();
            String key;
            String val;

            for (Iterator it = keySet.iterator(); it.hasNext(); mapTex.put(key, val))
            {
                key = (String)it.next();
                val = (String)mapProps.get(key);
                val = fixTextureName(val, basePath);

                if (key.startsWith(prefix))
                {
                    key = key.substring(prefix.length());
                }
            }

            return mapTex;
        }
    }

    private static String fixTextureName(String iconName, String basePath)
    {
        iconName = TextureUtils.fixResourcePath(iconName, basePath);

        if (!iconName.startsWith(basePath) && !iconName.startsWith("textures/") && !iconName.startsWith("mcpatcher/"))
        {
            iconName = basePath + "/" + iconName;
        }

        if (iconName.endsWith(".png"))
        {
            iconName = iconName.substring(0, iconName.length() - 4);
        }

        String pathBlocks = "textures/blocks/";

        if (iconName.startsWith(pathBlocks))
        {
            iconName = iconName.substring(pathBlocks.length());
        }

        if (iconName.startsWith("/"))
        {
            iconName = iconName.substring(1);
        }

        return iconName;
    }

    private int parseInt(String str, int defVal)
    {
        if (str == null)
        {
            return defVal;
        }
        else
        {
            str = str.trim();
            int val = Config.parseInt(str, Integer.MIN_VALUE);

            if (val == Integer.MIN_VALUE)
            {
                Config.warn("Invalid integer: " + str);
                return defVal;
            }
            else
            {
                return val;
            }
        }
    }

    private RangeListInt parseRangeListInt(String str)
    {
        if (str == null)
        {
            return null;
        }
        else
        {
            String[] tokens = Config.tokenize(str, " ");
            RangeListInt rangeList = new RangeListInt();

            for (int i = 0; i < tokens.length; ++i)
            {
                String token = tokens[i];
                RangeInt range = this.parseRangeInt(token);

                if (range == null)
                {
                    Config.warn("Invalid range list: " + str);
                    return null;
                }

                rangeList.addRange(range);
            }

            return rangeList;
        }
    }

    private RangeInt parseRangeInt(String str)
    {
        if (str == null)
        {
            return null;
        }
        else
        {
            str = str.trim();
            int countMinus = str.length() - str.replace("-", "").length();

            if (countMinus > 1)
            {
                Config.warn("Invalid range: " + str);
                return null;
            }
            else
            {
                String[] tokens = Config.tokenize(str, "- ");
                int[] vals = new int[tokens.length];
                int min;

                for (min = 0; min < tokens.length; ++min)
                {
                    String max = tokens[min];
                    int val = Config.parseInt(max, -1);

                    if (val < 0)
                    {
                        Config.warn("Invalid range: " + str);
                        return null;
                    }

                    vals[min] = val;
                }

                if (vals.length == 1)
                {
                    min = vals[0];

                    if (str.startsWith("-"))
                    {
                        return new RangeInt(-1, min);
                    }
                    else if (str.endsWith("-"))
                    {
                        return new RangeInt(min, -1);
                    }
                    else
                    {
                        return new RangeInt(min, min);
                    }
                }
                else if (vals.length == 2)
                {
                    min = Math.min(vals[0], vals[1]);
                    int var8 = Math.max(vals[0], vals[1]);
                    return new RangeInt(min, var8);
                }
                else
                {
                    Config.warn("Invalid range: " + str);
                    return null;
                }
            }
        }
    }

    private NbtTagValue[] parseNbtTagValues(Properties props)
    {
        Map mapNbt = getMatchingProperties(props, "nbt.");

        if (mapNbt.size() <= 0)
        {
            return null;
        }
        else
        {
            ArrayList listNbts = new ArrayList();
            Set keySet = mapNbt.keySet();
            Iterator nbts = keySet.iterator();

            while (nbts.hasNext())
            {
                String key = (String)nbts.next();
                String val = (String)mapNbt.get(key);
                NbtTagValue nbt = new NbtTagValue(key, val);
                listNbts.add(nbt);
            }

            NbtTagValue[] nbts1 = (NbtTagValue[])((NbtTagValue[])listNbts.toArray(new NbtTagValue[listNbts.size()]));
            return nbts1;
        }
    }

    private static Map getMatchingProperties(Properties props, String keyPrefix)
    {
        LinkedHashMap map = new LinkedHashMap();
        Set keySet = props.keySet();
        Iterator it = keySet.iterator();

        while (it.hasNext())
        {
            String key = (String)it.next();
            String val = props.getProperty(key);

            if (key.startsWith(keyPrefix))
            {
                map.put(key, val);
            }
        }

        return map;
    }

    public boolean isValid(String path)
    {
        if (this.name != null && this.name.length() > 0)
        {
            if (this.basePath == null)
            {
                Config.warn("No base path found: " + path);
                return false;
            }
            else if (this.type == 0)
            {
                Config.warn("No type defined: " + path);
                return false;
            }
            else if ((this.type == 1 || this.type == 3) && this.items == null)
            {
                Config.warn("No items defined: " + path);
                return false;
            }
            else if (this.texture == null && this.mapTextures == null)
            {
                Config.warn("No texture specified: " + path);
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            Config.warn("No name found: " + path);
            return false;
        }
    }

    public void updateIcons(TextureMap textureMap)
    {
        if (this.texture != null)
        {
            this.textureIcon = registerIcon(this.texture, textureMap);
        }

        if (this.mapTextures != null)
        {
            this.mapTextureIcons = new LinkedHashMap();
            Set keySet = this.mapTextures.keySet();
            Iterator it = keySet.iterator();

            while (it.hasNext())
            {
                String key = (String)it.next();
                String val = (String)this.mapTextures.get(key);
                IIcon icon = registerIcon(val, textureMap);
                this.mapTextureIcons.put(key, icon);
            }
        }
    }

    private static IIcon registerIcon(String tileName, TextureMap textureMap)
    {
        if (tileName == null)
        {
            return null;
        }
        else
        {
            String fullName = tileName;

            if (!tileName.contains("/"))
            {
                fullName = "textures/blocks/" + tileName;
            }

            String fileName = fullName + ".png";
            ResourceLocation loc = new ResourceLocation(fileName);
            boolean exists = Config.hasResource(loc);

            if (!exists)
            {
                Config.warn("File not found: " + fileName);
            }

            IIcon icon = textureMap.registerIcon(tileName);
            return icon;
        }
    }
}
