package com.ss.editor.extension.scene.control;

import com.jme3.scene.control.Control;
import com.ss.editor.extension.property.EditableProperty;
import org.jetbrains.annotations.NotNull;
import com.ss.rlib.util.HasName;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;

/**
 * The interface to implement an editable control.
 *
 * @author JavaSaBr
 */
public interface EditableControl extends Control, HasName {

    @NotNull
    Array<EditableProperty<?, ?>> EMPTY_PROPERTIES = ArrayFactory.newArray(EditableProperty.class);

    /**
     * @return the control's name.
     */
    @NotNull
    default String getName() {
        return getClass().getSimpleName();
    }

    /**
     * Get list of editable properties.
     *
     * @return the list of editable properties.
     */
    @NotNull
    default Array<EditableProperty<?, ?>> getEditableProperties() {
        return EMPTY_PROPERTIES;
    }
}
