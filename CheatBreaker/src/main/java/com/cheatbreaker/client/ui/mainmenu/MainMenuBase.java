package com.cheatbreaker.client.ui.mainmenu;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.ui.AbstractGui;
import com.cheatbreaker.client.ui.mainmenu.cosmetics.GuiCosmetics;
import com.cheatbreaker.client.ui.mainmenu.element.IconButtonElement;
import com.cheatbreaker.client.ui.mainmenu.element.TextButtonElement;
import com.cheatbreaker.client.ui.util.RenderUtil;
import com.cheatbreaker.client.ui.fading.ColorFade;
import com.google.gson.*;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import java.awt.*;
import java.io.*;
import java.net.Proxy;
import java.util.List;
import java.util.*;

public class MainMenuBase extends AbstractGui {
    private static int IIIllIllIlIlllllllIlIlIII = 4100;
    private final ResourceLocation logo = new ResourceLocation("client/logo_42.png");
    private final IconButtonElement exitButton;
    private final IconButtonElement languageButton;
    private final AccountList IlllIllIlIIIIlIIlIIllIIIl;
    private final TextButtonElement optionsButton;
    private final TextButtonElement changelogButton;
    private final TextButtonElement cosmeticsButton;
    private final ColorFade IllIlIIIIlllIIllIIlllIIlI;
    private final ResourceLocation[] IllIlIlIllllIlIIllllIIlll = new ResourceLocation[]{new ResourceLocation("client/panorama/0.png"), new ResourceLocation("client/panorama/1.png"), new ResourceLocation("client/panorama/2.png"), new ResourceLocation("client/panorama/3.png"), new ResourceLocation("client/panorama/4.png"), new ResourceLocation("client/panorama/5.png")};
    private ResourceLocation panoramaBackgroundLocation;
    private final File launcherAccounts;
    private final List<Account> accountsList;
    private float accountButtonWidth;

    public MainMenuBase() {
        this.launcherAccounts = new File(Minecraft.getMinecraft().mcDataDir + File.separator + "launcher_accounts.json");
        this.accountsList = new ArrayList<>();
        this.optionsButton = new TextButtonElement("OPTIONS");
        this.cosmeticsButton = new TextButtonElement("COSMETICS");
        this.changelogButton = new TextButtonElement("CHANGELOG");
        this.IllIlIIIIlllIIllIIlllIIlI = new ColorFade(0xF000000, -16777216);
        this.exitButton = new IconButtonElement(new ResourceLocation("client/icons/delete-64.png"));
        this.languageButton = new IconButtonElement(6, new ResourceLocation("client/icons/globe-24.png"));
        this.accountButtonWidth = CheatBreaker.getInstance().robotoRegular13px.getStringWidth(Minecraft.getMinecraft().getSession().getUsername());
        this.IlllIllIlIIIIlIIlIIllIIIl = new AccountList(this, Minecraft.getMinecraft().getSession().getUsername(), CheatBreaker.getInstance().getHeadLocation(Minecraft.getMinecraft().getSession().getUsername(), Minecraft.getMinecraft().getSession().getPlayerID()));
        //this.loadAccounts();
    }

    /*
     * Could not resolve type clashes
     */
    private void loadAccounts() {
        // TODO: rewrite this
        Minecraft minecraft = Minecraft.getMinecraft();
        if (launcherAccounts.exists()) {
            try (final BufferedReader reader = new BufferedReader(new FileReader(new File(Minecraft.getMinecraft().mcDataDir, "launcher_accounts.json")))) {
                final JsonObject object = new JsonParser().parse(reader).getAsJsonObject();
                final Set<Map.Entry<String, JsonElement>> accounts = object.getAsJsonObject("accounts").entrySet();
                for (Map.Entry<String, JsonElement> accountElement : accounts) {
                    JsonObject account = accountElement.getValue().getAsJsonObject();
                    String accessToken = account.get("accessToken").getAsString();
                    JsonObject minecraftProfileObject = account.getAsJsonObject("minecraftProfile");
                    String uuid = minecraftProfileObject.get("id").getAsString();
                    String displayName = minecraftProfileObject.get("name").getAsString();
                    String clientToken = object.get("mojangClientToken").getAsString();
                    String userName = account.get("username").toString();
                    if (userName != null){
                        Account finalAccount = new Account(userName, clientToken,accessToken, displayName, uuid);
                        accountsList.add(finalAccount);
                        System.out.println("[CB] added account " + finalAccount.getUsername() + ".");
                        float f = CheatBreaker.getInstance().robotoRegular13px.getStringWidth(finalAccount.getDisplayName());
                        if (f > this.accountButtonWidth) {
                            this.accountButtonWidth = f;
                        }
                        if (minecraft.getSession() == null || !finalAccount.getUsername().equalsIgnoreCase(minecraft.getSession().getUsername()))
                            continue;
                        this.IlllIllIlIIIIlIIlIIllIIIl.lIIIIlIIllIIlIIlIIIlIIllI(finalAccount.getDisplayName());
                        this.IlllIllIlIIIIlIIlIIllIIIl.lIIIIlIIllIIlIIlIIIlIIllI(CheatBreaker.getInstance().getHeadLocation(finalAccount.getDisplayName(), finalAccount.getDisplayName()));
                        this.updateAccountButtonSize();
                    } else {
                        System.err.println("[CB] userName is null.");
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        if (this.IlllIllIlIIIIlIIlIIllIIIl != null) {
            this.IlllIllIlIIIIlIIlIIllIIIl.handleElementMouse();
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        ++IIIllIllIlIlllllllIlIlIII;
    }

    @Override
    public void initGui() {
        super.initGui();
        DynamicTexture IIIIllIIllIIIIllIllIIIlIl = new DynamicTexture(256, 256);
        this.panoramaBackgroundLocation = this.mc.getTextureManager().getDynamicTextureLocation("background", IIIIllIIllIIIIllIllIIIlIl);
        this.optionsButton.setElementSize((float) 124, (float) 6, (float) 42, 20);
        this.cosmeticsButton.setElementSize((float) 167, (float) 6, (float) 48, 20);
        this.exitButton.setElementSize(this.getScaledWidth() - (float) 30, (float) 7, (float) 23, 17);
        this.languageButton.setElementSize(this.getScaledWidth() / 2.0f - (float) 13, this.getScaledHeight() - (float) 17, (float) 26, 18);
        this.updateAccountButtonSize();
    }

    public void updateAccountButtonSize() {
        this.IlllIllIlIIIIlIIlIIllIIIl.setElementSize(this.getScaledWidth() - (float) 35 - this.IlllIllIlIIIIlIIlIIllIIIl.IIIIllIIllIIIIllIllIIIlIl(this.accountButtonWidth), (float) 7, this.IlllIllIlIIIIlIIlIIllIIIl.IIIIllIIllIIIIllIllIIIlIl(this.accountButtonWidth), 17);
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        GL11.glDisable(0xbc0);
        this.renderSkybox(n, n2, 1.0f);
        GL11.glEnable(3008);
        super.drawScreen(n, n2, f);
    }

    @Override
    public void drawMenu(float f, float f2) {
        drawGradientRect(0.0f, 0.0f, this.getScaledWidth(), this.getScaledHeight(), 0x5FFFFFFF, 0x2FFFFFFF);
        drawGradientRect(0.0f, 0.0f, this.getScaledWidth(), 160, -553648128, 0);
        boolean bl = f < this.optionsButton.getX() && f2 < (float) 30;
        Color color = this.IllIlIIIIlllIIllIIlllIIlI.lIIIIIIIIIlIllIIllIlIIlIl(bl);
        CheatBreaker.getInstance().robotoRegular24px.drawCenteredString("CheatBreaker", 37, (float) 9, color.getRGB());
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.logo, (float) 10, (float) 8, (float) 6);
        CheatBreaker.getInstance().robotoRegular24px.drawString("CheatBreaker", 36, (float) 8, -1);
        String string = "CheatBreaker Dev (" + CheatBreaker.getInstance().getGitCommit() + "/" + CheatBreaker.getInstance().getGitBranch() + ")";
        CheatBreaker.getInstance().playRegular18px.drawStringWithShadow(string, (float) 5, this.getScaledHeight() - (float) 14, -1879048193);
        String string2 = "Copyright Mojang AB. Do not distribute!";
        CheatBreaker.getInstance().playRegular18px.drawStringWithShadow(string2, this.getScaledWidth() - (float) CheatBreaker.getInstance().playRegular18px.getStringWidth(string2) - 5f, this.getScaledHeight() - 14f, -1879048193);
        this.exitButton.drawElement(f, f2, true);
        if (!(mc.currentScreen instanceof GuiCosmetics)) this.languageButton.drawElement(f, f2, true);
        this.IlllIllIlIIIIlIIlIIllIIIl.drawElement(f, f2, true);
        this.optionsButton.drawElement(f, f2, true);
        this.cosmeticsButton.drawElement(f, f2, true);
    }

    @Override
    public void onMouseReleased(float mouseX, float mouseY, int mouseButton) {

    }

    @Override
    public void onMouseClicked(float mouseX, float mouseY, int button) {
        this.exitButton.handleElementMouseClicked(mouseX, mouseY, button, true);
        this.IlllIllIlIIIIlIIlIIllIIIl.handleElementMouseClicked(mouseX, mouseY, button, true);
        if (this.exitButton.isMouseInside(mouseX, mouseY)) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            this.mc.shutdown();
        } else if (this.optionsButton.isMouseInside(mouseX, mouseY)) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        } else if (this.languageButton.isMouseInside(mouseX, mouseY)) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        } else if (this.cosmeticsButton.isMouseInside(mouseX, mouseY)) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            this.mc.displayGuiScreen(new GuiCosmetics());
        } else {
            boolean bl;
            boolean bl2 = bl = mouseX < this.optionsButton.getX() && mouseY < (float) 30;
            if (bl && !(this.mc.currentScreen instanceof MainMenu)) {
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                this.mc.displayGuiScreen(new MainMenu());
            }
        }
    }

    private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
        Tessellator var4 = Tessellator.instance;
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        byte var5 = 8;

        for (int var6 = 0; var6 < var5 * var5; ++var6) {
            GL11.glPushMatrix();
            float var7 = ((float) (var6 % var5) / (float) var5 - 0.5F) / 64.0F;
            float var8 = ((float) (var6 / var5) / (float) var5 - 0.5F) / 64.0F;
            float var9 = 0.0F;
            GL11.glTranslatef(var7, var8, var9);
            GL11.glRotatef(MathHelper.sin(((float) IIIllIllIlIlllllllIlIlIII + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-((float) IIIllIllIlIlllllllIlIlIII + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int var10 = 0; var10 < 6; ++var10) {
                GL11.glPushMatrix();

                if (var10 == 1) {
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var10 == 2) {
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var10 == 3) {
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var10 == 4) {
                    GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (var10 == 5) {
                    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                this.mc.getTextureManager().bindTexture(IllIlIlIllllIlIIllllIIlll[var10]);
                var4.startDrawingQuads();
                var4.setColorRGBA_I(16777215, 255 / (var6 + 1));
                float var11 = 0.0F;
                var4.addVertexWithUV(-1.0D, -1.0D, 1.0D, 0.0F + var11, 0.0F + var11);
                var4.addVertexWithUV(1.0D, -1.0D, 1.0D, 1.0F - var11, 0.0F + var11);
                var4.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0F - var11, 1.0F - var11);
                var4.addVertexWithUV(-1.0D, 1.0D, 1.0D, 0.0F + var11, 1.0F - var11);
                var4.draw();
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
            GL11.glColorMask(true, true, true, false);
        }

        var4.setTranslation(0.0D, 0.0D, 0.0D);
        GL11.glColorMask(true, true, true, true);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Rotate and blurs the skybox view in the main menu
     */
    private void rotateAndBlurSkybox(float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(this.panoramaBackgroundLocation);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColorMask(true, true, true, false);
        Tessellator var2 = Tessellator.instance;
        var2.startDrawingQuads();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        byte var3 = 3;

        for (int var4 = 0; var4 < var3; ++var4) {
            var2.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float) (var4 + 1));
            int var5 = this.width;
            int var6 = this.height;
            float var7 = (float) (var4 - var3 / 2) / 256.0F;
            var2.addVertexWithUV(var5, var6, zLevel, 0.0F + var7, 1.0D);
            var2.addVertexWithUV(var5, 0.0D, zLevel, 1.0F + var7, 1.0D);
            var2.addVertexWithUV(0.0D, 0.0D, zLevel, 1.0F + var7, 0.0D);
            var2.addVertexWithUV(0.0D, var6, zLevel, 0.0F + var7, 0.0D);
        }

        var2.draw();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColorMask(true, true, true, true);
    }

    /**
     * Renders the skybox in the main menu
     */
    private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GL11.glViewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        Tessellator var4 = Tessellator.instance;
        var4.startDrawingQuads();
        float var5 = this.width > this.height ? 120.0F / (float) this.width : 120.0F / (float) this.height;
        float var6 = (float) this.height * var5 / 256.0F;
        float var7 = (float) this.width * var5 / 256.0F;
        var4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        int var8 = this.width;
        int var9 = this.height;
        var4.addVertexWithUV(0.0D, var9, zLevel, 0.5F - var6, 0.5F + var7);
        var4.addVertexWithUV(var8, var9, zLevel, 0.5F - var6, 0.5F - var7);
        var4.addVertexWithUV(var8, 0.0D, zLevel, 0.5F + var6, 0.5F - var7);
        var4.addVertexWithUV(0.0D, 0.0D, zLevel, 0.5F + var6, 0.5F + var7);
        var4.draw();
    }

    public void login(String string) {
        block21:
        {
            try {
                Session session;
                Account selectedAccount = null;
                for (Account account : this.accountsList) {
                    if (!account.getDisplayName().equals(string)) continue;
                    selectedAccount = account;
                }
                if (selectedAccount == null) break block21;
                if (selectedAccount.getUUID().equalsIgnoreCase(Minecraft.getMinecraft().getSession().getPlayerID())) {
                    return;
                }
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                for (Session object2 : CheatBreaker.getInstance().sessions) {
                    if (!object2.func_148256_e().getId().toString().replaceAll("-", "").equalsIgnoreCase(selectedAccount.getUUID().replaceAll("-", "")))
                        continue;
                    Minecraft.getMinecraft().setSession(object2);
                    this.IlllIllIlIIIIlIIlIIllIIIl.lIIIIlIIllIIlIIlIIIlIIllI(selectedAccount.getDisplayName());
                    this.IlllIllIlIIIIlIIlIIllIIIl.lIIIIlIIllIIlIIlIIIlIIllI(selectedAccount.getHeadLocation());
                    this.updateAccountButtonSize();
                    return;
                }
                YggdrasilAuthenticationService yggdrasilAuthenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, selectedAccount.getClientToken());
                YggdrasilUserAuthentication object2 = (YggdrasilUserAuthentication) yggdrasilAuthenticationService.createUserAuthentication(Agent.MINECRAFT);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("uuid", selectedAccount.getUUID());
                hashMap.put("displayName", selectedAccount.getDisplayName());
                hashMap.put("username", selectedAccount.getUsername());
                hashMap.put("accessToken", selectedAccount.getAccessToken());
                object2.loadFromStorage(hashMap);
                try {
                    object2.logIn();
                    session = new Session(object2.getSelectedProfile().getName(), object2.getSelectedProfile().getId().toString(), object2.getAuthenticatedToken(), "mojang");
                } catch (AuthenticationException authenticationException) {
                    authenticationException.printStackTrace();
                    return;
                }
                System.out.println("Updated accessToken and logged user in.");
                this.IlllIllIlIIIIlIIlIIllIIIl.lIIIIlIIllIIlIIlIIIlIIllI(selectedAccount.getDisplayName());
                this.IlllIllIlIIIIlIIlIIllIIIl.lIIIIlIIllIIlIIlIIIlIIllI(selectedAccount.getHeadLocation());
                this.updateAccountButtonSize();
                CheatBreaker.getInstance().sessions.add(session);
                Minecraft.getMinecraft().setSession(session);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public List<Account> getAccounts() {
        return this.accountsList;
    }
}

