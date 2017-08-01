package com.ss.editor.extension.property;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The simple implementation of editable property to a generic object.
 *
 * @param <T> the property type.
 * @param <O> the edited object type.
 * @author JavaSabr
 */
public class SimpleProperty<T, O> implements EditableProperty<T, O> {

    /**
     * The type of this property.
     */
    @NotNull
    private final EditablePropertyType type;

    /**
     * The name of this property.
     */
    @NotNull
    private final String name;

    /**
     * The getter of this property.
     */
    @NotNull
    private final Getter<O, T> getter;

    /**
     * The setter of this property.
     */
    @NotNull
    private final Setter<O, T> setter;

    /**
     * The edited object.
     */
    @NotNull
    private final O object;

    /**
     * The scroll power.
     */
    private float scrollPower;

    /**
     * The min value.
     */
    private float minValue;

    /**
     * The max value.
     */
    private float maxValue;

    public SimpleProperty(@NotNull final EditablePropertyType type, @NotNull final String name, @NotNull final O object,
                          @NotNull final Getter<O, T> getter, @NotNull final Setter<O, T> setter) {
        this(type, name, 1F, Integer.MIN_VALUE, Integer.MAX_VALUE, object, getter, setter);
    }

    public SimpleProperty(@NotNull final EditablePropertyType type, @NotNull final String name, final float scrollPower,
                          @NotNull final O object, @NotNull final Getter<O, T> getter,
                          @NotNull final Setter<O, T> setter) {
        this(type, name, scrollPower, Integer.MIN_VALUE, Integer.MAX_VALUE, object, getter, setter);
    }

    public SimpleProperty(@NotNull final EditablePropertyType type, @NotNull final String name, final float scrollPower,
                          final float minValue, final float maxValue, @NotNull final O object,
                          @NotNull final Getter<O, T> getter, @NotNull final Setter<O, T> setter) {
        this.type = type;
        this.name = name;
        this.object = object;
        this.getter = getter;
        this.setter = setter;
        this.scrollPower = scrollPower;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public float getScrollPower() {
        return scrollPower;
    }

    @Override
    public float getMaxValue() {
        return maxValue;
    }

    @Override
    public float getMinValue() {
        return minValue;
    }

    @NotNull
    @Override
    public EditablePropertyType getType() {
        return type;
    }

    @NotNull
    @Override
    public O getObject() {
        return object;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    @Nullable
    public T getValue() {
        return getter.get(object);
    }

    @Override
    public void setValue(@Nullable final T value) {
        setter.set(object, value);
    }

    @Override
    public String toString() {
        return "SimpleProperty{" + "type=" + type + ", name='" + name + '\'' + ", getter=" + getter + ", setter=" +
                setter + ", object=" + object + ", scrollPower=" + scrollPower + ", minValue=" + minValue +
                ", maxValue=" + maxValue + '}';
    }
}
