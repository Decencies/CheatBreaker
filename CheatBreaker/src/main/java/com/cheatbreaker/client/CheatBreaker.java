package com.cheatbreaker.client;

import com.cheatbreaker.client.audio.AudioDevice;
import com.cheatbreaker.client.util.dash.CBDashManager;
import com.cheatbreaker.client.util.voicechat.VoiceChatManager;
import com.cheatbreaker.client.config.ConfigManager;
import com.cheatbreaker.client.config.GlobalSettings;
import com.cheatbreaker.client.config.Profile;
import com.cheatbreaker.client.event.EventBus;
import com.cheatbreaker.client.event.type.KeyboardEvent;
import com.cheatbreaker.client.event.type.PluginMessageEvent;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.module.ModuleManager;
import com.cheatbreaker.client.nethandler.NetHandler;
import com.cheatbreaker.client.ui.module.CBModulesGui;
import com.cheatbreaker.client.util.cosmetic.Cosmetic;
import com.cheatbreaker.client.util.friend.Status;
import com.cheatbreaker.client.util.thread.ServerStatusThread;
import com.cheatbreaker.client.util.SessionServer;
import com.cheatbreaker.client.ui.util.font.CBFontRenderer;
import com.cheatbreaker.client.ui.overlay.Alert;
import com.cheatbreaker.client.util.friend.FriendsManager;
import com.cheatbreaker.client.ui.overlay.OverlayGui;
import com.cheatbreaker.client.util.title.TitleManager;
import com.cheatbreaker.client.util.worldborder.WorldBorderManager;
import com.cheatbreaker.client.websocket.AssetsWebSocket;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.*;

@Getter
public class CheatBreaker implements SkinManager.SkinAvailableCallback {

    @Getter
    public static CheatBreaker instance;
    public static byte[] processBytesAuth = "Decencies".getBytes(); // originally "Vote Trump 2020!" (jhalt's doing LMAO???)

    public List<Profile> profiles;
    public Profile activeProfile;
    public GlobalSettings globalSettings;
    public ModuleManager moduleManager;
    public ConfigManager configManager;
    public EventBus eventBus;

    public List<SessionServer> statusServers;
    public List<ResourceLocation> presetLocations;

    public CBFontRenderer playBold22px;
    public CBFontRenderer playRegular22px;
    public CBFontRenderer ubuntuMedium16px;
    public CBFontRenderer playBold18px;
    public CBFontRenderer robotoRegular24px;
    public CBFontRenderer playRegular18px;
    public CBFontRenderer playRegular14px;
    public CBFontRenderer playRegular16px;
    public CBFontRenderer robotoRegular13px;
    public CBFontRenderer robotoBold14px;
    public CBFontRenderer playRegular12px;

    private static final ResourceLocation playRegular;
    private static final ResourceLocation playBold;
    private static final ResourceLocation robotoRegular;
    private static final ResourceLocation robotoBold;
    private static final ResourceLocation ubuntuMedium;

    public NetHandler netHandler;
    public TitleManager titleManager;
    public WorldBorderManager borderManager;

    static {
        playRegular = new ResourceLocation("client/font/Play-Regular.ttf");
        playBold = new ResourceLocation("client/font/Play-Bold.ttf");
        robotoRegular = new ResourceLocation("client/font/Roboto-Regular.ttf");
        robotoBold = new ResourceLocation("client/font/Roboto-Bold.ttf");
        ubuntuMedium = new ResourceLocation("client/font/Ubuntu-M.ttf");
    }

    public long startTime;

    private List<AudioDevice> audioDevices = new ArrayList<>();
    private final VoiceChatManager voiceChatManager;

    public static final AudioFormat universalAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000.0f, 16, 1, 2, 16000.0f, false);

    public List<Session> sessions;

    private List<Cosmetic> cosmetics = new ArrayList<>();
    private AssetsWebSocket websocket;

    private String gitCommit = "?";
    private String gitCommitId = "?";
    private String gitBranch = "?";

    private FriendsManager friendsManager;
    private Status statusEnum;

    @Setter
    private boolean consoleAllowed;

    private List<String> consoleLines;
    private CBDashManager radioManager;

    @Setter
    private boolean acceptingFriendRequests;

    private final Map<String, ResourceLocation> playerSkins = new HashMap<>();

    public boolean isConsoleAllowed() {
        return true;
    }

    public CheatBreaker() {
        this.audioDevices = new ArrayList<>();
        this.presetLocations = new ArrayList<>();
        cosmetics = new ArrayList<>();
        sessions = new ArrayList<>();
        statusServers = new ArrayList<>();
        this.profiles = new ArrayList<>();
        this.consoleLines = new ArrayList<>();
        this.startTime = System.currentTimeMillis();
        System.out.println("[CB] Starting CheatBreaker setup");
        this.createDefaultConfigPresets();
        System.out.println("[CB] Created default configuration presets");
        CheatBreaker.instance = this;
        this.initAudioDevices();
        this.voiceChatManager = new VoiceChatManager(audioDevices.get(0));
        this.globalSettings = new GlobalSettings();
        System.out.println("[CB] Created settings");
        this.eventBus = new EventBus();
        System.out.println("[CB] Created EventBus");
        this.moduleManager = new ModuleManager();
        System.out.println("[CB] Created Mod Manager");
        this.netHandler = new NetHandler();
        System.out.println("[CB] Created Net Handler");
        this.titleManager = new TitleManager();
        cosmetics.add(new Cosmetic("Steve", "CheatBreaker Cape", 1.0f, true, "client/defaults/cb.png"));
        cosmetics.add(new Cosmetic("Steve", "CheatBreaker Black Cape", 1.0f, false, "client/defaults/cb_black.png"));
        this.statusEnum = Status.AWAY;
        this.radioManager = new CBDashManager();

    }

    public <T> void reverse(Queue<T> queue) {
        Stack<T> stack = new Stack<>();
        while(!queue.isEmpty()) stack.push(queue.poll());
        while(!stack.isEmpty()) queue.add(stack.pop());
    }

    public void initialize() {
        this.loadFonts();
        System.out.println("[CB] Loaded all fonts");
        this.loadProfiles();
        System.out.println("[CB] Loaded " + this.profiles.size() + " custom profiles");
        (this.configManager = new ConfigManager()).read();

        connectToAssetsServer();
        this.friendsManager = new FriendsManager();
        OverlayGui.setInstance(new OverlayGui());

        this.eventBus.addEvent(PluginMessageEvent.class, this.netHandler::onPluginMessage);

        this.eventBus.addEvent(KeyboardEvent.class, (e) -> {
            if (e.getKeyboardKey() == Keyboard.KEY_H) {
                Alert.displayMessage("Hello", "Hello, World\nNew Line");
            }
            if (e.getKeyboardKey() == Keyboard.KEY_RSHIFT) {
                if (Minecraft.getMinecraft().currentScreen == null) {
                    Minecraft.getMinecraft().displayGuiScreen(new CBModulesGui());
                }
            }
        });

        new ServerStatusThread().start();
        loadVersionData();
        this.borderManager = new WorldBorderManager();
    }

    private void loadFonts() {
        playBold22px = new CBFontRenderer(playBold, 22);
        playRegular22px = new CBFontRenderer(playRegular, 22);
        playRegular18px = new CBFontRenderer(playRegular, 18);
        playRegular14px = new CBFontRenderer(playRegular, 14);
        playRegular12px = new CBFontRenderer(playRegular, 12);
        playRegular16px = new CBFontRenderer(playRegular, 16);
        playBold18px = new CBFontRenderer(playBold, 18);
        ubuntuMedium16px = new CBFontRenderer(ubuntuMedium, 16);
        robotoRegular13px = new CBFontRenderer(robotoRegular, 13);
        robotoBold14px = new CBFontRenderer(robotoBold, 14);
        robotoRegular24px = new CBFontRenderer(robotoRegular, 24);
    }

    private void createDefaultConfigPresets() {
        File file = ConfigManager.profilesDir;
        if (file.exists() || file.mkdirs()) {
            for (ResourceLocation resourceLocation : presetLocations) {
                File file2 = new File(file, resourceLocation.getResourcePath().replaceAll("([a-zA-Z0-9/]+)/", ""));
                if (!file2.exists()) {
                    try {
                        InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation).getInputStream();
                        Files.copy(stream, file2.toPath());
                        stream.close();
                    }
                    catch (final IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private void loadProfiles() {
        this.profiles.add(new Profile("default", false));
        final File dir = ConfigManager.profilesDir;
        final File[] files;
        if (dir.exists() && dir.isDirectory() && (files = dir.listFiles()) != null) {
            for (final File file : files) {
                if (file.getName().endsWith(".cfg")) {
                    this.profiles.add(new Profile(file.getName().replace(".cfg", ""), true));
                }
            }
        }
    }

    private String getNewProfileName(final String base) {
        final File dir = ConfigManager.profilesDir;
        if (dir.exists() || dir.mkdirs()) {
            if (new File(dir + File.separator + base + ".cfg").exists()) {
                return this.getNewProfileName(base + "1");
            }
        }
        return base;
    }

    public void createNewProfile() {
        if (this.activeProfile == this.profiles.get(0)) {
            final Profile profile = new Profile(this.getNewProfileName("Profile 1"), true);
            this.activeProfile = profile;
            this.profiles.add(profile);
            this.configManager.write();
        }
    }

    private void initAudioDevices() {
        final Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (final Mixer.Info info : mixers) {
            final Mixer mixer = AudioSystem.getMixer(info);
            try {
                TargetDataLine dataLine = (TargetDataLine) mixer.getLine(new DataLine.Info(TargetDataLine.class, CheatBreaker.universalAudioFormat));
                if (info != null) {
                    System.out.println("[CB] Added mic option : " + info.getName());
                    this.audioDevices.add(new AudioDevice(info.getDescription(), info.getName(), dataLine));
                }
            } catch (final IllegalArgumentException | LineUnavailableException ignored) {
                // the device was not a microphone.
            }
        }
    }

    public String[] getAudioDeviceList() {
        final String[] audioDevices = new String[this.audioDevices.size()];
        int var1 = 0;
        for(final Iterator<AudioDevice> var2 = this.audioDevices.iterator(); var2.hasNext(); ++var1) {
            final AudioDevice var3 = var2.next();
            audioDevices[var1] = var3.getDescriptor();
        }
        return audioDevices;
    }

    public boolean isUsingStaffModules() {
        for (final AbstractModule cbModule : this.moduleManager.staffModules) {
            if (cbModule.isStaffEnabledModule()) {
                return true;
            }
        }
        return false;
    }

    public float getScaleFactor() {
        switch (Minecraft.getMinecraft().gameSettings.guiScale) {
            case 0: {
                return 2.0f;
            }
            case 1: {
                return 0.5f;
            }
            case 3: {
                return 1.5f;
            }
            default: {
                return 1.0f;
            }
        }
    }

    public void sendSound(final String s) {
        this.sendSoundVol(s, 1.0f);
    }

    public void sendSoundVol(final String s, final float n) {
        if (!(boolean)this.globalSettings.muteCheatBreakerSounds.getValue())
            Minecraft.getMinecraft().getSoundHandler().field_147694_f.playSound(s, n);
    }

    public ResourceLocation getHeadLocation(String displayName, String uuid) {
        ResourceLocation playerSkin = this.playerSkins.getOrDefault(displayName, new ResourceLocation("client/heads/" + displayName + ".png"));
        if (!this.playerSkins.containsKey(displayName)) {
            ThreadDownloadImageData skinData = new ThreadDownloadImageData(null, "https://minotar.net/helm/" + displayName + "/32.png", new ResourceLocation("client/defaults/steve.png"), null);
            (Minecraft.getMinecraft()).getTextureManager().loadTexture(playerSkin, skinData);
            this.playerSkins.put(displayName, playerSkin);
        }
        return playerSkin;
    }

    public String getPluginMessageChannel() {
        return "CB-Client";
    }

    public String getPluginBinaryChannel() {
        return "CB-Binary";
    }

    @Override
    public void func_152121_a(MinecraftProfileTexture.Type p_152121_1_, ResourceLocation p_152121_2_) {
    }

    public void connectToAssetsServer() {
        final Map<String, String> hashMap = new HashMap<>();
        hashMap.put("username", Minecraft.getMinecraft().getSession().getUsername());
        hashMap.put("playerId", Minecraft.getMinecraft().getSession().getPlayerID());
        hashMap.put("version", getGitCommit());
        try {
            (this.websocket = new AssetsWebSocket(new URI("ws://localhost:80"), hashMap)).connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void loadVersionData() {
        try {
            final ResourceLocation resourceLocation = new ResourceLocation("client/properties/app.properties");
            final Properties properties = new Properties();
            InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation).getInputStream();

            if (inputStream == null) {
                this.gitCommit = "?";
                this.gitCommitId = "?";
                this.gitBranch = "?";
                return;
            }

            properties.load(inputStream);
            this.gitCommit = properties.getProperty("git.commit.id.abbrev");
            this.gitCommitId = properties.getProperty("git.commit.id");
            this.gitBranch = properties.getProperty("git.branch");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AssetsWebSocket getAssetsWebSocket() {
        return websocket;
    }

    public Status getStatus() {
        return statusEnum;
    }

    public void setStatus(Status cbStatusEnum) {
        this.statusEnum = cbStatusEnum;
    }

    public String getStatusString() {
        String s;
        switch (this.getStatus()) {
            case AWAY: {
                s = "Away";
                break;
            }
            case BUSY: {
                s = "Busy";
                break;
            }
            case HIDDEN: {
                s = "Hidden";
                break;
            }
            default: {
                s = "Online";
                break;
            }
        }
        return s;
    }

    public void removeCosmeticsFromPlayer(final String playerId) {
        this.cosmetics.removeIf(cosmetic -> cosmetic.getPlayerId().equals(playerId));
        this.cosmetics.removeIf(cosmetic -> cosmetic.getPlayerId().equals(playerId));
    }
}
