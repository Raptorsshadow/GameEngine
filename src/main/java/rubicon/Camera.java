package rubicon;

import lombok.Data;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Class: Camera
 * Author: rapto
 * CreatedDate: 1/19/2025 : 1:50 AM
 * Project: GameEngine
 * Description: Responsible for creating and adjusting a Camera viewpoint.
 */
@Data
public class Camera {

    public static final float RIGHT_LENGTH = 32f * 40f;
    public static final float TOP_LENGTH = 32f * 21f;
    public static final float NEAR_DIST = 0f;
    public static final float FAR_DIST = 100f;
    //Defines the projection matrix used to describe the scene canvas
    private final Matrix4f projectionMatrix;

    //Defines the viewpoint of the camera itself
    private final Matrix4f viewMatrix;

    //Defines the x/y coordinates of the camera
    private final Vector2f position;

    //Placeholder for storing the inverse of the projection matrix.  Used for mouse interactions with screen.
    private final Matrix4f inverseProjection;

    //Placeholder for storing the inverse of the view matrix.  Used for mouse interactions with the screen.
    private final Matrix4f inverseView;

    /**
     * Constructor responsible for initializing relevant camera matrices and adjusting the projection to the
     * given position.
     *
     * @param position x/y coordinates of the camera
     */
    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }

    /**
     * Defines how large our "World View" Is.
     */
    public void adjustProjection() {
        projectionMatrix.identity();

        //Configured the Projection matrix to be an orthogonal plane.
        projectionMatrix.ortho(0.0f, RIGHT_LENGTH, 0.0f, TOP_LENGTH, NEAR_DIST, FAR_DIST);
        projectionMatrix.invert(inverseProjection);
    }

    /**
     * Defines how the Camera is looking.
     *
     * @return View Matrix after lookAt cameraFront/Position
     */
    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f), cameraFront.add(position.x, position.y, 0.0f),
                cameraUp);
        this.viewMatrix.invert(inverseView);
        return this.viewMatrix;
    }
}
