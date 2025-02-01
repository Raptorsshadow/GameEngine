package components;

import org.joml.Vector2f;
import render.Texture;

import java.util.ArrayList;
import java.util.List;

/**
 * Class: SpriteSheet
 * Author: rapto
 * CreatedDate: 1/22/2025 : 4:24 PM
 * Project: GameEngine
 * Description: Manages a collection of sprites from a tiled uniform sprite sheet
 */
public class SpriteSheet {
    //Texture containing all the sprites
    private final Texture texture;
    //Collection of manages sprites
    private final List<Sprite> sprites;

    /**
     * Constructor that generates individual sprites from a texture.
     *
     * @param texture      Uniform tiles sprite sheet
     * @param spriteWidth  Width of a Tile
     * @param spriteHeight Height of a Tile
     * @param numSprites   Number of sprites within sprite sheet
     * @param spaceBuffer  Spacing border around tiles
     */
    public SpriteSheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spaceBuffer) {
        this.sprites = new ArrayList<>();
        this.texture = texture;

        //Initialize the Start coordinates to traverse left to right, top to bottom
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;

        //Iterate over the numSprites given to parse out tiles
        for (int i = 0; i < numSprites; i++) {
            //Convert pixel values into normalized floats
            float topY = (currentY + spriteHeight) / (float) texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float) texture.getWidth();
            float leftX = currentX / (float) texture.getWidth();
            float bottomY = currentY / (float) texture.getHeight();

            //Build texture coordinates for the tile
            Vector2f[] texCoords = new Vector2f[]{
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };
            //Build and store the Sprite
            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setTexCoords(texCoords);
            sprites.add(sprite);

            //Increment the positional bits
            currentX += spriteWidth + spaceBuffer;
            //If we've reached the end on the right, reset to start on left and decrementY to go down to next row.
            if (currentX >= texture.getWidth()) {
                currentX = 0;
                currentY -= (spriteHeight + spaceBuffer);
            }
        }
    }

    /**
     * Retrieve a sprite at given index
     *
     * @param index Index of sprite in sheet
     * @return Sprite at index
     */
    public Sprite getSprite(int index) {
        return this.sprites.get(index);
    }
}
