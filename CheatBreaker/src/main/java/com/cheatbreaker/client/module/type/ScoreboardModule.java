package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.event.type.GuiDrawEvent;
import com.cheatbreaker.client.event.type.RenderPreviewEvent;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.module.ModuleRule;
import com.cheatbreaker.client.ui.module.CBGuiAnchor;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.scoreboard.*;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.util.Collection;
import java.util.Iterator;

public class ScoreboardModule extends AbstractModule {
    public static ModuleRule rule = ModuleRule.SCOREBOARD;
    public Setting removeNumbers;

    public ScoreboardModule() {
        super("Scoreboard");
        this.setDefaultAnchor(CBGuiAnchor.RIGHT_MIDDLE);
        this.removeNumbers = new Setting(this, "Remove Scoreboard numbers").setValue(true);
        this.addEvent(GuiDrawEvent.class, this::renderReal);
        this.addEvent(RenderPreviewEvent.class, this::renderPreview);
        this.setDefaultState(true);
    }

    private void renderPreview(RenderPreviewEvent renderPreviewEvent) {
        if (!this.isRenderHud()) {
            return;
        }
        if (this.minecraft.theWorld.getScoreboard().func_96539_a(1) != null) {
            return;
        }
        GL11.glPushMatrix();
        this.scaleAndTranslate(renderPreviewEvent.getResolution());
        GL11.glTranslatef(this.isRemoveNumbers() ? (float) -12 : 2.0f, this.height, 0.0f);
        Scoreboard scoreboard = new Scoreboard();
        ScoreObjective objective = new ScoreObjective(scoreboard, "CheatBreaker", IScoreObjectiveCriteria.field_96641_b);
        objective.setDisplayName(EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + "Cheat" + EnumChatFormatting.RESET + "" + EnumChatFormatting.WHITE + "Breaker");
        scoreboard.func_96529_a("Steve", objective);
        scoreboard.func_96529_a("Alex", objective);
        this.drawObjective(objective, this.minecraft.fontRenderer);
        GL11.glPopMatrix();
    }

    private void renderReal(GuiDrawEvent guiDrawEvent) {
        if (!this.isRenderHud()) {
            return;
        }
        GL11.glPushMatrix();
        this.scaleAndTranslate(guiDrawEvent.getResolution());
        GL11.glTranslatef(this.isRemoveNumbers() ? (float) -12 : 2.0f, this.height, 0.0f);
        ScoreObjective objective = this.minecraft.theWorld.getScoreboard().func_96539_a(1);
        if (objective != null) {
            this.drawObjective(objective, this.minecraft.fontRenderer);
        }
        GL11.glPopMatrix();
    }

    private void drawObjective(ScoreObjective objective, FontRenderer fontRenderer) {
        Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> collection = scoreboard.func_96534_i(objective);
        boolean removeNumbers = isRemoveNumbers();
        if (collection.size() <= 15) {
            int width = fontRenderer.getStringWidth(objective.getDisplayName());
            int numbersX = width + 16;
            for (Score score : collection) {
                ScorePlayerTeam playersTeam = scoreboard.getPlayersTeam(score.getPlayerName());
                String string = ScorePlayerTeam.formatPlayerName(playersTeam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
                width = Math.max(width, fontRenderer.getStringWidth(string));
            }
            int n8 = 0;
            Iterator<Score> iterator = collection.iterator();
            int n9 = 0;
            while (iterator.hasNext()) {
                Score score = iterator.next();
                Team team = scoreboard.getPlayersTeam(score.getPlayerName());
                String string = ScorePlayerTeam.formatPlayerName(team, score.getPlayerName());
                String string2 = EnumChatFormatting.RED + "" + score.getScorePoints();
                int lineY = -++n8 * fontRenderer.FONT_HEIGHT;
                int lineX = width + 9;
                if (lineX < numbersX) {
                    lineX = numbersX;
                }
                Gui.drawRect(-2 + (removeNumbers ? 14 : 0), lineY, lineX, lineY + fontRenderer.FONT_HEIGHT, 0x50000000);
                n9 = lineX - (-2 + (removeNumbers ? 14 : 0));
                fontRenderer.drawString(string, (removeNumbers ? 16 : 0), lineY, 0x20FFFFFF);
                if (!removeNumbers) {
                    fontRenderer.drawString(string2, lineX - fontRenderer.getStringWidth(string2) - 2, lineY, 0x20FFFFFF);
                }
                if (n8 != collection.size()) continue;
                String string3 = objective.getDisplayName();
                Gui.drawRect(-2 + (removeNumbers ? 14 : 0), lineY - fontRenderer.FONT_HEIGHT - 1, lineX, lineY - 1, 0x60000000);
                Gui.drawRect(-2 + (removeNumbers ? 14 : 0), lineY - 1, lineX, lineY, 0x50000000);
                fontRenderer.drawString(string3, +width / 2 - fontRenderer.getStringWidth(string3) / 2 + (removeNumbers ? 14 : 0), lineY - fontRenderer.FONT_HEIGHT, 0x20FFFFFF);
            }
            this.setDimensions(n9, collection.size() * fontRenderer.FONT_HEIGHT + 12);
        }
    }

    private boolean isRemoveNumbers() {
        return rule == ModuleRule.SCOREBOARD ? (Boolean) this.removeNumbers.getValue() : false;
    }
}
