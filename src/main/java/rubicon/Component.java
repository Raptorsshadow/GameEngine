package rubicon;

import imgui.ImGui;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import org.joml.Vector4f;

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
    protected transient GameObject gameObject;

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
                    int val = (int) value;
                    int[] imInt = {val};
                    if (ImGui.dragInt(name + " : ", imInt)) {
                        f.set(this, imInt[0]);
                    }
                } else if(type == float.class) {
                    float val = (float) value;
                    float[] imFloat = {val};
                    if (ImGui.dragFloat(name + " : ", imFloat)) {
                        f.set(this, imFloat[0]);
                    }
                } else if(type == boolean.class) {
                    boolean val = (boolean) value;
                    if(ImGui.checkbox(name + " : ", val)) {
                        f.set(this, !val);
                    }
                } else if (type == Vector3f.class) {
                    Vector3f val = (Vector3f) value;
                    float[] floats = {val.x, val.y, val.z};
                    if(ImGui.dragFloat3(name + " : ", floats)) {
                        val.set(floats[0], floats[1], floats[2]);
                    }
                } else if (type == Vector4f.class) {
                    Vector4f val = (Vector4f) value;
                    float[] floats = {val.x, val.y, val.z, val.w};
                    if(ImGui.dragFloat4(name + " : ", floats)) {
                        val.set(floats[0], floats[1], floats[2], floats[3]);
                    }
                }
                if(isPrivate) {
                    f.setAccessible(false);
                }
            }
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
