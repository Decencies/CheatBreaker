import java.io.File;
import java.util.Arrays;
import java.util.UUID;

import com.google.common.eventbus.EventBus;
import net.minecraft.client.main.Main;

public class Start
{
    public static void main(final String[] args) {
        final String system = System.getProperty("os.name").toLowerCase();
        File file;
        if (system.contains("win") && (file = new File(new File(System.getenv("APPDATA")), ".minecraft")).exists()) {
            System.out.println("Loaded CheatBreaker for Windows");
        }
        else {
            if (!system.contains("mac") || !(file = new File(new File(System.getProperty("user.home")), "Library/Application Support/minecraft")).exists()) {
                throw new RuntimeException("Failed to determine Minecraft directory");
            }
            System.out.println("Loaded CheatBreaker for MacOS");
        }
        Main.main(concat(new String[] {
                "--version", "CheatBreaker",
                "--accessToken", "0",
                "--assetIndex", "1.7.10",
                "--userProperties", "{}",
                "--gameDir", file.getAbsolutePath(),
                "--uuid", UUID.nameUUIDFromBytes(args[1].getBytes()).toString(),
                "--assetsDir", new File(file, "assets").getAbsolutePath()
        }, args));
    }

    public static <T> T[] concat(final T[] first, final T[] second) {
        final T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
