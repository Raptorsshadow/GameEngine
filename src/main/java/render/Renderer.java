package render;

import components.SpriteRenderer;
import rubicon.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class: Renderer
 * Author: rapto
 * CreatedDate: 1/22/2025 : 11:01 AM
 * Project: GameEngine
 * Description: Manages GameObject resources and batches them as available for rendering.
 */
public class Renderer {

    // Max number of elements to batch together
    private static final int MAX_BATCH_SIZE = 1000;

    //Stores references to all RenderBatch elements
    private final List<RenderBatch> batches = new ArrayList<>();

    /**
     * Adds a GameObject to the Renderer.
     *
     * @param go GameObject we wish to render.
     */
    public void add(GameObject go) {
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if (spr != null) {
            add(spr);
        }
    }

    /**
     * Attempts to add give SpriteRenderer to an available RenderBatch or allocates a new one if necessary.
     *
     * @param spr Sprite Component to be registered
     */
    private void add(SpriteRenderer spr) {
        boolean isAdded = false;
        for (RenderBatch batch : batches) {
            if (batch.hasRoom() && batch.getZIndex() == spr.getGameObject().getzIndex()) {
                Texture tex = spr.getTexture();
                if (tex == null || (batch.hasSprite(tex) || batch.hasSpriteRoom())) {
                    batch.addSprite(spr);
                    isAdded = true;
                    break;
                }
            }
        }
        if (!isAdded) {
            RenderBatch rb = new RenderBatch(MAX_BATCH_SIZE, spr.getGameObject().getzIndex());
            rb.start();
            batches.add(rb);
            rb.addSprite(spr);
            Collections.sort(batches);
        }
    }

    /**
     * Runs the render method on each RenderBatch Object
     */
    public void render() {
        batches.forEach(RenderBatch::render);
    }
}
