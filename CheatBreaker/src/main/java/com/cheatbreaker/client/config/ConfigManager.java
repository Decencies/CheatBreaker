package com.cheatbreaker.client.config;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.module.AbstractModule;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.ArrayList;

public class ConfigManager {

    public static final File configDir;
    public static final File profilesDir;
    private static final File globalConfig;

    static {
        configDir = new File(Minecraft.getMinecraft().mcDataDir + File.separator + "config" + File.separator + "client");
        profilesDir = new File(configDir + File.separator + "profiles");
        globalConfig = new File(configDir + File.separator + "global.cfg");
    }

    public void write() {
        try {
            if (this.createRequiredFiles()) {
                this.writeGlobalConfig(globalConfig);
                this.writeProfile(CheatBreaker.getInstance().activeProfile.getName());
            }
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public void read() {
        try {
            if (this.createRequiredFiles()) {
                this.readGlobalConfig(globalConfig);
                if (CheatBreaker.getInstance().activeProfile == null) {
                    CheatBreaker.getInstance().activeProfile = CheatBreaker.getInstance().profiles.get(0);
                }
                this.readProfile(CheatBreaker.getInstance().activeProfile.getName());
            }
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    private boolean createRequiredFiles() throws IOException {
        return !(!configDir.exists() && !configDir.mkdirs() || !globalConfig.exists() && !globalConfig.createNewFile());
    }

    public void readGlobalConfig(File file) {
        if (!file.exists()) {
            this.writeGlobalConfig(file);
            return;
        }
        try {
            String line;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                try {
                    File profileFile;
                    String[] split;
                    if (line.startsWith("#") || line.length() == 0 || (split = line.split("=", 2)).length != 2)
                        continue;
                    if (split[0].equalsIgnoreCase("ProfileIndexes")) {
                        for (String declaration : split[1].split("]\\[")) {
                            declaration = declaration.replaceFirst("\\[", "");
                            String[] pair = declaration.split(",", 2);
                            try {
                                int index = Integer.parseInt(pair[1]);
                                for (Profile profile : CheatBreaker.getInstance().profiles) {
                                    if (index == 0 || !profile.getName().equalsIgnoreCase(pair[0])) continue;
                                    profile.setIndex(index);
                                }
                            } catch (NumberFormatException numberFormatException) {
                                // empty catch block
                            }
                        }
                        continue;
                    }
                    if (split[0].equalsIgnoreCase("ActiveProfile")) {
                        profileFile = null;
                        File profilesDir = new File(configDir + File.separator + "profiles");
                        if (profilesDir.exists() || profilesDir.mkdirs()) {
                            profileFile = new File(profilesDir + File.separator + split[1] + ".cfg");
                        }
                        if (profileFile == null || !profileFile.exists()) continue;
                        Profile activeProfile = null;
                        for (Profile profile : CheatBreaker.getInstance().profiles) {
                            if (!split[1].equalsIgnoreCase((profile).getName())) continue;
                            activeProfile = profile;
                        }
                        if (activeProfile == null || !activeProfile.isEditable()) continue;
                        CheatBreaker.getInstance().activeProfile = activeProfile;
                        continue;
                    }
                    for (Setting setting : CheatBreaker.getInstance().globalSettings.settingsList) {
                        if (setting.getLabel().equalsIgnoreCase("label") || !setting.getLabel().equalsIgnoreCase(split[0]))
                            continue;

                        switch (setting.getType()) {
                            case BOOLEAN: {
                                setting.setValue(Boolean.parseBoolean(split[1]));
                                break;
                            }
                            case INTEGER: {
                                if (split[1].contains("rainbow")) {
                                    setting.rainbow = true;
                                    int n = Integer.parseInt(split[1].split(";")[0]);
                                    if (n > (Integer) setting.getMinimumValue() || n < (Integer) setting.getMaximumValue())
                                        continue;
                                    setting.setValue(n);
                                    break;
                                }
                                setting.rainbow = false;
                                int n = Integer.parseInt(split[1]);
                                if (n > (Integer) setting.getMinimumValue() || n < (Integer) setting.getMaximumValue())
                                    continue;
                                setting.setValue(n);
                                break;
                            }
                            case FLOAT: {
                                float f = Float.parseFloat(split[1]);
                                if (!(f <= (Float) setting.getMinimumValue()) || !(f >= (Float) setting.getMaximumValue()))
                                    break;
                                setting.setValue(f);
                                break;
                            }
                            case DOUBLE: {
                                double d = Double.parseDouble(split[1]);
                                if (!(d <= (Double) setting.getMinimumValue()) || !(d >= (Double) setting.getMaximumValue()))
                                    break;
                                setting.setValue(d);
                                break;
                            }
                            case STRING_ARRAY: {
                                boolean changed = false;
                                for (Object value : setting.getAcceptedValues()) {
                                    if (!((String) value).equalsIgnoreCase(split[1])) continue;
                                    changed = true;
                                }
                                if (!changed) break;
                                setting.setValue(split[1]);
                            }
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            bufferedReader.close();
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
        this.writeGlobalConfig(file);
    }

    public void writeGlobalConfig(File file) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write("################################");
            bufferedWriter.newLine();
            bufferedWriter.write("# MC_Client: GLOBAL SETTINGS");
            bufferedWriter.newLine();
            bufferedWriter.write("################################");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            if (CheatBreaker.getInstance().activeProfile != null && CheatBreaker.getInstance().activeProfile.isEditable()) {
                bufferedWriter.write("ActiveProfile=" + CheatBreaker.getInstance().activeProfile.getName());
                bufferedWriter.newLine();
            }
            for (Setting cBSetting : CheatBreaker.getInstance().globalSettings.settingsList) {
                if (cBSetting.getLabel().equalsIgnoreCase("label")) continue;
                if (cBSetting.rainbow) {
                    bufferedWriter.write(cBSetting.getLabel() + "=" + cBSetting.getValue() + ";rainbow");
                } else {
                    bufferedWriter.write(cBSetting.getLabel() + "=" + cBSetting.getValue());
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
            bufferedWriter.write("ProfileIndexes=");
            for (Profile profile : CheatBreaker.getInstance().profiles) {
                bufferedWriter.write("[" + profile.getName() + "," + profile.getIndex() + "]");
            }
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public void readProfile(String name) {
        if (name.equalsIgnoreCase("default")) {
            System.out.println("[CB] setting up default profile");
            for (AbstractModule module : CheatBreaker.getInstance().moduleManager.modules) {
                module.setState(module.defaultState);
                module.setAnchor(module.defaultGuiAnchor);
                module.setTranslations(module.defaultXTranslation, module.defaultYTranslation);
                module.setRenderHud(module.defaultRenderHud);
                for (int i = 0; i < module.getSettingsList().size(); ++i) {
                    try {
                        module.getSettingsList().get(i).setValue(module.getDefaultSettingsValues().get(i), false);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
            return;
        }
        File file = new File(configDir + File.separator + "profiles");
        File file2 = file.exists() || file.mkdirs() ? new File(file + File.separator + name + ".cfg") : null;
        if (file2 == null || !file2.exists()) {
            this.writeProfile(name);
            return;
        }
        ArrayList<AbstractModule> arrayList = new ArrayList<>(CheatBreaker.getInstance().moduleManager.modules);
        try {
            String line;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file2));
            AbstractModule object = null;
            while ((line = bufferedReader.readLine()) != null) {
                try {
                    String[] split;
                    if (line.startsWith("#") || line.length() == 0) continue;
                    if (line.startsWith("[")) {
                        for (AbstractModule module : arrayList) {
                            if (!("[" + module.getName() + "]").equalsIgnoreCase(line)) continue;
                            object = module;
                        }
                        continue;
                    }
                    if (object == null) continue;
                    if (line.startsWith("-")) {
                        split = line.replaceFirst("-", "").split("=", 2);
                        if (split.length != 2) continue;
                        try {
                            switch (split[0]) {
                                case "State": {
                                    if (object.isStaffModule()) break;
                                    object.setState(Boolean.parseBoolean(split[1]));
                                    break;
                                }
                                case "RenderHUD": {
                                    object.setRenderHud(Boolean.parseBoolean(split[1]));
                                    break;
                                }
                                case "xTranslation": {
                                    object.setXTranslation(Float.parseFloat(split[1]));
                                    break;
                                }
                                case "yTranslation": {
                                    object.setYTranslation(Float.parseFloat(split[1]));
                                }
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        continue;
                    }
                    split = line.split("=", 2);
                    if (split.length != 2) continue;
                    for (Setting setting : object.getSettingsList()) {
                        if (setting.getLabel().equalsIgnoreCase("label") || !setting.getLabel().equalsIgnoreCase(split[0]))
                            continue;

                        switch (setting.getType()) {
                            case BOOLEAN: {
                                setting.setValue(Boolean.parseBoolean(split[1]));
                                break;
                            }
                            case INTEGER: {
                                if (split[1].contains("rainbow")) {
                                    Object[] arrobject = split[1].split(";");
                                    int n = Integer.parseInt((String) arrobject[0]);
                                    setting.rainbow = true;
                                    if (n > (Integer) setting.getMinimumValue() || n < (Integer) setting.getMaximumValue())
                                        continue;
                                    setting.setValue(n);
                                    break;
                                }
                                int n = Integer.parseInt(split[1]);
                                setting.rainbow = false;
                                if (n > (Integer) setting.getMinimumValue() || n < (Integer) setting.getMaximumValue())
                                    continue;
                                setting.setValue(n);
                                break;
                            }
                            case FLOAT: {
                                float f = Float.parseFloat(split[1]);
                                if (!(f <= (Float) setting.getMinimumValue()) || !(f >= (Float) setting.getMaximumValue()))
                                    break;
                                setting.setValue(f);
                                break;
                            }
                            case DOUBLE: {
                                double d = Double.parseDouble(split[1]);
                                if (!(d <= (Double) setting.getMinimumValue()) || !(d >= (Double) setting.getMaximumValue()))
                                    break;
                                setting.setValue(d);
                                break;
                            }
                            case STRING_ARRAY: {
                                boolean bl = false;
                                for (Object object3 : setting.getAcceptedValues()) {
                                    if (!((String) object3).equalsIgnoreCase(split[1])) continue;
                                    bl = true;
                                }
                                if (!bl) break;
                                setting.setValue(split[1]);
                                break;
                            }
                            case STRING: {
                                if (setting.getLabel().equalsIgnoreCase("label")) break;
                                setting.setValue(split[1].replaceAll("&([abcdefghijklmrABCDEFGHIJKLMNR0-9])|(&$)", "§$1"));
                            }
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            bufferedReader.close();
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public void writeProfile(String string) {
        if (string.equalsIgnoreCase("default")) {
            return;
        }
        File profilesDir = new File(configDir + File.separator + "profiles");
        File profileFile = profilesDir.exists() || profilesDir.mkdirs() ? new File(profilesDir + File.separator + string + ".cfg") : null;
        if (profileFile == null) {
            System.err.println("[CB] Config manager panic!");
            return;
        }
        ArrayList<AbstractModule> arrayList = new ArrayList<>(CheatBreaker.getInstance().moduleManager.modules);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(profileFile));
            bufferedWriter.write("################################");
            bufferedWriter.newLine();
            bufferedWriter.write("# MC_Client: MODULE SETTINGS");
            bufferedWriter.newLine();
            bufferedWriter.write("################################");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            for (AbstractModule Module : arrayList) {
                bufferedWriter.write("[" + Module.getName() + "]");
                bufferedWriter.newLine();
                bufferedWriter.write("-State=" + Module.isEnabled());
                bufferedWriter.newLine();
                bufferedWriter.write("-xTranslation=" + Module.getXTranslation());
                bufferedWriter.newLine();
                bufferedWriter.write("-yTranslation=" + Module.getYTranslation());
                bufferedWriter.newLine();
                bufferedWriter.write("-RenderHUD=" + Module.isRenderHud());
                bufferedWriter.newLine();
                for (Setting cBSetting : Module.getSettingsList()) {
                    if (cBSetting.getLabel().equalsIgnoreCase("label")) continue;
                    if (cBSetting.getType() == Setting.Type.STRING) {
                        bufferedWriter.write(cBSetting.getLabel() + "=" + (cBSetting.getValue() + "").replaceAll("§", "&"));
                    } else if (cBSetting.rainbow) {
                        bufferedWriter.write(cBSetting.getLabel() + "=" + cBSetting.getValue() + ";rainbow");
                    } else {
                        bufferedWriter.write(cBSetting.getLabel() + "=" + cBSetting.getValue());
                    }
                    bufferedWriter.newLine();
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

}
