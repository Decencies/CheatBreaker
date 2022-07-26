package net.minecraft;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.module.type.togglesprint.ToggleSprintModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInputFromOptions;

import java.text.DecimalFormat;

public class MinecraftMovementInputHelper extends MovementInputFromOptions {
    public static boolean lIIIIlIIllIIlIIlIIIlIIllI;
    public static boolean lIIIIIIIIIlIllIIllIlIIlIl;
    public static boolean isSprinting = true;
    public static boolean superSusBoolean = false;
    public static boolean aSusBoolean = false;
    private static long IlIlllIIIIllIllllIllIIlIl;
    private static long llIIlllIIIIlllIllIlIlllIl;
    private static boolean lIIlIlIllIIlIIIlIIIlllIII;
    private static boolean IIIlllIIIllIllIlIIIIIIlII;
    private static boolean llIlIIIlIIIIlIlllIlIIIIll;
    private static boolean IIIlIIllllIIllllllIlIIIll;
    private static boolean lllIIIIIlIllIlIIIllllllII;
    public static String toggleSprintString = "";

    public MinecraftMovementInputHelper(GameSettings gameSettings) {
        super(gameSettings);
    }

    public static void lIIIIlIIllIIlIIlIIIlIIllI(Minecraft minecraft, MovementInputFromOptions movementInputFromOptions, EntityPlayerSP entityPlayerSP) {
        movementInputFromOptions.moveStrafe = 0.0f;
        movementInputFromOptions.moveForward = 0.0f;
        GameSettings gameSettings = minecraft.gameSettings;
        if (gameSettings.keyBindForward.getIsKeyPressed()) {
            movementInputFromOptions.moveForward += 1.0f;
        }
        if (gameSettings.keyBindBack.getIsKeyPressed()) {
            movementInputFromOptions.moveForward -= 1.0f;
        }
        if (gameSettings.keyBindLeft.getIsKeyPressed()) {
            movementInputFromOptions.moveStrafe += 1.0f;
        }
        if (gameSettings.keyBindRight.getIsKeyPressed()) {
            movementInputFromOptions.moveStrafe -= 1.0f;
        }
        if (entityPlayerSP.isRiding() && !IIIlIIllllIIllllllIlIIIll) {
            IIIlIIllllIIllllllIlIIIll = true;
            lllIIIIIlIllIlIIIllllllII = isSprinting;
        } else if (IIIlIIllllIIllllllIlIIIll && !entityPlayerSP.isRiding()) {
            IIIlIIllllIIllllllIlIIIll = false;
            if (lllIIIIIlIllIlIIIllllllII && !isSprinting) {
                isSprinting = true;
                llIIlllIIIIlllIllIlIlllIl = System.currentTimeMillis();
                IIIlllIIIllIllIlIIIIIIlII = true;
                superSusBoolean = false;
            }
        }
        movementInputFromOptions.jump = gameSettings.keyBindJump.getIsKeyPressed();
        if ((Boolean) ToggleSprintModule.toggleSneak.getValue() && CheatBreaker.getInstance().getModuleManager().toggleSprint.isEnabled()) {
            if (gameSettings.keyBindSneak.getIsKeyPressed() && !lIIlIlIllIIlIIIlIIIlllIII) {
                if (entityPlayerSP.isRiding() || entityPlayerSP.capabilities.isFlying) {
                    movementInputFromOptions.sneak = true;
                    llIlIIIlIIIIlIlllIlIIIIll = entityPlayerSP.isRiding();
                } else {
                    movementInputFromOptions.sneak = !movementInputFromOptions.sneak;
                }
                IlIlllIIIIllIllllIllIIlIl = System.currentTimeMillis();
                lIIlIlIllIIlIIIlIIIlllIII = true;
            }
            if (!gameSettings.keyBindSneak.getIsKeyPressed() && lIIlIlIllIIlIIIlIIIlllIII) {
                if (entityPlayerSP.capabilities.isFlying || llIlIIIlIIIIlIlllIlIIIIll) {
                    movementInputFromOptions.sneak = false;
                } else if (System.currentTimeMillis() - IlIlllIIIIllIllllIllIIlIl > 300L) {
                    movementInputFromOptions.sneak = false;
                }
                lIIlIlIllIIlIIIlIIIlllIII = false;
            }
        } else {
            movementInputFromOptions.sneak = gameSettings.keyBindSneak.getIsKeyPressed();
        }
        if (movementInputFromOptions.sneak) {
            movementInputFromOptions.moveStrafe = (float)((double)movementInputFromOptions.moveStrafe * ((double)1.7f * 0.17647058328542756));
            movementInputFromOptions.moveForward = (float)((double)movementInputFromOptions.moveForward * (0.19999999999999998 * 1.5));
        }
        boolean bl = (float)entityPlayerSP.getFoodStats().getFoodLevel() > (float)6 || entityPlayerSP.capabilities.isFlying;
        boolean bl2 = !movementInputFromOptions.sneak && !entityPlayerSP.capabilities.isFlying && bl;
        lIIIIlIIllIIlIIlIIIlIIllI = !((Boolean) ToggleSprintModule.toggleSprint.getValue());
        lIIIIIIIIIlIllIIllIlIIlIl = (Boolean) ToggleSprintModule.doubleTap.getValue();
        if ((bl2 || lIIIIlIIllIIlIIlIIIlIIllI) && gameSettings.keyBindSprint.getIsKeyPressed() && !IIIlllIIIllIllIlIIIIIIlII && !entityPlayerSP.capabilities.isFlying && !lIIIIlIIllIIlIIlIIIlIIllI) {
            isSprinting = !isSprinting;
            llIIlllIIIIlllIllIlIlllIl = System.currentTimeMillis();
            IIIlllIIIllIllIlIIIIIIlII = true;
            superSusBoolean = false;
        }
        if ((bl2 || lIIIIlIIllIIlIIlIIIlIIllI) && !gameSettings.keyBindSprint.getIsKeyPressed() && IIIlllIIIllIllIlIIIIIIlII) {
            if (System.currentTimeMillis() - llIIlllIIIIlllIllIlIlllIl > 300L) {
                superSusBoolean = true;
            }
            IIIlllIIIllIllIlIIIIIIlII = false;
        }
        MinecraftMovementInputHelper.lIIIIlIIllIIlIIlIIIlIIllI(movementInputFromOptions, entityPlayerSP, gameSettings);
    }

    public void setSprintState(boolean bl, boolean bl2) {
        isSprinting = bl;
        aSusBoolean = bl2;
    }

    private static void lIIIIlIIllIIlIIlIIIlIIllI(MovementInputFromOptions movementInputFromOptions, EntityPlayerSP entityPlayerSP, GameSettings gameSettings) {
//        toggleSprintString = "";
        String string = "";
        boolean flying = entityPlayerSP.capabilities.isFlying;
        boolean riding = entityPlayerSP.isRiding();
        boolean sneakHeld = gameSettings.keyBindSneak.getIsKeyPressed();
        boolean sprintHeld = gameSettings.keyBindSprint.getIsKeyPressed();
        if (flying) {
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            string = (Boolean) ToggleSprintModule.flyBoost.getValue() && sprintHeld && entityPlayerSP.capabilities.isCreativeMode ? string + ((String)CheatBreaker.getInstance().getModuleManager().toggleSprint.flyBoostString.getValue()).replaceAll("%BOOST%", decimalFormat.format(ToggleSprintModule.flyBoostAmount.getValue())) : string + CheatBreaker.getInstance().getModuleManager().toggleSprint.flyString.getValue();
        }
        if (riding) {
            string = string + CheatBreaker.getInstance().getModuleManager().toggleSprint.ridingString.getValue();
        }
        if (movementInputFromOptions.sneak) {
            string = flying ? CheatBreaker.getInstance().getModuleManager().toggleSprint.decendString.getValue().toString() :
                    (riding ? CheatBreaker.getInstance().getModuleManager().toggleSprint.dismountString.getValue().toString() :
                            (sneakHeld ? string + CheatBreaker.getInstance().getModuleManager().toggleSprint.sneakHeldString.getValue() :
                                    string + CheatBreaker.getInstance().getModuleManager().toggleSprint.sneakToggledString.getValue()));
        } else if (isSprinting && !flying && !riding) {
            boolean bl5 = superSusBoolean || lIIIIlIIllIIlIIlIIIlIIllI || aSusBoolean;
            string = sprintHeld ? string + CheatBreaker.getInstance().getModuleManager().toggleSprint.sprintHeldString.getValue() : (bl5 ? string + CheatBreaker.getInstance().getModuleManager().toggleSprint.sprintVanillaString.getValue() : string + CheatBreaker.getInstance().getModuleManager().toggleSprint.sprintToggledString.getValue());
        }
        toggleSprintString = string;
    }
}
