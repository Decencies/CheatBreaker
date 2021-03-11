package com.cheatbreaker.client.ui.overlay.element;

import com.cheatbreaker.client.ui.mainmenu.AbstractElement;
import com.sun.javafx.geom.Vec2d;
import java.util.concurrent.atomic.AtomicBoolean;
import org.lwjgl.input.Mouse;

public abstract class DraggableElement extends AbstractElement {
    private final Vec2d position = new Vec2d();
    private final AtomicBoolean dragging = new AtomicBoolean();

    protected void drag(float mouseX, float mouseY) {
        if (this.dragging.get()) {
            if (!Mouse.isButtonDown(0)) {
                this.dragging.set(false);
                return;
            }
            double newX = (double)mouseX - this.position.x;
            double newY = (double)mouseY - this.position.y;
            this.setElementSize((float)newX, (float)newY, this.width, this.height);
        }
    }

    protected void updateDraggingPosition(float mouseX, float mouseY) {
        this.position.set(mouseX - this.x, mouseY - this.y);
        this.dragging.set(true);
    }

    protected void IllIIIIIIIlIlIllllIIllIII() {
        if (this.dragging.get()) {
            this.dragging.set(false);
        }
    }
}

