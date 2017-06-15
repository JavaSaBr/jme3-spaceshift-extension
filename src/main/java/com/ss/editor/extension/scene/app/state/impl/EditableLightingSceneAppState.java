package com.ss.editor.extension.scene.app.state.impl;

import com.simsilica.fx.LightingState;
import com.ss.editor.extension.property.EditableProperty;
import com.ss.editor.extension.property.EditablePropertyType;
import com.ss.editor.extension.scene.app.state.EditableSceneAppState;
import com.ss.editor.extension.property.SimpleProperty;
import org.jetbrains.annotations.NotNull;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;

/**
 * The editable version of lighting state.
 *
 * @author JavaSaBr
 */
public class EditableLightingSceneAppState extends LightingState implements EditableSceneAppState {

    public EditableLightingSceneAppState() {
        super();
    }

    @NotNull
    @Override
    public String getName() {
        return "Lighting State";
    }

    @NotNull
    @Override
    public Array<EditableProperty<?, ?>> getEditableProperties() {

        final Array<EditableProperty<?, ?>> result = ArrayFactory.newArray(EditableProperty.class);

        result.add(new SimpleProperty<>(EditablePropertyType.COLOR, "Ambient color", this,
                                        LightingState::getAmbientColor,
                                        LightingState::setAmbientColor));
        result.add(new SimpleProperty<>(EditablePropertyType.COLOR, "Sun color", this,
                                        LightingState::getSunColor,
                                        LightingState::setSunColor));
        result.add(new SimpleProperty<>(EditablePropertyType.FLOAT, "Time of day", 0.1F, -0.300F, 1.300F, this,
                                        LightingState::getTimeOfDay,
                                        LightingState::setTimeOfDay));
        result.add(new SimpleProperty<>(EditablePropertyType.FLOAT, "Orientation", 0.1F, 0F, 6.283F, this,
                                        LightingState::getOrientation,
                                        LightingState::setOrientation));

        return result;
    }
}
