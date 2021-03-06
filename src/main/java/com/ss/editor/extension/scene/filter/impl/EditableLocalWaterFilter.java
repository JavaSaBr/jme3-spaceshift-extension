package com.ss.editor.extension.scene.filter.impl;

import static com.ss.editor.extension.property.EditablePropertyType.FLOAT;
import static com.ss.editor.extension.property.EditablePropertyType.VECTOR_3F;
import static com.ss.editor.extension.property.ReflectionGetterSetterFactory.makeGetter;
import static com.ss.editor.extension.property.ReflectionGetterSetterFactory.makeSetter;
import static java.lang.Math.max;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.ss.editor.extension.property.EditableProperty;
import com.ss.editor.extension.property.SimpleProperty;
import com.ss.editor.extension.scene.ScenePresentable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The editable implementation of local water filter.
 *
 * @author JavaSaBr
 */
public class EditableLocalWaterFilter extends EditableWaterFilter implements ScenePresentable {

    public EditableLocalWaterFilter() {
        setCenter(new Vector3f());
        setRadius(10F);
        setShapeType(AreaShape.Circular);
    }

    @Override
    public @NotNull Vector3f getLocation() {
        return getCenter();
    }

    @Override
    public @NotNull Quaternion getRotation() {
        return Quaternion.IDENTITY;
    }

    @Override
    public @NotNull Vector3f getScale() {
        return new Vector3f(getRadius(), getRadius(), getRadius());
    }

    @Override
    public void setLocation(@NotNull Vector3f location) {
        setCenter(location.clone());
        setWaterHeight(location.getY());
    }

    @Override
    public void setScale(@NotNull Vector3f scale) {
        setRadius(max(max(scale.getX(), scale.getY()), scale.getZ()));
    }

    @Override
    public void setRotation(@NotNull Quaternion rotation) {

    }

    @Override
    public @NotNull ScenePresentable.PresentationType getPresentationType() {
        return PresentationType.SPHERE;
    }

    @Override
    public @NotNull String getName() {
        return "Local water filter";
    }

    @Override
    public @NotNull List<EditableProperty<?, ?>> getEditableProperties() {

        final List<EditableProperty<?, ?>> properties = super.getEditableProperties();
        properties.add(new SimpleProperty<>(VECTOR_3F, "Center", this,
                makeGetter(this, Vector3f.class, "getCenter"),
                makeSetter(this, Vector3f.class, "setCenter")));
        properties.add(new SimpleProperty<>(FLOAT, "Radius", this,
                makeGetter(this, float.class, "getRadius"),
                makeSetter(this, float.class, "setRadius")));

        return properties;
    }
}
