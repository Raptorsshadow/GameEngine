package rubicon;

public abstract class Component {
    public GameObject gameObject;

    public abstract void update(float dt);

    public abstract void start();
}
