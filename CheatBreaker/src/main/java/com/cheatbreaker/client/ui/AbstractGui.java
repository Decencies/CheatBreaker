package com.cheatbreaker.client.ui;

import com.cheatbreaker.client.ui.mainmenu.AbstractElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractGui extends GuiScreen {

    protected ScaledResolution resolution;
    protected float scaledWidth;
    protected float scaledHeight;
    protected List<AbstractElement> elements;
    protected int elementListSize = 0;

    @Override
    public void setWorldAndResolution(final Minecraft mc, final int displayWidth, final int displayHeight) {
        this.mc = mc;
        this.fontRendererObj = mc.fontRenderer;
        this.width = displayWidth;
        this.height = displayHeight;
        this.buttonList.clear();
        this.resolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        final float scaleFactor = getScaleFactor();
        this.scaledWidth = width / scaleFactor;
        this.scaledHeight = height / scaleFactor;
        this.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final float scaleFactor = getScaleFactor();
        GL11.glPushMatrix();
        GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
        drawMenu(mouseX / scaleFactor, mouseY / scaleFactor);
        GL11.glPopMatrix();
    }

    protected abstract void drawMenu(float mouseX, float mouseY);

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        final float scaleFactor = getScaleFactor();
        onMouseClicked(mouseX / scaleFactor, mouseY / scaleFactor, mouseButton);
    }

    protected abstract void onMouseClicked(float mouseX, float mouseY, int mouseButton);

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int mouseButton) {
        final float scaleFactor = getScaleFactor();
        onMouseReleased(mouseX / scaleFactor, mouseY / scaleFactor, mouseButton);
    }

    protected abstract void onMouseReleased(float mouseX, float mouseY, int mouseButton);

    protected void setElementsAndUpdateSize(AbstractElement... elements) {
        this.elements = new ArrayList<>();
        this.elements.addAll(Arrays.asList(elements));
        this.elementListSize = this.elements.size();
    }

    public void addElements(AbstractElement... elements) {
        this.elements.addAll(Arrays.asList(elements));
        this.initGui();
    }

    public void removeElements(AbstractElement... elements) {
        this.elements.removeAll(Arrays.asList(elements));
        this.initGui();
    }

    protected void handleMouse() {
        this.elements.forEach(AbstractElement::handleElementMouse);
    }

    protected void handleClose() {
        this.elements.forEach(AbstractElement::handleElementClose);
    }

    protected void updateElements() {
        this.elements.forEach(AbstractElement::handleElementUpdate);
    }

    protected void handleKeyTyped(char c, int n) {
        this.elements.forEach(element -> element.handleElementKeyTyped(c, n));
    }

    protected void drawElements(float f, float f2, AbstractElement... elements) {
        List<AbstractElement> list = Arrays.asList(elements);
        for (AbstractElement element : this.elements) {
            if (list.contains(element)) continue;
            element.drawElement(f, f2, this.isMouseHovered(element, f, f2));
        }
    }

    protected void handleMouseReleased(float f, float f2, int n) {
        AbstractElement element;
        Iterator<AbstractElement> iterator = this.elements.iterator();
        while (!(!iterator.hasNext() || (element = iterator.next()).isMouseInside(f, f2) && element.handleElementMouseRelease(f, f2, n, this.isMouseHovered(element, f, f2)))) {
        }
    }

    protected void onMouseClicked(final float n, final float n2, final int n3, final AbstractElement... array) {
        final List<AbstractElement> list = Arrays.asList(array);
        Object o = null;
        boolean b = false;
        for (final AbstractElement llllIIIIlIlIllIIIllllIIll : this.elements) {
            if (list.contains(llllIIIIlIlIllIIIllllIIll)) {
                continue;
            }
            if (!llllIIIIlIlIllIIIllllIIll.isMouseInside(n, n2)) {
                continue;
            }
            if (!this.elements.contains(llllIIIIlIlIllIIIllllIIll)) {
                o = llllIIIIlIlIllIIIllllIIll;
            }
            if (llllIIIIlIlIllIIIllllIIll.handleElementMouseClicked(n, n2, n3, this.isMouseHovered(llllIIIIlIlIllIIIllllIIll, n, n2, array))) {
                b = true;
                break;
            }
        }
        if (b) {
            return;
        }
        if (o != null) {
            this.elements.add(this.elements.remove(this.elements.indexOf(o)));
        }
        final Iterator<AbstractElement> iterator2 = this.elements.iterator();
        while (iterator2.hasNext() && !iterator2.next().handleMouseClickedInternal(n, n2, n3)) {
        }
    }

    protected boolean isMouseHovered(AbstractElement element, float f, float f2, AbstractElement... elements) {
        AbstractElement element2;
        List<AbstractElement> list = Arrays.asList(elements);
        boolean bl = true;
        for (int i = this.elements.size() - 1; i >= 0 && (element2 = this.elements.get(i)) != element; --i) {
            if (list.contains(element2) || !element2.isMouseInside(f, f2)) continue;
            bl = false;
            break;
        }
        return bl;
    }

    public float getScaleFactor() {
        float n;
        switch (resolution.getScaleFactor()) {
            case 1: {
                n = 0.5f;
                break;
            }
            case 3: {
                n = 1.5f;
                break;
            }
            case 4: {
                n = 2.0f;
                break;
            }
            default: {
                n = 1.0f;
                break;
            }
        }
        return 1.0f / n;
    }

    public List<AbstractElement> getElements() {
        return elements;
    }

    protected void setElements(AbstractElement... elements) {
        this.elements = new ArrayList<>();
        this.elements.addAll(Arrays.asList(elements));
    }

    public float getScaledWidth() {
        return scaledWidth;
    }

    public float getScaledHeight() {
        return scaledHeight;
    }

    public ScaledResolution getResolution() {
        return resolution;
    }
}
