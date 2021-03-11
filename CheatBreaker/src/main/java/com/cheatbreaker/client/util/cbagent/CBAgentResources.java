package com.cheatbreaker.client.util.cbagent;

public class CBAgentResources {
    @CBAgentByteArrayReference
    public static native byte[] getBytesNative(String var0);

    @CBAgentBooleanReference
    public static native boolean existsBytesNative(String var0);

    public static boolean existsBytes(String string) {
        boolean bl = false;
        try {
            bl = CBAgentResources.existsBytesNative(string);
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            // empty catch block
        }
        return bl;
    }
}