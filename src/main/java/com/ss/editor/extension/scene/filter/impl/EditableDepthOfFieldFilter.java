package com.ss.editor.extension.scene.filter.impl;

import static com.ss.editor.extension.property.EditablePropertyType.FLOAT;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.util.clone.Cloner;
import com.ss.editor.extension.property.EditableProperty;
import com.ss.editor.extension.property.SimpleProperty;
import com.ss.editor.extension.scene.filter.EditableSceneFilter;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;

/**
 * The editable implementation of depth of field filter.
 *
 * @author JavaSaBr
 */
public class EditableDepthOfFieldFilter extends DepthOfFieldFilter implements EditableSceneFilter<DepthOfFieldFilter> {

    @Override
    public DepthOfFieldFilter get() {
        return this;
    }

    @NotNull
    @Override
    public String getName() {
        return "Depth of field filter";
    }

    @Override
    public Object jmeClone() {
        try {
            return super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    public Array<EditableProperty<?, ?>> getEditableProperties() {

        final Array<EditableProperty<?, ?>> result = ArrayFactory.newArray(EditableProperty.class);

        result.add(new SimpleProperty<>(FLOAT, "Blur scale", 0.01F, 0F, 100F, this,
                                        DepthOfFieldFilter::getBlurScale,
                                        DepthOfFieldFilter::setBlurScale));
        result.add(new SimpleProperty<>(FLOAT, "Focus distance", 1F, 0F, Integer.MAX_VALUE, this,
                                        DepthOfFieldFilter::getFocusDistance,
                                        DepthOfFieldFilter::setFocusDistance));
        result.add(new SimpleProperty<>(FLOAT, "Focus range", 1F, 0F, Integer.MAX_VALUE, this,
                                        DepthOfFieldFilter::getFocusRange,
                                        DepthOfFieldFilter::setFocusRange));

        return result;
    }

    @Override
    public void cloneFields(@NotNull final Cloner cloner, @NotNull final Object original) {
    }
}
