package net.minecraft.client.audio;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class MusicTicker implements IUpdatePlayerListBox
{
    private final Random field_147679_a = new Random();
    private final Minecraft field_147677_b;
    private ISound field_147678_c;
    private int field_147676_d = 100;


    public MusicTicker(Minecraft p_i45112_1_)
    {
        this.field_147677_b = p_i45112_1_;
    }

    /**
     * Updates the JList with a new model.
     */
    public void update()
    {
        MusicTicker.MusicType var1 = this.field_147677_b.func_147109_W();

        if (this.field_147678_c != null)
        {
            if (!var1.func_148635_a().equals(this.field_147678_c.func_147650_b()))
            {
                this.field_147677_b.getSoundHandler().func_147683_b(this.field_147678_c);
                this.field_147676_d = MathHelper.getRandomIntegerInRange(this.field_147679_a, 0, var1.func_148634_b() / 2);
            }

            if (!this.field_147677_b.getSoundHandler().func_147692_c(this.field_147678_c))
            {
                this.field_147678_c = null;
                this.field_147676_d = Math.min(MathHelper.getRandomIntegerInRange(this.field_147679_a, var1.func_148634_b(), var1.func_148633_c()), this.field_147676_d);
            }
        }

        if (this.field_147678_c == null && this.field_147676_d-- <= 0)
        {
            this.field_147678_c = PositionedSoundRecord.func_147673_a(var1.func_148635_a());
            this.field_147677_b.getSoundHandler().playSound(this.field_147678_c);
            this.field_147676_d = Integer.MAX_VALUE;
        }
    }

    public static enum MusicType
    {
        MENU("MENU", 0, new ResourceLocation("minecraft:music.menu"), 20, 600),
        GAME("GAME", 1, new ResourceLocation("minecraft:music.game"), 12000, 24000),
        CREATIVE("CREATIVE", 2, new ResourceLocation("minecraft:music.game.creative"), 1200, 3600),
        CREDITS("CREDITS", 3, new ResourceLocation("minecraft:music.game.end.credits"), Integer.MAX_VALUE, Integer.MAX_VALUE),
        NETHER("NETHER", 4, new ResourceLocation("minecraft:music.game.nether"), 1200, 3600),
        END_BOSS("END_BOSS", 5, new ResourceLocation("minecraft:music.game.end.dragon"), 0, 0),
        END("END", 6, new ResourceLocation("minecraft:music.game.end"), 6000, 24000);
        private final ResourceLocation field_148645_h;
        private final int field_148646_i;
        private final int field_148643_j;

        private static final MusicTicker.MusicType[] $VALUES = new MusicTicker.MusicType[]{MENU, GAME, CREATIVE, CREDITS, NETHER, END_BOSS, END};


        private MusicType(String p_i45111_1_, int p_i45111_2_, ResourceLocation p_i45111_3_, int p_i45111_4_, int p_i45111_5_)
        {
            this.field_148645_h = p_i45111_3_;
            this.field_148646_i = p_i45111_4_;
            this.field_148643_j = p_i45111_5_;
        }

        public ResourceLocation func_148635_a()
        {
            return this.field_148645_h;
        }

        public int func_148634_b()
        {
            return this.field_148646_i;
        }

        public int func_148633_c()
        {
            return this.field_148643_j;
        }
    }
}
