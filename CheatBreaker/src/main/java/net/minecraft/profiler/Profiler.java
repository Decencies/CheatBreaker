package net.minecraft.profiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.src.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Profiler
{
    private static final Logger logger = LogManager.getLogger();

    /** List of parent sections */
    private final List sectionList = new ArrayList();

    /** List of timestamps (System.nanoTime) */
    private final List timestampList = new ArrayList();

    /** Flag profiling enabled */
    public boolean profilingEnabled;

    /** Current profiling section */
    private String profilingSection = "";

    /** Profiling map */
    private final Map profilingMap = new HashMap();
    
    public boolean profilerGlobalEnabled = true;
    private boolean profilerLocalEnabled;
    private long startTickNano;
    public long timeTickNano;
    private long startUpdateChunksNano;
    public long timeUpdateChunksNano;

    public Profiler()
    {
        this.profilerLocalEnabled = this.profilerGlobalEnabled;
        this.startTickNano = 0L;
        this.timeTickNano = 0L;
        this.startUpdateChunksNano = 0L;
        this.timeUpdateChunksNano = 0L;
    }

    /**
     * Clear profiling.
     */
    public void clearProfiling()
    {
        this.profilingMap.clear();
        this.profilingSection = "";
        this.sectionList.clear();
        this.profilerLocalEnabled = this.profilerGlobalEnabled;
    }

    /**
     * Start section
     */
    public void startSection(String par1Str)
    {
        if (Config.getGameSettings().showDebugInfo)
        {
            if (this.startTickNano == 0L && par1Str.equals("tick"))
            {
                this.startTickNano = System.nanoTime();
            }

            if (this.startTickNano != 0L && par1Str.equals("preRenderErrors"))
            {
                this.timeTickNano = System.nanoTime() - this.startTickNano;
                this.startTickNano = 0L;
            }

            if (this.startUpdateChunksNano == 0L && par1Str.equals("updatechunks"))
            {
                this.startUpdateChunksNano = System.nanoTime();
            }

            if (this.startUpdateChunksNano != 0L && par1Str.equals("terrain"))
            {
                this.timeUpdateChunksNano = System.nanoTime() - this.startUpdateChunksNano;
                this.startUpdateChunksNano = 0L;
            }
        }

        if (this.profilerLocalEnabled)
        {
            if (this.profilingEnabled)
            {
                if (this.profilingSection.length() > 0)
                {
                    this.profilingSection = this.profilingSection + ".";
                }

                this.profilingSection = this.profilingSection + par1Str;
                this.sectionList.add(this.profilingSection);
                this.timestampList.add(Long.valueOf(System.nanoTime()));
            }
        }
    }

    /**
     * End section
     */
    public void endSection()
    {
        if (this.profilerLocalEnabled)
        {
            if (this.profilingEnabled)
            {
                long var1 = System.nanoTime();
                long var3 = ((Long)this.timestampList.remove(this.timestampList.size() - 1)).longValue();
                this.sectionList.remove(this.sectionList.size() - 1);
                long var5 = var1 - var3;

                if (this.profilingMap.containsKey(this.profilingSection))
                {
                    this.profilingMap.put(this.profilingSection, Long.valueOf(((Long)this.profilingMap.get(this.profilingSection)).longValue() + var5));
                }
                else
                {
                    this.profilingMap.put(this.profilingSection, Long.valueOf(var5));
                }

                if (var5 > 100000000L)
                {
                    logger.warn("Something\'s taking too long! \'" + this.profilingSection + "\' took aprox " + (double)var5 / 1000000.0D + " ms");
                }

                this.profilingSection = !this.sectionList.isEmpty() ? (String)this.sectionList.get(this.sectionList.size() - 1) : "";
            }
        }
    }

    /**
     * Get profiling data
     */
    public List getProfilingData(String par1Str)
    {
        this.profilerLocalEnabled = this.profilerGlobalEnabled;

        if (!this.profilerLocalEnabled)
        {
            return new ArrayList(Arrays.asList(new Profiler.Result[] {new Profiler.Result("root", 0.0D, 0.0D)}));
        }
        else if (!this.profilingEnabled)
        {
            return null;
        }
        else
        {
            long var3 = this.profilingMap.containsKey("root") ? ((Long)this.profilingMap.get("root")).longValue() : 0L;
            long var5 = this.profilingMap.containsKey(par1Str) ? ((Long)this.profilingMap.get(par1Str)).longValue() : -1L;
            ArrayList var7 = new ArrayList();

            if (par1Str.length() > 0)
            {
                par1Str = par1Str + ".";
            }

            long var8 = 0L;
            Iterator var10 = this.profilingMap.keySet().iterator();

            while (var10.hasNext())
            {
                String var20 = (String)var10.next();

                if (var20.length() > par1Str.length() && var20.startsWith(par1Str) && var20.indexOf(".", par1Str.length() + 1) < 0)
                {
                    var8 += ((Long)this.profilingMap.get(var20)).longValue();
                }
            }

            float var201 = (float)var8;

            if (var8 < var5)
            {
                var8 = var5;
            }

            if (var3 < var8)
            {
                var3 = var8;
            }

            Iterator var21 = this.profilingMap.keySet().iterator();
            String var12;

            while (var21.hasNext())
            {
                var12 = (String)var21.next();

                if (var12.length() > par1Str.length() && var12.startsWith(par1Str) && var12.indexOf(".", par1Str.length() + 1) < 0)
                {
                    long var13 = ((Long)this.profilingMap.get(var12)).longValue();
                    double var15 = (double)var13 * 100.0D / (double)var8;
                    double var17 = (double)var13 * 100.0D / (double)var3;
                    String var19 = var12.substring(par1Str.length());
                    var7.add(new Profiler.Result(var19, var15, var17));
                }
            }

            var21 = this.profilingMap.keySet().iterator();

            while (var21.hasNext())
            {
                var12 = (String)var21.next();
                this.profilingMap.put(var12, Long.valueOf(((Long)this.profilingMap.get(var12)).longValue() * 999L / 1000L));
            }

            if ((float)var8 > var201)
            {
                var7.add(new Profiler.Result("unspecified", (double)((float)var8 - var201) * 100.0D / (double)var8, (double)((float)var8 - var201) * 100.0D / (double)var3));
            }

            Collections.sort(var7);
            var7.add(0, new Profiler.Result(par1Str, 100.0D, (double)var8 * 100.0D / (double)var3));
            return var7;
        }
    }

    /**
     * End current section and start a new section
     */
    public void endStartSection(String par1Str)
    {
        if (this.profilerLocalEnabled)
        {
            this.endSection();
            this.startSection(par1Str);
        }
    }

    public String getNameOfLastSection()
    {
        return this.sectionList.size() == 0 ? "[UNKNOWN]" : (String)this.sectionList.get(this.sectionList.size() - 1);
    }

    public static final class Result implements Comparable
    {
        public double field_76332_a;
        public double field_76330_b;
        public String field_76331_c;
        

        public Result(String par1Str, double par2, double par4)
        {
            this.field_76331_c = par1Str;
            this.field_76332_a = par2;
            this.field_76330_b = par4;
        }

        public int compareTo(Profiler.Result par1Obj)
        {
            return par1Obj.field_76332_a < this.field_76332_a ? -1 : (par1Obj.field_76332_a > this.field_76332_a ? 1 : par1Obj.field_76331_c.compareTo(this.field_76331_c));
        }

        public int func_76329_a()
        {
            return (this.field_76331_c.hashCode() & 11184810) + 4473924;
        }

        public int compareTo(Object par1Obj)
        {
            return this.compareTo((Profiler.Result)par1Obj);
        }
    }
}
