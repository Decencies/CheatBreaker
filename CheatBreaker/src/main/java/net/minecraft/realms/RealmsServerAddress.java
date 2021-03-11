package net.minecraft.realms;

import java.util.Hashtable;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

public class RealmsServerAddress
{
    private final String host;
    private final int port;


    protected RealmsServerAddress(String p_i1121_1_, int p_i1121_2_)
    {
        this.host = p_i1121_1_;
        this.port = p_i1121_2_;
    }

    public String getHost()
    {
        return this.host;
    }

    public int getPort()
    {
        return this.port;
    }

    public static RealmsServerAddress parseString(String p_parseString_0_)
    {
        if (p_parseString_0_ == null)
        {
            return null;
        }
        else
        {
            String[] var1 = p_parseString_0_.split(":");

            if (p_parseString_0_.startsWith("["))
            {
                int var2 = p_parseString_0_.indexOf("]");

                if (var2 > 0)
                {
                    String var3 = p_parseString_0_.substring(1, var2);
                    String var4 = p_parseString_0_.substring(var2 + 1).trim();

                    if (var4.startsWith(":") && var4.length() > 0)
                    {
                        var4 = var4.substring(1);
                        var1 = new String[] {var3, var4};
                    }
                    else
                    {
                        var1 = new String[] {var3};
                    }
                }
            }

            if (var1.length > 2)
            {
                var1 = new String[] {p_parseString_0_};
            }

            String var5 = var1[0];
            int var6 = var1.length > 1 ? parseInt(var1[1], 25565) : 25565;

            if (var6 == 25565)
            {
                String[] var7 = lookupSrv(var5);
                var5 = var7[0];
                var6 = parseInt(var7[1], 25565);
            }

            return new RealmsServerAddress(var5, var6);
        }
    }

    private static String[] lookupSrv(String p_lookupSrv_0_)
    {
        try
        {
            String var1 = "com.sun.jndi.dns.DnsContextFactory";
            Class.forName("com.sun.jndi.dns.DnsContextFactory");
            Hashtable var2 = new Hashtable();
            var2.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            var2.put("java.naming.provider.url", "dns:");
            var2.put("com.sun.jndi.dns.timeout.retries", "1");
            InitialDirContext var3 = new InitialDirContext(var2);
            Attributes var4 = var3.getAttributes("_minecraft._tcp." + p_lookupSrv_0_, new String[] {"SRV"});
            String[] var5 = var4.get("srv").get().toString().split(" ", 4);
            return new String[] {var5[3], var5[2]};
        }
        catch (Throwable var6)
        {
            return new String[] {p_lookupSrv_0_, Integer.toString(25565)};
        }
    }

    private static int parseInt(String p_parseInt_0_, int p_parseInt_1_)
    {
        try
        {
            return Integer.parseInt(p_parseInt_0_.trim());
        }
        catch (Exception var3)
        {
            return p_parseInt_1_;
        }
    }
}
