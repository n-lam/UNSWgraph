package unsw.graphics.scene;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame2D;
import unsw.graphics.Matrix3;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point2D;

/**
 * The camera is a SceneObject that can be moved, rotated and scaled like any other, as well as
 * attached to any parent in the scene tree.
 *
 * @author malcolmr
 * @author Robert Clifton-Everest
 */
public class Camera extends SceneObject {
    
    /**
     * The aspect ratio is the ratio of the width of the window to the height.
     */
    private float myAspectRatio;

    public Camera(SceneObject parent) {
        super(parent);
    }

    public void setView(GL3 gl) {
        CoordFrame2D viewFrame = CoordFrame2D.identity().scale(1/myAspectRatio, 1);
        
        // rotation and scale
        viewFrame = viewFrame
                .scale(1/getGlobalScale(), 1/getGlobalScale())
                .rotate(-getGlobalRotation())
                .translate(-getGlobalPosition().getX(), -getGlobalPosition().getY());
        
        Shader.setViewMatrix(gl, viewFrame.getMatrix());
    }

    public void reshape(int width, int height) {
        myAspectRatio = (1f * width) / height;            
    }

    /**
     * Transforms a point from camera coordinates to world coordinates. Useful for things like mouse
     * interaction
     * 
     * @param x
     * @param y
     * @return
     */
    public Point2D fromView(float x, float y) {
        Matrix3 mat = Matrix3.translation(getGlobalPosition())
                .multiply(Matrix3.rotation(getGlobalRotation()))
                .multiply(Matrix3.scale(getGlobalScale(), getGlobalScale()))
                .multiply(Matrix3.scale(myAspectRatio, 1));
        return mat.multiply(new Vector3(x,y,1)).asPoint2D();
    }

    public float getAspectRatio() {
        return myAspectRatio;
    }
}
