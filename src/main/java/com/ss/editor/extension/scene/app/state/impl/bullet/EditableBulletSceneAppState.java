package com.ss.editor.extension.scene.app.state.impl.bullet;

import static com.ss.editor.extension.property.EditablePropertyType.*;
import static com.ss.editor.extension.property.ReflectionGetterSetterFactory.makeGetter;
import static com.ss.editor.extension.property.ReflectionGetterSetterFactory.makeSetter;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState.ThreadingType;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsSpace.BroadphaseType;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.util.clone.Cloner;
import com.ss.editor.extension.property.EditableProperty;
import com.ss.editor.extension.property.SimpleProperty;
import com.ss.editor.extension.scene.SceneNode;
import com.ss.editor.extension.scene.app.state.EditableSceneAppState;
import com.ss.editor.extension.scene.app.state.SceneAppState;
import com.ss.editor.extension.scene.app.state.impl.bullet.debug.BulletDebugAppState;
import com.ss.editor.extension.scene.filter.SceneFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * The implementation of an editable bullet state.
 *
 * @author JavaSaBr
 */
public class EditableBulletSceneAppState extends AbstractAppState implements EditableSceneAppState,
        PhysicsTickListener {

    /**
     * The Physics update task.
     */
    protected final Callable<Boolean> physicsUpdateTask = new Callable<Boolean>() {

        @Override
        public Boolean call() throws Exception {
            final PhysicsSpace physicsSpace = getPhysicsSpace();
            if (physicsSpace == null) return false;
            physicsSpace.update(getTpf() * getSpeed());
            return true;
        }
    };

    /***
     * The physics space.
     */
    @Nullable
    protected volatile PhysicsSpace physicsSpace;

    /**
     * The time per frame.
     */
    protected volatile float tpf;

    /**
     * The executor.
     */
    @Nullable
    protected ScheduledExecutorService executor;

    /**
     * The state manager.
     */
    @Nullable
    protected AppStateManager stateManager;

    /**
     * The application.
     */
    @Nullable
    protected Application application;

    /**
     * The debug state.
     */
    @Nullable
    protected BulletDebugAppState debugAppState;

    /**
     * The scene node.
     */
    @Nullable
    protected SceneNode sceneNode;

    /**
     * The threading type.
     */
    @NotNull
    protected ThreadingType threadingType;

    /**
     * The prev threading type.
     */
    @Nullable
    protected ThreadingType prevThreadingType;

    /**
     * The broadphase type.
     */
    @NotNull
    protected BroadphaseType broadphaseType;

    /**
     * The world min.
     */
    @NotNull
    protected Vector3f worldMin;

    /**
     * Thw world max.
     */
    @NotNull
    protected Vector3f worldMax;

    /**
     * The reference to background physics updating.
     */
    protected Future<?> physicsFuture;

    /**
     * The speed.
     */
    protected float speed;

    /**
     * The flag to enable debug.
     */
    protected boolean debugEnabled;

    /**
     * Instantiates a new Editable bullet scene app state.
     */
    public EditableBulletSceneAppState() {
        this.threadingType = ThreadingType.SEQUENTIAL;
        this.broadphaseType = BroadphaseType.DBVT;
        this.worldMin = new Vector3f(-10000f, -10000f, -10000f);
        this.worldMax = new Vector3f(10000f, 10000f, 10000f);
        this.debugEnabled = false;
    }

    @Override
    public void setSceneNode(@Nullable final SceneNode sceneNode) {
        this.sceneNode = sceneNode;
    }

    /**
     * Gets scene node.
     *
     * @return the scene node.
     */
    @Nullable
    protected SceneNode getSceneNode() {
        return sceneNode;
    }

    /**
     * Sets debug enabled.
     *
     * @param debugEnabled the flag to enable debug.
     */
    public void setDebugEnabled(final boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
        rebuildState();
    }

    /**
     * Is debug enabled boolean.
     *
     * @return true if debug is enabled.
     */
    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    /**
     * Gets physics space.
     *
     * @return the physics space.
     */
    @Nullable
    public PhysicsSpace getPhysicsSpace() {
        return physicsSpace;
    }

    /**
     * Gets speed.
     *
     * @return the speed.
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Sets speed.
     *
     * @param speed the speed.
     */
    public void setSpeed(final float speed) {
        this.speed = speed;
    }

    /**
     * Gets tpf.
     *
     * @return the time per frame.
     */
    public float getTpf() {
        return tpf;
    }

    @NotNull
    @Override
    public String getName() {
        return "Bullet state";
    }

    /**
     * Sets threading type.
     *
     * @param threadingType the threading type
     */
    public void setThreadingType(@NotNull final ThreadingType threadingType) {
        this.prevThreadingType = getPhysicsSpace() != null ? getThreadingType() : null;
        this.threadingType = threadingType;
        rebuildState();
    }

    /**
     * Gets threading type.
     *
     * @return the threading type
     */
    @NotNull
    public ThreadingType getThreadingType() {
        return threadingType;
    }

    /**
     * Sets broadphase type.
     *
     * @param broadphaseType the broadphase type
     */
    public void setBroadphaseType(@NotNull final BroadphaseType broadphaseType) {
        this.broadphaseType = broadphaseType;
        rebuildState();
    }

    /**
     * Gets broadphase type.
     *
     * @return the broadphase type
     */
    @NotNull
    public BroadphaseType getBroadphaseType() {
        return broadphaseType;
    }

    /**
     * Gets world max.
     *
     * @return the world max
     */
    @NotNull
    public Vector3f getWorldMax() {
        return worldMax;
    }

    /**
     * Sets world max.
     *
     * @param worldMax the world max
     */
    public void setWorldMax(@NotNull final Vector3f worldMax) {
        this.worldMax.set(worldMax);
        rebuildState();
    }

    /**
     * Gets world min.
     *
     * @return the world min
     */
    @NotNull
    public Vector3f getWorldMin() {
        return worldMin;
    }

    /**
     * Sets world min.
     *
     * @param worldMin the world min
     */
    public void setWorldMin(@NotNull final Vector3f worldMin) {
        this.worldMin.set(worldMin);
        rebuildState();
    }

    @Override
    public void initialize(@NotNull final AppStateManager stateManager, @NotNull final Application app) {
        super.initialize(stateManager, app);
        this.stateManager = stateManager;
        this.application = app;

        startPhysics();

        if (isDebugEnabled()) {
            debugAppState = new BulletDebugAppState(physicsSpace);
            stateManager.attach(debugAppState);
        }

        final SceneNode sceneNode = getSceneNode();
        if (sceneNode != null) {
            updateNode(sceneNode, physicsSpace);
        }
    }

    /**
     * Update a spatial.
     *
     * @param spatial      the spatial.
     * @param physicsSpace the new physical space or null.
     */
    private void updateNode(@NotNull final Spatial spatial, @Nullable final PhysicsSpace physicsSpace) {
        spatial.depthFirstTraversal(new SceneGraphVisitor() {

            @Override
            public void visit(final Spatial spatial) {

                final int numControls = spatial.getNumControls();

                for (int i = 0; i < numControls; i++) {
                    final Control control = spatial.getControl(i);
                    if (control instanceof PhysicsControl) {
                        ((PhysicsControl) control).setPhysicsSpace(physicsSpace);
                    }
                }
            }
        });
    }

    @Override
    public void cleanup() {
        super.cleanup();

        if (debugAppState != null) {
            stateManager.detach(debugAppState);
            debugAppState = null;
        }

        final SceneNode sceneNode = getSceneNode();
        if (sceneNode != null) {
            updateNode(sceneNode, null);
        }

        stopPhysics();

        this.stateManager = null;
        this.application = null;
    }

    /**
     * Start physics.
     */
    public void startPhysics() {
        if (physicsSpace != null) return;

        if (threadingType == ThreadingType.PARALLEL) {
            startBackgroundPhysics();
        } else {
            physicsSpace = new PhysicsSpace(worldMin, worldMax, broadphaseType);
            physicsSpace.addTickListener(this);
        }

        if (threadingType == ThreadingType.PARALLEL) {
            PhysicsSpace.setLocalThreadPhysicsSpace(physicsSpace);
        }
    }

    /**
     * Start background physics.
     *
     * @return the boolean
     */
    protected boolean startBackgroundPhysics() {

        if (executor == null) {
            executor = Executors.newSingleThreadScheduledExecutor();
        }

        try {

            return executor.submit(new Callable<Boolean>() {

                @Override
                public Boolean call() throws Exception {

                    physicsSpace = new PhysicsSpace(worldMin, worldMax, broadphaseType);
                    physicsSpace.addTickListener(EditableBulletSceneAppState.this);

                    return true;
                }

            }).get();

        } catch (final InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Rebuild this state.
     */
    protected void rebuildState() {
        if (!isInitialized()) return;

        final SceneNode sceneNode = getSceneNode();

        if (debugAppState != null) {
            stateManager.detach(debugAppState);
            debugAppState = null;
        }

        if (sceneNode != null) {
            updateNode(sceneNode, null);
        }

        stopPhysics();
        startPhysics();

        if (isDebugEnabled()) {
            debugAppState = new BulletDebugAppState(physicsSpace);
            stateManager.attach(debugAppState);
        }

        if (sceneNode != null) {
            updateNode(sceneNode, physicsSpace);
        }
    }

    /**
     * Stop physics.
     */
    public void stopPhysics() {
        if (physicsSpace == null) return;

        if (executor != null) {
            executor.shutdown();
            executor = null;
        }

        final ThreadingType threadingType = prevThreadingType != null ? prevThreadingType : getThreadingType();

        if (threadingType == ThreadingType.PARALLEL) {
            PhysicsSpace.setLocalThreadPhysicsSpace(null);
            prevThreadingType = null;
        }

        physicsSpace.removeTickListener(this);
        physicsSpace.destroy();
        physicsSpace = null;
    }

    @Override
    public void render(@NotNull final RenderManager renderManager) {
        if (!isEnabled()) return;
        switch (threadingType) {
            case PARALLEL: {
                physicsFuture = executor.submit(physicsUpdateTask);
                break;
            }
            case SEQUENTIAL: {
                physicsSpace.update(tpf * speed);
                break;
            }
        }
    }

    @Override
    public void postRender() {
        if (physicsFuture == null) return;
        try {
            physicsFuture.get();
            physicsFuture = null;
        } catch (final InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prePhysicsTick(@NotNull final PhysicsSpace space, final float tpf) {

    }

    @Override
    public void physicsTick(@NotNull final PhysicsSpace space, final float tpf) {

    }

    @Override
    public void update(final float tpf) {
        super.update(tpf);

        final PhysicsSpace physicsSpace = getPhysicsSpace();
        if (physicsSpace != null) {
            physicsSpace.distributeEvents();
        }

        this.tpf = tpf;
    }

    @Override
    public void notifyAdded(@NotNull final Object object) {
        if (object instanceof PhysicsControl) {
            ((PhysicsControl) object).setPhysicsSpace(getPhysicsSpace());
        } else if (object instanceof Spatial) {
            updateNode((Spatial) object, getPhysicsSpace());
        }
    }

    @Override
    public void notifyRemoved(@NotNull final Object object) {
        if (object instanceof PhysicsControl) {
            ((PhysicsControl) object).setPhysicsSpace(null);
        } else if (object instanceof Spatial) {
            updateNode((Spatial) object, null);
        }
    }

    @NotNull
    @Override
    public List<EditableProperty<?, ?>> getEditableProperties() {

        final List<EditableProperty<?, ?>> result = new ArrayList<>(6);

        result.add(new SimpleProperty<>(BOOLEAN, "Debug enabled", this,
                makeGetter(this, boolean.class, "isDebugEnabled"),
                makeSetter(this, boolean.class, "setDebugEnabled")));
        result.add(new SimpleProperty<>(FLOAT, "Speed", this,
                makeGetter(this, float.class, "getSpeed"),
                makeSetter(this, float.class, "setSpeed")));
        result.add(new SimpleProperty<>(ENUM, "Broadphase type", this,
                makeGetter(this, BroadphaseType.class, "getBroadphaseType"),
                makeSetter(this, BroadphaseType.class, "setBroadphaseType")));
        result.add(new SimpleProperty<>(ENUM, "Threading type", this,
                makeGetter(this, ThreadingType.class, "getThreadingType"),
                makeSetter(this, ThreadingType.class, "setThreadingType")));
        result.add(new SimpleProperty<>(VECTOR_3F, "World max", this,
                makeGetter(this, Vector3f.class, "getWorldMax"),
                makeSetter(this, Vector3f.class, "setWorldMax")));
        result.add(new SimpleProperty<>(VECTOR_3F, "World min", this,
                makeGetter(this, Vector3f.class, "getWorldMin"),
                makeSetter(this, Vector3f.class, "setWorldMin")));

        return result;
    }

    @Nullable
    @Override
    public String checkStates(@NotNull final List<SceneAppState> exists) {
        return null;
    }

    @Nullable
    @Override
    public String checkFilters(@NotNull final List<SceneFilter> exists) {
        return null;
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
    public void cloneFields(@NotNull final Cloner cloner, @NotNull final Object original) {
        sceneNode = cloner.clone(sceneNode);
        worldMin = cloner.clone(worldMin);
        worldMax = cloner.clone(worldMax);
    }

    @Override
    public void write(@NotNull final JmeExporter exporter) throws IOException {
        final OutputCapsule capsule = exporter.getCapsule(this);
        capsule.write(sceneNode, "sceneNode", null);
        capsule.write(threadingType, "threadingType", ThreadingType.SEQUENTIAL);
        capsule.write(broadphaseType, "broadphaseType", BroadphaseType.DBVT);
        capsule.write(worldMin, "worldMin", null);
        capsule.write(worldMax, "worldMax", null);
        capsule.write(speed, "speed", 0);
        capsule.write(debugEnabled, "debugEnabled", false);
    }

    @Override
    public void read(@NotNull final JmeImporter importer) throws IOException {
        final InputCapsule capsule = importer.getCapsule(this);
        sceneNode = (SceneNode) capsule.readSavable("sceneNode", null);
        threadingType = capsule.readEnum("threadingType", ThreadingType.class, ThreadingType.SEQUENTIAL);
        broadphaseType = capsule.readEnum("broadphaseType", BroadphaseType.class, BroadphaseType.DBVT);
        worldMin = (Vector3f) capsule.readSavable("worldMin", null);
        worldMax = (Vector3f) capsule.readSavable("worldMax", null);
        speed = capsule.readFloat("speed", 0);
        debugEnabled = capsule.readBoolean("debugEnabled", false);
    }
}
