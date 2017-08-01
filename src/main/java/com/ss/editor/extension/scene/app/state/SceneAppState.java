package com.ss.editor.extension.scene.app.state;

import com.jme3.app.state.AppState;
import com.jme3.export.Savable;
import com.jme3.util.clone.JmeCloneable;
import com.ss.editor.extension.Named;
import com.ss.editor.extension.scene.SceneNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface to implement a scene app state.
 *
 * @author JavaSaBr
 */
public interface SceneAppState extends AppState, Savable, Named, Cloneable, JmeCloneable {

    /**
     * Set a scene node which is owner of this app state.
     *
     * @param sceneNode the scene node or null.
     */
    void setSceneNode(@Nullable final SceneNode sceneNode);

    /**
     * Notify a scene node about added an object to a scene node.
     *
     * @param object the added object.
     */
    void notifyAdded(@NotNull final Object object);

    /**
     * Notify a scene node about removed an object from a scene node.
     *
     * @param object the removed object.
     */
    void notifyRemoved(@NotNull final Object object);
}

