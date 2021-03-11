package com.cheatbreaker.client.ui.overlay.element;

import com.cheatbreaker.client.ui.mainmenu.AbstractElement;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ElementListElement<T extends AbstractElement> extends AbstractElement {
    protected final List<T> elements = new CopyOnWriteArrayList<>();

    public ElementListElement(List<T> list) {
        this.elements.addAll(list);
    }

    @Override
    public void handleElementDraw(float f, float f2, boolean bl) {
        this.elements.forEach(element -> element.drawElement(f, f2, bl));
    }

    @Override
    public void handleElementClose() {
        this.elements.forEach(AbstractElement::handleElementClose);
    }

    @Override
    public void handleElementUpdate() {
        this.elements.forEach(AbstractElement::handleElementUpdate);
    }

    @Override
    public void handleElementKeyTyped(char c, int n) {
        this.elements.forEach(element -> element.handleElementKeyTyped(c, n));
    }

    @Override
    public boolean handleElementMouseClicked(float f, float f2, int n, boolean bl) {
        if (!bl) {
            return false;
        }
        boolean bl2 = false;
        for (AbstractElement element : this.elements) {
            if (bl2) break;
            bl2 = element.handleElementMouseClicked(f, f2, n, true);
        }
        return bl2;
    }

    @Override
    public boolean handleElementMouseRelease(float f, float f2, int n, boolean bl) {
        if (!bl) {
            return false;
        }
        boolean bl2 = false;
        for (AbstractElement element : this.elements) {
            if (bl2) break;
            bl2 = element.handleElementMouseRelease(f, f2, n, true);
        }
        return bl2;
    }

    public List<T> getElements() {
        return this.elements;
    }
}

