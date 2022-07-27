package com.cheatbreaker.client.module;

import com.cheatbreaker.client.module.staff.XRayModule;
import com.cheatbreaker.client.module.type.bossbar.BossBarModule;
import com.cheatbreaker.client.module.type.togglesprint.ToggleSprintModule;
import com.cheatbreaker.client.util.voicechat.VoiceChat;
import com.cheatbreaker.client.module.type.*;
import com.cheatbreaker.client.module.type.armourstatus.ArmourStatusModule;
import com.cheatbreaker.client.module.type.cooldowns.CooldownsModule;
import com.cheatbreaker.client.module.type.keystrokes.KeystrokesModule;
import com.cheatbreaker.client.module.type.notifications.CBNotificationsModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    public List<AbstractModule> modules;
    public List<AbstractModule> staffModules;
    public AbstractModule llIIlllIIIIlllIllIlIlllIl;

    public ToggleSprintModule toggleSprint;
    public MiniMapModule minmap;
    public CBNotificationsModule notifications;
    public ArmourStatusModule armourStatus;
    public CooldownsModule cooldowns;
    public ScoreboardModule scoreboard;
    public XRayModule xray;
    public PotionStatusModule potionStatus;
    public BossBarModule bossBar;
    public DirectionHudModule directionHud;
    public KeystrokesModule keyStrokes;
    public FPSModule fpsModule;
    public CPSModule cpsModule;
    public CoordinatesModule coordinatesModule;
    public VoiceChat voiceChat;
    public TeammatesModule teammatesModule;

    public ModuleManager() {
        modules = new ArrayList<>();
        staffModules = new ArrayList<>();

        modules.add(coordinatesModule = new CoordinatesModule());
        modules.add(minmap = new MiniMapModule());
        modules.add(toggleSprint = new ToggleSprintModule());
        modules.add(potionStatus = new PotionStatusModule());
        modules.add(armourStatus = new ArmourStatusModule());
        modules.add(keyStrokes = new KeystrokesModule());
        modules.add(scoreboard = new ScoreboardModule());
        modules.add(cooldowns = new CooldownsModule());
        modules.add(notifications = new CBNotificationsModule());
        modules.add(directionHud = new DirectionHudModule());
        modules.add(bossBar = new BossBarModule());
        modules.add(cpsModule = new CPSModule());
        modules.add(fpsModule = new FPSModule());
        this.voiceChat = new VoiceChat();
        this.teammatesModule = new TeammatesModule();
        this.teammatesModule.lIIIIlIIllIIlIIlIIIlIIllI(true);

        staffModules.add(xray = new XRayModule());
        for (AbstractModule staffModule : staffModules) {
            staffModule.setStaffModuleEnabled(true);
        }
    }

}
