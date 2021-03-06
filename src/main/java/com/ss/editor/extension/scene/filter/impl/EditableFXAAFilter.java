package com.ss.editor.extension.scene.filter.impl;

import static com.ss.editor.extension.property.EditablePropertyType.FLOAT;
import static com.ss.editor.extension.property.ReflectionGetterSetterFactory.makeGetter;
import static com.ss.editor.extension.property.ReflectionGetterSetterFactory.makeSetter;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.util.clone.Cloner;
import com.ss.editor.extension.property.EditableProperty;
import com.ss.editor.extension.property.SimpleProperty;
import com.ss.editor.extension.scene.app.state.SceneAppState;
import com.ss.editor.extension.scene.filter.EditableSceneFilter;
import com.ss.editor.extension.scene.filter.SceneFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The editable implementation of FXAA filter.
 *
 * @author JavaSaBr
 */
public class EditableFXAAFilter extends FXAAFilter implements EditableSceneFilter {

    @Override
    public @NotNull FXAAFilter get() {
        return this;
    }

    @Override
    public @NotNull String getName() {
        return "FXAA filter";
    }

    @Override
    public Object jmeClone() {
        try {
            return super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull List<EditableProperty<?, ?>> getEditableProperties() {

        List<EditableProperty<?, ?>> result = new ArrayList<>(4);

        result.add(new SimpleProperty<>(FLOAT, "Sub pixel shift", 0.005F, 0F, 10F, this,
                makeGetter(this, float.class, "getSubPixelShift"),
                makeSetter(this, float.class, "setSubPixelShift")));
        result.add(new SimpleProperty<>(FLOAT, "VX offset", 0.005F, 0F, 10F, this,
                makeGetter(this, float.class, "getVxOffset"),
                makeSetter(this, float.class, "setVxOffset")));
        result.add(new SimpleProperty<>(FLOAT, "Span max", 1F, 0F, 100F, this,
                makeGetter(this, float.class, "getSpanMax"),
                makeSetter(this, float.class, "setSpanMax")));
        result.add(new SimpleProperty<>(FLOAT, "Reduce mul", 0.1F, 0F, 100F, this,
                makeGetter(this, float.class, "getReduceMul"),
                makeSetter(this, float.class, "setReduceMul")));

        return result;
    }

    @Override
    public void cloneFields(@NotNull Cloner cloner, @NotNull Object original) {
    }

    @Override
    public void read(@NotNull JmeImporter importer) throws IOException {
        super.read(importer);
        InputCapsule capsule = importer.getCapsule(this);
        setSubPixelShift(capsule.readFloat("subPixelShift", 1.0f / 4.0f));
        setVxOffset(capsule.readFloat("vxOffset", 0.0f));
        setSpanMax(capsule.readFloat("spanMax", 8.0f));
        setReduceMul(capsule.readFloat("reduceMul", 1.0f / 8.0f));
    }

    @Override
    public void write(@NotNull JmeExporter exporter) throws IOException {
        super.write(exporter);
        OutputCapsule capsule = exporter.getCapsule(this);
        capsule.write(getSubPixelShift(), "subPixelShift", 1.0f / 4.0f);
        capsule.write(getVxOffset(), "vxOffset", 0.0f);
        capsule.write(getSpanMax(), "spanMax", 8.0f);
        capsule.write(getReduceMul(), "reduceMul", 1.0f / 8.0f);
    }

    @Override
    public @Nullable String checkStates(@NotNull List<SceneAppState> exists) {
        return null;
    }

    @Override
    public @Nullable String checkFilters(@NotNull List<SceneFilter> exists) {
        return null;
    }
}
