package component;

import imgui.ImGui;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;
import org.joml.Vector4f;
import rubicon.GameObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Class: Component
 * Author: rapto
 * CreatedDate: 1/19/2025 : 12:19 AM
 * Project: GameEngine
 * Description: Abstract class for managing the component lifecycle for the Entity Component System.
 */
@Setter
@Getter
public abstract class Component implements Serializable {
    private static final Logger log = LogManager.getLogger(Component.class);

    protected transient GameObject gameObject;

    private static int idCounter = 0;
    private int uid = -1;

    /**
     * Responsible for performing an update operation on each "tick" to be defined by concrete classes.
     * Called after start and while the component is in use.
     *
     * @param dt Delta Time
     */
    public void update(float dt) {
        //Not implemented
    }

    /**
     * Optional lifecycle hook to perform initialization and startup procedures if necessary.
     */
    public void start() {
        //Not implemented
    }

    /**
     * Hook for a component to render its own ImGui overlay
     * By default leverages reflection to inspect fields on class and builds a generic ImGui window for those.
     * Ignores fields marked as transient.
     */
    public void imgui() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for(Field f : fields) {
                boolean isTransient = Modifier.isTransient(f.getModifiers());
                //If field is transient, skip
                if(isTransient) {
                    continue;
                }
                //If field is private, make it accessible and then re-lock it.
                boolean isPrivate = Modifier.isPrivate(f.getModifiers());
                if(isPrivate) {
                    f.setAccessible(true);
                }
                Class<?> type = f.getType();
                Object value = f.get(this);
                String name = f.getName();

                //Check the type of field and render/update appropriately.
                if(type == int.class) {
                    imguiInt(f, (int) value, name);
                } else if(type == float.class) {
                    imguiFloat(f, (float) value, name);
                } else if(type == boolean.class) {
                    imguiBoolean(f, (boolean) value, name);
                } else if (type == Vector3f.class) {
                    imguiVector3f((Vector3f) value, name);
                } else if (type == Vector4f.class) {
                    imguiVector4f((Vector4f) value, name);
                }
                if(isPrivate) {
                    f.setAccessible(false);
                }
            }
        } catch(IllegalAccessException e) {
            log.error("Unable to access field", e);
        }
    }

    /**
     * Generate an imgui control for managing a Vector4f field
     * @param val field value
     * @param name field name
     */
    private static void imguiVector4f(Vector4f val, String name) {
        float[] floats = {val.x, val.y, val.z, val.w};
        if(ImGui.dragFloat4(name + " : ", floats)) {
            val.set(floats[0], floats[1], floats[2], floats[3]);
        }
    }

    /**
     * Generate an imgui control for managing a Vector3f field
     * @param val field value
     * @param name field name
     */
    private static void imguiVector3f(Vector3f val, String name) {
        float[] floats = {val.x, val.y, val.z};
        if(ImGui.dragFloat3(name + " : ", floats)) {
            val.set(floats[0], floats[1], floats[2]);
        }
    }

    /**
     * Generate an imgui control for managing a boolean field
     * @param val field value
     * @param name field name
     */
    private void imguiBoolean(Field f, boolean val, String name) throws IllegalAccessException {
        if(ImGui.checkbox(name + " : ", val)) {
            f.set(this, !val);
        }
    }

    /**
     * Generate an imgui control for managing a float field
     * @param val field value
     * @param name field name
     */
    private void imguiFloat(Field f, float val, String name) throws IllegalAccessException {
        float[] imFloat = {val};
        if (ImGui.dragFloat(name + " : ", imFloat)) {
            f.set(this, imFloat[0]);
        }
    }

    /**
     * Generate an imgui control for managing an int field
     * @param val field value
     * @param name field name
     */
    private void imguiInt(Field f, int val, String name) throws IllegalAccessException {
        int[] imInt = {val};
        if (ImGui.dragInt(name + " : ", imInt)) {
            f.set(this, imInt[0]);
        }
    }

    /**
     * Set the uid to be the next available component identifier
     */
    public synchronized void generateId() {
        if (this.uid == -1) {
            this.uid = idCounter++;
        }
    }

    /**
     * Set the component identifier seed.
     * @param maxId new seed
     */
    public static void init(int maxId) {
        idCounter = maxId;
    }
}
