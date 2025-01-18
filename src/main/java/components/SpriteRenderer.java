package components;

import rubicon.Component;

public class SpriteRenderer extends Component {
    private boolean first = false;
    @Override
    public void update(float dt) {
        if(!first)
            System.out.println("I am Updating!");
        first = true;
    }

    @Override
    public void start() {
        System.out.println("I am Starting");
    }
}
