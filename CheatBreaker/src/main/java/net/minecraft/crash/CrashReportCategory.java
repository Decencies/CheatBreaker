package net.minecraft.crash;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;

public class CrashReportCategory
{
    private final CrashReport theCrashReport;
    private final String field_85076_b;
    private final List field_85077_c = new ArrayList();
    private StackTraceElement[] stackTrace = new StackTraceElement[0];


    public CrashReportCategory(CrashReport p_i1353_1_, String p_i1353_2_)
    {
        this.theCrashReport = p_i1353_1_;
        this.field_85076_b = p_i1353_2_;
    }

    public static String func_85074_a(double p_85074_0_, double p_85074_2_, double p_85074_4_)
    {
        return String.format("%.2f,%.2f,%.2f - %s", new Object[] {Double.valueOf(p_85074_0_), Double.valueOf(p_85074_2_), Double.valueOf(p_85074_4_), getLocationInfo(MathHelper.floor_double(p_85074_0_), MathHelper.floor_double(p_85074_2_), MathHelper.floor_double(p_85074_4_))});
    }

    /**
     * Returns a string with world information on location.Args:x,y,z
     */
    public static String getLocationInfo(int p_85071_0_, int p_85071_1_, int p_85071_2_)
    {
        StringBuilder var3 = new StringBuilder();

        try
        {
            var3.append(String.format("World: (%d,%d,%d)", new Object[] {Integer.valueOf(p_85071_0_), Integer.valueOf(p_85071_1_), Integer.valueOf(p_85071_2_)}));
        }
        catch (Throwable var16)
        {
            var3.append("(Error finding world loc)");
        }

        var3.append(", ");
        int var4;
        int var5;
        int var6;
        int var7;
        int var8;
        int var9;
        int var10;
        int var11;
        int var12;

        try
        {
            var4 = p_85071_0_ >> 4;
            var5 = p_85071_2_ >> 4;
            var6 = p_85071_0_ & 15;
            var7 = p_85071_1_ >> 4;
            var8 = p_85071_2_ & 15;
            var9 = var4 << 4;
            var10 = var5 << 4;
            var11 = (var4 + 1 << 4) - 1;
            var12 = (var5 + 1 << 4) - 1;
            var3.append(String.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)", new Object[] {Integer.valueOf(var6), Integer.valueOf(var7), Integer.valueOf(var8), Integer.valueOf(var4), Integer.valueOf(var5), Integer.valueOf(var9), Integer.valueOf(var10), Integer.valueOf(var11), Integer.valueOf(var12)}));
        }
        catch (Throwable var15)
        {
            var3.append("(Error finding chunk loc)");
        }

        var3.append(", ");

        try
        {
            var4 = p_85071_0_ >> 9;
            var5 = p_85071_2_ >> 9;
            var6 = var4 << 5;
            var7 = var5 << 5;
            var8 = (var4 + 1 << 5) - 1;
            var9 = (var5 + 1 << 5) - 1;
            var10 = var4 << 9;
            var11 = var5 << 9;
            var12 = (var4 + 1 << 9) - 1;
            int var13 = (var5 + 1 << 9) - 1;
            var3.append(String.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)", new Object[] {Integer.valueOf(var4), Integer.valueOf(var5), Integer.valueOf(var6), Integer.valueOf(var7), Integer.valueOf(var8), Integer.valueOf(var9), Integer.valueOf(var10), Integer.valueOf(var11), Integer.valueOf(var12), Integer.valueOf(var13)}));
        }
        catch (Throwable var14)
        {
            var3.append("(Error finding world loc)");
        }

        return var3.toString();
    }

    /**
     * Adds a Crashreport section with the given name with the value set to the result of the given Callable;
     */
    public void addCrashSectionCallable(String p_71500_1_, Callable p_71500_2_)
    {
        try
        {
            this.addCrashSection(p_71500_1_, p_71500_2_.call());
        }
        catch (Throwable var4)
        {
            this.addCrashSectionThrowable(p_71500_1_, var4);
        }
    }

    /**
     * Adds a Crashreport section with the given name with the given value (convered .toString())
     */
    public void addCrashSection(String p_71507_1_, Object p_71507_2_)
    {
        this.field_85077_c.add(new CrashReportCategory.Entry(p_71507_1_, p_71507_2_));
    }

    /**
     * Adds a Crashreport section with the given name with the given Throwable
     */
    public void addCrashSectionThrowable(String p_71499_1_, Throwable p_71499_2_)
    {
        this.addCrashSection(p_71499_1_, p_71499_2_);
    }

    /**
     * Resets our stack trace according to the current trace, pruning the deepest 3 entries.  The parameter indicates
     * how many additional deepest entries to prune.  Returns the number of entries in the resulting pruned stack trace.
     */
    public int getPrunedStackTrace(int p_85073_1_)
    {
        StackTraceElement[] var2 = Thread.currentThread().getStackTrace();

        if (var2.length <= 0)
        {
            return 0;
        }
        else
        {
            this.stackTrace = new StackTraceElement[var2.length - 3 - p_85073_1_];
            System.arraycopy(var2, 3 + p_85073_1_, this.stackTrace, 0, this.stackTrace.length);
            return this.stackTrace.length;
        }
    }

    /**
     * Do the deepest two elements of our saved stack trace match the given elements, in order from the deepest?
     */
    public boolean firstTwoElementsOfStackTraceMatch(StackTraceElement p_85069_1_, StackTraceElement p_85069_2_)
    {
        if (this.stackTrace.length != 0 && p_85069_1_ != null)
        {
            StackTraceElement var3 = this.stackTrace[0];

            if (var3.isNativeMethod() == p_85069_1_.isNativeMethod() && var3.getClassName().equals(p_85069_1_.getClassName()) && var3.getFileName().equals(p_85069_1_.getFileName()) && var3.getMethodName().equals(p_85069_1_.getMethodName()))
            {
                if (p_85069_2_ != null != this.stackTrace.length > 1)
                {
                    return false;
                }
                else if (p_85069_2_ != null && !this.stackTrace[1].equals(p_85069_2_))
                {
                    return false;
                }
                else
                {
                    this.stackTrace[0] = p_85069_1_;
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Removes the given number entries from the bottom of the stack trace.
     */
    public void trimStackTraceEntriesFromBottom(int p_85070_1_)
    {
        StackTraceElement[] var2 = new StackTraceElement[this.stackTrace.length - p_85070_1_];
        System.arraycopy(this.stackTrace, 0, var2, 0, var2.length);
        this.stackTrace = var2;
    }

    public void appendToStringBuilder(StringBuilder p_85072_1_)
    {
        p_85072_1_.append("-- ").append(this.field_85076_b).append(" --\n");
        p_85072_1_.append("Details:");
        Iterator var2 = this.field_85077_c.iterator();

        while (var2.hasNext())
        {
            CrashReportCategory.Entry var3 = (CrashReportCategory.Entry)var2.next();
            p_85072_1_.append("\n\t");
            p_85072_1_.append(var3.func_85089_a());
            p_85072_1_.append(": ");
            p_85072_1_.append(var3.func_85090_b());
        }

        if (this.stackTrace != null && this.stackTrace.length > 0)
        {
            p_85072_1_.append("\nStacktrace:");
            StackTraceElement[] var6 = this.stackTrace;
            int var7 = var6.length;

            for (int var4 = 0; var4 < var7; ++var4)
            {
                StackTraceElement var5 = var6[var4];
                p_85072_1_.append("\n\tat ");
                p_85072_1_.append(var5.toString());
            }
        }
    }

    public StackTraceElement[] func_147152_a()
    {
        return this.stackTrace;
    }

    public static void func_147153_a(CrashReportCategory p_147153_0_, final int p_147153_1_, final int p_147153_2_, final int p_147153_3_, final Block p_147153_4_, final int p_147153_5_)
    {
        final int var6 = Block.getIdFromBlock(p_147153_4_);
        p_147153_0_.addCrashSectionCallable("Block type", new Callable()
        {

            public String call()
            {
                try
                {
                    return String.format("ID #%d (%s // %s)", new Object[] {Integer.valueOf(var6), p_147153_4_.getUnlocalizedName(), p_147153_4_.getClass().getCanonicalName()});
                }
                catch (Throwable var2)
                {
                    return "ID #" + var6;
                }
            }
        });
        p_147153_0_.addCrashSectionCallable("Block data value", new Callable()
        {

            public String call()
            {
                if (p_147153_5_ < 0)
                {
                    return "Unknown? (Got " + p_147153_5_ + ")";
                }
                else
                {
                    String var1 = String.format("%4s", new Object[] {Integer.toBinaryString(p_147153_5_)}).replace(" ", "0");
                    return String.format("%1$d / 0x%1$X / 0b%2$s", new Object[] {Integer.valueOf(p_147153_5_), var1});
                }
            }
        });
        p_147153_0_.addCrashSectionCallable("Block location", new Callable()
        {

            public String call()
            {
                return CrashReportCategory.getLocationInfo(p_147153_1_, p_147153_2_, p_147153_3_);
            }
        });
    }

    static class Entry
    {
        private final String field_85092_a;
        private final String field_85091_b;


        public Entry(String p_i1352_1_, Object p_i1352_2_)
        {
            this.field_85092_a = p_i1352_1_;

            if (p_i1352_2_ == null)
            {
                this.field_85091_b = "~~NULL~~";
            }
            else if (p_i1352_2_ instanceof Throwable)
            {
                Throwable var3 = (Throwable)p_i1352_2_;
                this.field_85091_b = "~~ERROR~~ " + var3.getClass().getSimpleName() + ": " + var3.getMessage();
            }
            else
            {
                this.field_85091_b = p_i1352_2_.toString();
            }
        }

        public String func_85089_a()
        {
            return this.field_85092_a;
        }

        public String func_85090_b()
        {
            return this.field_85091_b;
        }
    }
}
