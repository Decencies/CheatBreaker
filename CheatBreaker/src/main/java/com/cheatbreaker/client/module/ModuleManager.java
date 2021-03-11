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

    public ToggleSprintModule toggleSprint;
    public List<AbstractModule> modules;
    public List<AbstractModule> staffModules;
    public MiniMapModule minmap;
    public CBNotificationsModule notifications;
    public ArmourStatusModule armourStatus;
    public CooldownsModule cooldowns;
    public ScoreboardModule scoreboard;
    public XRayModule xray;
    public PotionStatusModule potionStatus;
    public BossBarModule bossBar;
    public DirectionHudModule directionHud;
    public AbstractModule llIIlllIIIIlllIllIlIlllIl;
    public KeystrokesModule keyStrokes;
    public FPSModule fpsModule;
    public CPSModule cpsModule;
    public CoordinatesModule coordinatesModule;
    public VoiceChat voiceChat;
    public TeammatesModule teammatesModule;

    public ModuleManager() {
        modules = new ArrayList<>();
        staffModules = new ArrayList<>();
        staffModules.add(xray = new XRayModule());
        modules.add(minmap = new MiniMapModule());
        modules.add(notifications = new CBNotificationsModule());
        modules.add(armourStatus = new ArmourStatusModule());
        modules.add(cooldowns = new CooldownsModule());
        modules.add(scoreboard = new ScoreboardModule());
        modules.add(potionStatus = new PotionStatusModule());
        modules.add(keyStrokes = new KeystrokesModule());
        modules.add(fpsModule = new FPSModule());
        modules.add(cpsModule = new CPSModule());
        modules.add(coordinatesModule = new CoordinatesModule());
        modules.add(directionHud = new DirectionHudModule());
        for (AbstractModule staffModule : staffModules) {
            staffModule.setStaffModuleEnabled(true);
        }
        modules.add((this.bossBar = new BossBarModule()));
        modules.add((this.toggleSprint = new ToggleSprintModule()));
        this.voiceChat = new VoiceChat();
        this.teammatesModule = new TeammatesModule();
        this.teammatesModule.lIIIIlIIllIIlIIlIIIlIIllI(true);
    }

}
