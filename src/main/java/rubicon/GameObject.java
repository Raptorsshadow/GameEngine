package rubicon;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private final String name;
    private final List<Component> components;
    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
    }

    public <T extends Component> T getComponent(Class<T> cClass) {
       return components
               .stream()
               .filter(c ->  cClass.isAssignableFrom(c.getClass()))
               .findFirst()
               .map(cClass::cast)
               .orElse(null);
    }

    public <T extends Component> void removeComponent(Class<T> cClass) {
        for(Component c : this.components) {
            if(cClass.isAssignableFrom(c.getClass())) {
                this.components.remove(c);
                return;
            }
        }
    }

    public void addComponent(Component c) {
        this.components.add(c);
        c.gameObject = this;
    }

    public void update(float dt) {
        System.out.println("Updating Components for " + this.name);
        this.components.forEach(c -> c.update(dt));
    }

    public void start() {
        System.out.println("Starting Components for " + this.name);
        this.components.forEach(Component::start);
    }
}
