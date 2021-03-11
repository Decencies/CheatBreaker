package net.minecraft.client.gui;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiCreateFlatWorld extends GuiScreen
{
    private static RenderItem field_146392_a = new RenderItem();
    private final GuiCreateWorld field_146385_f;
    private FlatGeneratorInfo field_146387_g = FlatGeneratorInfo.getDefaultFlatGenerator();
    private String field_146393_h;
    private String field_146394_i;
    private String field_146391_r;
    private GuiCreateFlatWorld.Details field_146390_s;
    private GuiButton field_146389_t;
    private GuiButton field_146388_u;
    private GuiButton field_146386_v;


    public GuiCreateFlatWorld(GuiCreateWorld p_i1029_1_, String p_i1029_2_)
    {
        this.field_146385_f = p_i1029_1_;
        this.func_146383_a(p_i1029_2_);
    }

    public String func_146384_e()
    {
        return this.field_146387_g.toString();
    }

    public void func_146383_a(String p_146383_1_)
    {
        this.field_146387_g = FlatGeneratorInfo.createFlatGeneratorFromString(p_146383_1_);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.clear();
        this.field_146393_h = I18n.format("createWorld.customize.flat.title", new Object[0]);
        this.field_146394_i = I18n.format("createWorld.customize.flat.tile", new Object[0]);
        this.field_146391_r = I18n.format("createWorld.customize.flat.height", new Object[0]);
        this.field_146390_s = new GuiCreateFlatWorld.Details();
        this.buttonList.add(this.field_146389_t = new GuiButton(2, this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("createWorld.customize.flat.addLayer", new Object[0]) + " (NYI)"));
        this.buttonList.add(this.field_146388_u = new GuiButton(3, this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("createWorld.customize.flat.editLayer", new Object[0]) + " (NYI)"));
        this.buttonList.add(this.field_146386_v = new GuiButton(4, this.width / 2 - 155, this.height - 52, 150, 20, I18n.format("createWorld.customize.flat.removeLayer", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(5, this.width / 2 + 5, this.height - 52, 150, 20, I18n.format("createWorld.customize.presets", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.field_146389_t.field_146125_m = this.field_146388_u.field_146125_m = false;
        this.field_146387_g.func_82645_d();
        this.func_146375_g();
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        int var2 = this.field_146387_g.getFlatLayers().size() - this.field_146390_s.field_148228_k - 1;

        if (p_146284_1_.id == 1)
        {
            this.mc.displayGuiScreen(this.field_146385_f);
        }
        else if (p_146284_1_.id == 0)
        {
            this.field_146385_f.field_146334_a = this.func_146384_e();
            this.mc.displayGuiScreen(this.field_146385_f);
        }
        else if (p_146284_1_.id == 5)
        {
            this.mc.displayGuiScreen(new GuiFlatPresets(this));
        }
        else if (p_146284_1_.id == 4 && this.func_146382_i())
        {
            this.field_146387_g.getFlatLayers().remove(var2);
            this.field_146390_s.field_148228_k = Math.min(this.field_146390_s.field_148228_k, this.field_146387_g.getFlatLayers().size() - 1);
        }

        this.field_146387_g.func_82645_d();
        this.func_146375_g();
    }

    public void func_146375_g()
    {
        boolean var1 = this.func_146382_i();
        this.field_146386_v.enabled = var1;
        this.field_146388_u.enabled = var1;
        this.field_146388_u.enabled = false;
        this.field_146389_t.enabled = false;
    }

    private boolean func_146382_i()
    {
        return this.field_146390_s.field_148228_k > -1 && this.field_146390_s.field_148228_k < this.field_146387_g.getFlatLayers().size();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawDefaultBackground();
        this.field_146390_s.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
        this.drawCenteredString(this.fontRendererObj, this.field_146393_h, this.width / 2, 8, 16777215);
        int var4 = this.width / 2 - 92 - 16;
        this.drawString(this.fontRendererObj, this.field_146394_i, var4, 32, 16777215);
        this.drawString(this.fontRendererObj, this.field_146391_r, var4 + 2 + 213 - this.fontRendererObj.getStringWidth(this.field_146391_r), 32, 16777215);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    class Details extends GuiSlot
    {
        public int field_148228_k = -1;


        public Details()
        {
            super(GuiCreateFlatWorld.this.mc, GuiCreateFlatWorld.this.width, GuiCreateFlatWorld.this.height, 43, GuiCreateFlatWorld.this.height - 60, 24);
        }

        private void func_148225_a(int p_148225_1_, int p_148225_2_, ItemStack p_148225_3_)
        {
            this.func_148226_e(p_148225_1_ + 1, p_148225_2_ + 1);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);

            if (p_148225_3_ != null)
            {
                RenderHelper.enableGUIStandardItemLighting();
                GuiCreateFlatWorld.field_146392_a.renderItemIntoGUI(GuiCreateFlatWorld.this.fontRendererObj, GuiCreateFlatWorld.this.mc.getTextureManager(), p_148225_3_, p_148225_1_ + 2, p_148225_2_ + 2);
                RenderHelper.disableStandardItemLighting();
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }

        private void func_148226_e(int p_148226_1_, int p_148226_2_)
        {
            this.func_148224_c(p_148226_1_, p_148226_2_, 0, 0);
        }

        private void func_148224_c(int p_148224_1_, int p_148224_2_, int p_148224_3_, int p_148224_4_)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GuiCreateFlatWorld.this.mc.getTextureManager().bindTexture(Gui.statIcons);
            float var5 = 0.0078125F;
            float var6 = 0.0078125F;
            boolean var7 = true;
            boolean var8 = true;
            Tessellator var9 = Tessellator.instance;
            var9.startDrawingQuads();
            var9.addVertexWithUV((double)(p_148224_1_ + 0), (double)(p_148224_2_ + 18), (double)GuiCreateFlatWorld.this.zLevel, (double)((float)(p_148224_3_ + 0) * 0.0078125F), (double)((float)(p_148224_4_ + 18) * 0.0078125F));
            var9.addVertexWithUV((double)(p_148224_1_ + 18), (double)(p_148224_2_ + 18), (double)GuiCreateFlatWorld.this.zLevel, (double)((float)(p_148224_3_ + 18) * 0.0078125F), (double)((float)(p_148224_4_ + 18) * 0.0078125F));
            var9.addVertexWithUV((double)(p_148224_1_ + 18), (double)(p_148224_2_ + 0), (double)GuiCreateFlatWorld.this.zLevel, (double)((float)(p_148224_3_ + 18) * 0.0078125F), (double)((float)(p_148224_4_ + 0) * 0.0078125F));
            var9.addVertexWithUV((double)(p_148224_1_ + 0), (double)(p_148224_2_ + 0), (double)GuiCreateFlatWorld.this.zLevel, (double)((float)(p_148224_3_ + 0) * 0.0078125F), (double)((float)(p_148224_4_ + 0) * 0.0078125F));
            var9.draw();
        }

        protected int getSize()
        {
            return GuiCreateFlatWorld.this.field_146387_g.getFlatLayers().size();
        }

        protected void elementClicked(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_)
        {
            this.field_148228_k = p_148144_1_;
            GuiCreateFlatWorld.this.func_146375_g();
        }

        protected boolean isSelected(int p_148131_1_)
        {
            return p_148131_1_ == this.field_148228_k;
        }

        protected void drawBackground() {}

        protected void drawSlot(int p_148126_1_, int p_148126_2_, int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_)
        {
            FlatLayerInfo var8 = (FlatLayerInfo)GuiCreateFlatWorld.this.field_146387_g.getFlatLayers().get(GuiCreateFlatWorld.this.field_146387_g.getFlatLayers().size() - p_148126_1_ - 1);
            Item var9 = Item.getItemFromBlock(var8.func_151536_b());
            ItemStack var10 = var8.func_151536_b() == Blocks.air ? null : new ItemStack(var9, 1, var8.getFillBlockMeta());
            String var11 = var10 != null && var9 != null ? var9.getItemStackDisplayName(var10) : "Air";
            this.func_148225_a(p_148126_2_, p_148126_3_, var10);
            GuiCreateFlatWorld.this.fontRendererObj.drawString(var11, p_148126_2_ + 18 + 5, p_148126_3_ + 3, 16777215);
            String var12;

            if (p_148126_1_ == 0)
            {
                var12 = I18n.format("createWorld.customize.flat.layer.top", new Object[] {Integer.valueOf(var8.getLayerCount())});
            }
            else if (p_148126_1_ == GuiCreateFlatWorld.this.field_146387_g.getFlatLayers().size() - 1)
            {
                var12 = I18n.format("createWorld.customize.flat.layer.bottom", new Object[] {Integer.valueOf(var8.getLayerCount())});
            }
            else
            {
                var12 = I18n.format("createWorld.customize.flat.layer", new Object[] {Integer.valueOf(var8.getLayerCount())});
            }

            GuiCreateFlatWorld.this.fontRendererObj.drawString(var12, p_148126_2_ + 2 + 213 - GuiCreateFlatWorld.this.fontRendererObj.getStringWidth(var12), p_148126_3_ + 3, 16777215);
        }

        protected int func_148137_d()
        {
            return this.field_148155_a - 70;
        }
    }
}
