package com.cheatbreaker.client.config;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.module.AbstractModule;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Ported from CheatBreaker b302ec0/master
 * @author Decencies
 */
public class Setting {

    private final String label;
    private Object value;
    private Number minimumValue;
    private Number maximumValue;
    private Setting parent;
    private String[] acceptedValues;
    private Consumer<Object> valueConsumer;
    private AbstractModule container;
    public boolean rainbow;
    public int[] colorArray;

    public Setting(String label) {
        if (label.isEmpty()) throw new IllegalStateException("Label is empty.");
        this.label = label;
    }

    public Setting(AbstractModule container, String label) {
        this(container.getSettingsList(), label);
        this.container = container;
    }

    public Setting(List<Setting> list, String label) {
        this(label);
        list.add(this);
    }

    public int getColorValue() {
        if (this.rainbow) {
            // strip the alpha channel from the color.
            int stripped = (Integer) this.value >> 24 & 0xFF;
            float hue = (float) System.nanoTime() / (1.0E10f) % 1.0f;
            return stripped << 24 | Color.HSBtoRGB(hue, 1.0f, 1.0f) & 0xFFFFFF;
        }
        return (Integer) this.value;
    }

    public Number getMinimumValue() {
        return this.minimumValue;
    }

    public Number getMaximumValue() {
        return this.maximumValue;
    }

    public Object getValue() {
        return this.value;
    }

    public Setting setValue(Object object) {
        return this.setValue(object, true);
    }

    public Setting getParent() {
        return this.parent;
    }

    public Setting setParent(Setting parent) {
        if (parent.getType() != Type.BOOLEAN) {
            throw new IllegalStateException("Parent can only be boolean.");
        }
        this.parent = parent;
        return this;
    }

    public String getLabel() {
        return this.label;
    }

    public String[] getAcceptedValues() {
        return this.acceptedValues;
    }

    public boolean getParentValue() {
        return this.parent != null && (Boolean) this.parent.getValue();
    }

    public Setting setValue(Object object, boolean createProfile) {
        if (CheatBreaker.getInstance().activeProfile != null && !CheatBreaker.getInstance().activeProfile.isEditable()) {
            if (createProfile) {
                CheatBreaker.getInstance().createNewProfile();
            }
        } else if (this.container != null) {
            this.container.getDefaultSettingsValues().add(object);
        }
        this.value = object;
        if (this.valueConsumer != null) {
            this.valueConsumer.accept(object);
        }
        return this;
    }

    public Setting acceptedValues(String... valueArray) {
        this.acceptedValues = valueArray;
        return this;
    }

    public Setting onChange(Consumer<Object> consumer) {
        this.valueConsumer = consumer;
        return this;
    }

    public Setting setMinMax(Number min, Number max) {
        this.minimumValue = min;
        this.maximumValue = max;
        return this;
    }

    public Type getType() {
        if (this.value.getClass().isAssignableFrom(Boolean.class)) {
            return Type.BOOLEAN;
        }
        if (this.value.getClass().isAssignableFrom(String.class)) {
            if (this.acceptedValues == null || this.acceptedValues.length == 0) {
                return Type.STRING;
            }
            return Type.STRING_ARRAY;
        }
        if (this.value.getClass().isAssignableFrom(Float.class)) {
            return Type.FLOAT;
        }
        if (this.value.getClass().isAssignableFrom(Double.class)) {
            return Type.DOUBLE;
        }
        if (this.value.getClass().isAssignableFrom(String[].class)) {
            return Type.STRING_ARRAY;
        }
        if (this.value.getClass().isAssignableFrom(Integer.class)) {
            return Type.INTEGER;
        }
        return null;
    }

    public enum Type {
        STRING,
        STRING_ARRAY,
        FLOAT,
        INTEGER,
        DOUBLE,
        BOOLEAN
    }
}
