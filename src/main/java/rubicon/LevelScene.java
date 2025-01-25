package rubicon;

/**
 * Class: LevelScene
 * Author: rapto
 * CreatedDate: 1/19/2025 : 2:44 AM
 * Project: GameEngine
 * Description: Level scene class that renders a red screen for test purposes.
 */
public class LevelScene extends Scene {

    public LevelScene() {
        System.out.println("Inside Level Scene");
        Window.getBackgroundColor()
              .set(1, 0, 0, 1);
    }

    /**
     * Responsible for updating the scene at each time delta.
     *
     * @param dt Time delta for the scene.
     */
    @Override
    public void update(float dt) {
        // Unused
    }
}
