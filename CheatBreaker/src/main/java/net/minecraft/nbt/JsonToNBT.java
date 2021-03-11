package net.minecraft.nbt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JsonToNBT
{
    private static final Logger logger = LogManager.getLogger();


    public static NBTBase func_150315_a(String p_150315_0_) throws NBTException
    {
        p_150315_0_ = p_150315_0_.trim();
        int var1 = func_150310_b(p_150315_0_);

        if (var1 != 1)
        {
            throw new NBTException("Encountered multiple top tags, only one expected");
        }
        else
        {
            JsonToNBT.Any var2 = null;

            if (p_150315_0_.startsWith("{"))
            {
                var2 = func_150316_a("tag", p_150315_0_);
            }
            else
            {
                var2 = func_150316_a(func_150313_b(p_150315_0_, false), func_150311_c(p_150315_0_, false));
            }

            return var2.func_150489_a();
        }
    }

    static int func_150310_b(String p_150310_0_) throws NBTException
    {
        int var1 = 0;
        boolean var2 = false;
        Stack var3 = new Stack();

        for (int var4 = 0; var4 < p_150310_0_.length(); ++var4)
        {
            char var5 = p_150310_0_.charAt(var4);

            if (var5 == 34)
            {
                if (var4 > 0 && p_150310_0_.charAt(var4 - 1) == 92)
                {
                    if (!var2)
                    {
                        throw new NBTException("Illegal use of \\\": " + p_150310_0_);
                    }
                }
                else
                {
                    var2 = !var2;
                }
            }
            else if (!var2)
            {
                if (var5 != 123 && var5 != 91)
                {
                    if (var5 == 125 && (var3.isEmpty() || ((Character)var3.pop()).charValue() != 123))
                    {
                        throw new NBTException("Unbalanced curly brackets {}: " + p_150310_0_);
                    }

                    if (var5 == 93 && (var3.isEmpty() || ((Character)var3.pop()).charValue() != 91))
                    {
                        throw new NBTException("Unbalanced square brackets []: " + p_150310_0_);
                    }
                }
                else
                {
                    if (var3.isEmpty())
                    {
                        ++var1;
                    }

                    var3.push(Character.valueOf(var5));
                }
            }
        }

        if (var2)
        {
            throw new NBTException("Unbalanced quotation: " + p_150310_0_);
        }
        else if (!var3.isEmpty())
        {
            throw new NBTException("Unbalanced brackets: " + p_150310_0_);
        }
        else if (var1 == 0 && !p_150310_0_.isEmpty())
        {
            return 1;
        }
        else
        {
            return var1;
        }
    }

    static JsonToNBT.Any func_150316_a(String p_150316_0_, String p_150316_1_) throws NBTException
    {
        p_150316_1_ = p_150316_1_.trim();
        func_150310_b(p_150316_1_);
        String var3;
        String var4;
        String var5;
        char var6;

        if (p_150316_1_.startsWith("{"))
        {
            if (!p_150316_1_.endsWith("}"))
            {
                throw new NBTException("Unable to locate ending bracket for: " + p_150316_1_);
            }
            else
            {
                p_150316_1_ = p_150316_1_.substring(1, p_150316_1_.length() - 1);
                JsonToNBT.Compound var7 = new JsonToNBT.Compound(p_150316_0_);

                while (p_150316_1_.length() > 0)
                {
                    var3 = func_150314_a(p_150316_1_, false);

                    if (var3.length() > 0)
                    {
                        var4 = func_150313_b(var3, false);
                        var5 = func_150311_c(var3, false);
                        var7.field_150491_b.add(func_150316_a(var4, var5));

                        if (p_150316_1_.length() < var3.length() + 1)
                        {
                            break;
                        }

                        var6 = p_150316_1_.charAt(var3.length());

                        if (var6 != 44 && var6 != 123 && var6 != 125 && var6 != 91 && var6 != 93)
                        {
                            throw new NBTException("Unexpected token \'" + var6 + "\' at: " + p_150316_1_.substring(var3.length()));
                        }

                        p_150316_1_ = p_150316_1_.substring(var3.length() + 1);
                    }
                }

                return var7;
            }
        }
        else if (p_150316_1_.startsWith("[") && !p_150316_1_.matches("\\[[-\\d|,\\s]+\\]"))
        {
            if (!p_150316_1_.endsWith("]"))
            {
                throw new NBTException("Unable to locate ending bracket for: " + p_150316_1_);
            }
            else
            {
                p_150316_1_ = p_150316_1_.substring(1, p_150316_1_.length() - 1);
                JsonToNBT.List var2 = new JsonToNBT.List(p_150316_0_);

                while (p_150316_1_.length() > 0)
                {
                    var3 = func_150314_a(p_150316_1_, true);

                    if (var3.length() > 0)
                    {
                        var4 = func_150313_b(var3, true);
                        var5 = func_150311_c(var3, true);
                        var2.field_150492_b.add(func_150316_a(var4, var5));

                        if (p_150316_1_.length() < var3.length() + 1)
                        {
                            break;
                        }

                        var6 = p_150316_1_.charAt(var3.length());

                        if (var6 != 44 && var6 != 123 && var6 != 125 && var6 != 91 && var6 != 93)
                        {
                            throw new NBTException("Unexpected token \'" + var6 + "\' at: " + p_150316_1_.substring(var3.length()));
                        }

                        p_150316_1_ = p_150316_1_.substring(var3.length() + 1);
                    }
                    else
                    {
                        logger.debug(p_150316_1_);
                    }
                }

                return var2;
            }
        }
        else
        {
            return new JsonToNBT.Primitive(p_150316_0_, p_150316_1_);
        }
    }

    private static String func_150314_a(String p_150314_0_, boolean p_150314_1_) throws NBTException
    {
        int var2 = func_150312_a(p_150314_0_, ':');

        if (var2 < 0 && !p_150314_1_)
        {
            throw new NBTException("Unable to locate name/value separator for string: " + p_150314_0_);
        }
        else
        {
            int var3 = func_150312_a(p_150314_0_, ',');

            if (var3 >= 0 && var3 < var2 && !p_150314_1_)
            {
                throw new NBTException("Name error at: " + p_150314_0_);
            }
            else
            {
                if (p_150314_1_ && (var2 < 0 || var2 > var3))
                {
                    var2 = -1;
                }

                Stack var4 = new Stack();
                int var5 = var2 + 1;
                boolean var6 = false;
                boolean var7 = false;
                boolean var8 = false;

                for (int var9 = 0; var5 < p_150314_0_.length(); ++var5)
                {
                    char var10 = p_150314_0_.charAt(var5);

                    if (var10 == 34)
                    {
                        if (var5 > 0 && p_150314_0_.charAt(var5 - 1) == 92)
                        {
                            if (!var6)
                            {
                                throw new NBTException("Illegal use of \\\": " + p_150314_0_);
                            }
                        }
                        else
                        {
                            var6 = !var6;

                            if (var6 && !var8)
                            {
                                var7 = true;
                            }

                            if (!var6)
                            {
                                var9 = var5;
                            }
                        }
                    }
                    else if (!var6)
                    {
                        if (var10 != 123 && var10 != 91)
                        {
                            if (var10 == 125 && (var4.isEmpty() || ((Character)var4.pop()).charValue() != 123))
                            {
                                throw new NBTException("Unbalanced curly brackets {}: " + p_150314_0_);
                            }

                            if (var10 == 93 && (var4.isEmpty() || ((Character)var4.pop()).charValue() != 91))
                            {
                                throw new NBTException("Unbalanced square brackets []: " + p_150314_0_);
                            }

                            if (var10 == 44 && var4.isEmpty())
                            {
                                return p_150314_0_.substring(0, var5);
                            }
                        }
                        else
                        {
                            var4.push(Character.valueOf(var10));
                        }
                    }

                    if (!Character.isWhitespace(var10))
                    {
                        if (!var6 && var7 && var9 != var5)
                        {
                            return p_150314_0_.substring(0, var9 + 1);
                        }

                        var8 = true;
                    }
                }

                return p_150314_0_.substring(0, var5);
            }
        }
    }

    private static String func_150313_b(String p_150313_0_, boolean p_150313_1_) throws NBTException
    {
        if (p_150313_1_)
        {
            p_150313_0_ = p_150313_0_.trim();

            if (p_150313_0_.startsWith("{") || p_150313_0_.startsWith("["))
            {
                return "";
            }
        }

        int var2 = p_150313_0_.indexOf(58);

        if (var2 < 0)
        {
            if (p_150313_1_)
            {
                return "";
            }
            else
            {
                throw new NBTException("Unable to locate name/value separator for string: " + p_150313_0_);
            }
        }
        else
        {
            return p_150313_0_.substring(0, var2).trim();
        }
    }

    private static String func_150311_c(String p_150311_0_, boolean p_150311_1_) throws NBTException
    {
        if (p_150311_1_)
        {
            p_150311_0_ = p_150311_0_.trim();

            if (p_150311_0_.startsWith("{") || p_150311_0_.startsWith("["))
            {
                return p_150311_0_;
            }
        }

        int var2 = p_150311_0_.indexOf(58);

        if (var2 < 0)
        {
            if (p_150311_1_)
            {
                return p_150311_0_;
            }
            else
            {
                throw new NBTException("Unable to locate name/value separator for string: " + p_150311_0_);
            }
        }
        else
        {
            return p_150311_0_.substring(var2 + 1).trim();
        }
    }

    private static int func_150312_a(String p_150312_0_, char p_150312_1_)
    {
        int var2 = 0;

        for (boolean var3 = false; var2 < p_150312_0_.length(); ++var2)
        {
            char var4 = p_150312_0_.charAt(var2);

            if (var4 == 34)
            {
                if (var2 <= 0 || p_150312_0_.charAt(var2 - 1) != 92)
                {
                    var3 = !var3;
                }
            }
            else if (!var3)
            {
                if (var4 == p_150312_1_)
                {
                    return var2;
                }

                if (var4 == 123 || var4 == 91)
                {
                    return -1;
                }
            }
        }

        return -1;
    }

    abstract static class Any
    {
        protected String field_150490_a;


        public abstract NBTBase func_150489_a();
    }

    static class Compound extends JsonToNBT.Any
    {
        protected ArrayList field_150491_b = new ArrayList();


        public Compound(String p_i45137_1_)
        {
            this.field_150490_a = p_i45137_1_;
        }

        public NBTBase func_150489_a()
        {
            NBTTagCompound var1 = new NBTTagCompound();
            Iterator var2 = this.field_150491_b.iterator();

            while (var2.hasNext())
            {
                JsonToNBT.Any var3 = (JsonToNBT.Any)var2.next();
                var1.setTag(var3.field_150490_a, var3.func_150489_a());
            }

            return var1;
        }
    }

    static class List extends JsonToNBT.Any
    {
        protected ArrayList field_150492_b = new ArrayList();


        public List(String p_i45138_1_)
        {
            this.field_150490_a = p_i45138_1_;
        }

        public NBTBase func_150489_a()
        {
            NBTTagList var1 = new NBTTagList();
            Iterator var2 = this.field_150492_b.iterator();

            while (var2.hasNext())
            {
                JsonToNBT.Any var3 = (JsonToNBT.Any)var2.next();
                var1.appendTag(var3.func_150489_a());
            }

            return var1;
        }
    }

    static class Primitive extends JsonToNBT.Any
    {
        protected String field_150493_b;


        public Primitive(String p_i45139_1_, String p_i45139_2_)
        {
            this.field_150490_a = p_i45139_1_;
            this.field_150493_b = p_i45139_2_;
        }

        public NBTBase func_150489_a()
        {
            try
            {
                if (this.field_150493_b.matches("[-+]?[0-9]*\\.?[0-9]+[d|D]"))
                {
                    return new NBTTagDouble(Double.parseDouble(this.field_150493_b.substring(0, this.field_150493_b.length() - 1)));
                }
                else if (this.field_150493_b.matches("[-+]?[0-9]*\\.?[0-9]+[f|F]"))
                {
                    return new NBTTagFloat(Float.parseFloat(this.field_150493_b.substring(0, this.field_150493_b.length() - 1)));
                }
                else if (this.field_150493_b.matches("[-+]?[0-9]+[b|B]"))
                {
                    return new NBTTagByte(Byte.parseByte(this.field_150493_b.substring(0, this.field_150493_b.length() - 1)));
                }
                else if (this.field_150493_b.matches("[-+]?[0-9]+[l|L]"))
                {
                    return new NBTTagLong(Long.parseLong(this.field_150493_b.substring(0, this.field_150493_b.length() - 1)));
                }
                else if (this.field_150493_b.matches("[-+]?[0-9]+[s|S]"))
                {
                    return new NBTTagShort(Short.parseShort(this.field_150493_b.substring(0, this.field_150493_b.length() - 1)));
                }
                else if (this.field_150493_b.matches("[-+]?[0-9]+"))
                {
                    return new NBTTagInt(Integer.parseInt(this.field_150493_b.substring(0, this.field_150493_b.length())));
                }
                else if (this.field_150493_b.matches("[-+]?[0-9]*\\.?[0-9]+"))
                {
                    return new NBTTagDouble(Double.parseDouble(this.field_150493_b.substring(0, this.field_150493_b.length())));
                }
                else if (!this.field_150493_b.equalsIgnoreCase("true") && !this.field_150493_b.equalsIgnoreCase("false"))
                {
                    if (this.field_150493_b.startsWith("[") && this.field_150493_b.endsWith("]"))
                    {
                        if (this.field_150493_b.length() > 2)
                        {
                            String var1 = this.field_150493_b.substring(1, this.field_150493_b.length() - 1);
                            String[] var2 = var1.split(",");

                            try
                            {
                                if (var2.length <= 1)
                                {
                                    return new NBTTagIntArray(new int[] {Integer.parseInt(var1.trim())});
                                }
                                else
                                {
                                    int[] var3 = new int[var2.length];

                                    for (int var4 = 0; var4 < var2.length; ++var4)
                                    {
                                        var3[var4] = Integer.parseInt(var2[var4].trim());
                                    }

                                    return new NBTTagIntArray(var3);
                                }
                            }
                            catch (NumberFormatException var5)
                            {
                                return new NBTTagString(this.field_150493_b);
                            }
                        }
                        else
                        {
                            return new NBTTagIntArray();
                        }
                    }
                    else
                    {
                        if (this.field_150493_b.startsWith("\"") && this.field_150493_b.endsWith("\"") && this.field_150493_b.length() > 2)
                        {
                            this.field_150493_b = this.field_150493_b.substring(1, this.field_150493_b.length() - 1);
                        }

                        this.field_150493_b = this.field_150493_b.replaceAll("\\\\\"", "\"");
                        return new NBTTagString(this.field_150493_b);
                    }
                }
                else
                {
                    return new NBTTagByte((byte)(Boolean.parseBoolean(this.field_150493_b) ? 1 : 0));
                }
            }
            catch (NumberFormatException var6)
            {
                this.field_150493_b = this.field_150493_b.replaceAll("\\\\\"", "\"");
                return new NBTTagString(this.field_150493_b);
            }
        }
    }
}
