package unsw.graphics.scene;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame2D;
import unsw.graphics.Matrix3;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point2D;

/**
 * A SceneObject is an object that can move around in the world.
 * 
 * SceneObjects form a scene tree.
 * 
 * Each SceneObject is offset from its parent by a translation, a rotation and a scale factor. 
 *
 * @author malcolmr
 * @author Robert Clifton-Everest
 */
public class SceneObject {
    
    // the links in the scene tree
    private SceneObject myParent;
    private List<SceneObject> myChildren;

    // the local transformation
    private Point2D myTranslation;
    private float myRotation; //normalised to the range [-180..180)
    private float myScale;
    
    // Is this part of the tree showing?
    private boolean amShowing;

    /**
     * Special constructor for creating the root node. Do not use otherwise.
     */
    public SceneObject() {
        myParent = null;
        myChildren = new ArrayList<SceneObject>();

        myRotation = 0;
        myScale = 1;
        myTranslation = new Point2D(0,0);

        amShowing = true;
    }

    /**
     * Public constructor for creating SceneObjects, connected to a parent.
     *  
     * New objects are created at the same location, orientation and scale as the parent.
     *
     * @param parent
     */
    public SceneObject(SceneObject parent) {
        myParent = parent;
        myChildren = new ArrayList<SceneObject>();

        parent.myChildren.add(this);

        myRotation = 0;
        myScale = 1;
        myTranslation = new Point2D(0,0);

        // initially showing
        amShowing = true;
    }

    /**
     * Remove an object and all its children from the scene tree.
     */
    public void destroy() {
	    List<SceneObject> childrenList = new ArrayList<SceneObject>(myChildren);
        for (SceneObject child : childrenList) {
            child.destroy();
        }
        if(myParent != null)
                myParent.myChildren.remove(this);
    }

    /**
     * Get the parent of this scene object
     * 
     * @return
     */
    public SceneObject getParent() {
        return myParent;
    }

    /**
     * Get the children of this object
     * 
     * @return
     */
    public List<SceneObject> getChildren() {
        return myChildren;
    }

    /**
     * Get the local rotation (in degrees)
     * 
     * @return
     */
    public float getRotation() {
        return myRotation;
    }

    /**
     * Set the local rotation (in degrees)
     * 
     * @return
     */
    public void setRotation(float rotation) {
        myRotation = MathUtil.normaliseAngle(rotation);
    }

    /**
     * Rotate the object by the given angle (in degrees)
     * 
     * @param angle
     */
    public void rotate(float angle) {
        myRotation += angle;
        myRotation = MathUtil.normaliseAngle(myRotation);
    }

    /**
     * Get the local scale
     * 
     * @return
     */
    public float getScale() {
        return myScale;
    }

    /**
     * Set the local scale
     * 
     * @param scale
     */
    public void setScale(float scale) {
        myScale = scale;
    }

    /**
     * Multiply the scale of the object by the given factor
     * 
     * @param factor
     */
    public void scale(float factor) {
        myScale *= factor;
    }

    /**
     * Get the local position of the object 
     * 
     * @return
     */
    public Point2D getPosition() {
        return myTranslation;
    }

    /**
     * Set the local position of the object
     * 
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {
        setPosition(new Point2D(x,y));
    }

    /**
     * Set the local position of the object
     * 
     * @param p
     */
    public void setPosition(Point2D p) {
        myTranslation = p;
    }

    /**
     * Move the object by the specified offset in local coordinates
     * 
     * @param dx
     * @param dy
     */
    public void translate(float dx, float dy) {
        myTranslation = myTranslation.translate(dx, dy);
    }

    /**
     * Test if the object is visible
     * 
     * @return
     */
    public boolean isShowing() {
        return amShowing;
    }

    /**
     * Set the showing flag to make the object visible (true) or invisible (false).
     * This flag should also apply to all descendents of this object.
     * 
     * @param showing
     */
    public void show(boolean showing) {
        amShowing = showing;
    }

    /**
     * Update the object and all it's children. This method is called once per frame. 
     * 
     * @param dt The amount of time since the last update (in seconds)
     */
    public void update(float dt) {
        updateSelf(dt);
        
        // Make a copy of all the children to avoid concurrently modification issues if new objects
        // are added to the scene during the update.
        List<SceneObject> children = new ArrayList<SceneObject>(myChildren);
        for (SceneObject so : children) {
            so.update(dt);
        }
    }

    /** 
     * Update the object itself. Does nothing in the default case. Subclasses can override this
     * for animation or interactivity.
     * 
     * @param dt
     */
    public void updateSelf(float dt) {
        // Do nothing by default
    }

    /**
     * Draw the object (but not any descendants)
     * 
     * This does nothing in the base SceneObject class. Override this in subclasses.
     * 
     * @param gl
     */
    public void drawSelf(GL3 gl, CoordFrame2D frame) {
        // Do nothing by default
    }

    
    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
    
    /**
     * Draw the object and all of its descendants recursively.
     * 
     *
     * @param gl
     */
    public void draw(GL3 gl, CoordFrame2D frame) {
        
        // don't draw if it is not showing
        if (!amShowing) {
            return;
        }

        // draw the object (Call drawSelf() to draw the object itself)
        // and all its children recursively
        drawSelf(gl, frame.translate(myTranslation).rotate(myRotation).scale(myScale, myScale));

        for (SceneObject object: myChildren) {
            //object.draw(gl, frame.translate(myTranslation.getX(), myTranslation.getY()).rotate(myRotation).scale(myScale, myScale));
            object.draw(gl, frame.translate(myTranslation.getX(), myTranslation.getY()).rotate(myRotation).scale(myScale, myScale));

        }
    }

    /**
     * Compute the object's position in world coordinates
     * 
     * @return a point in world coordinates
     */
    public Point2D getGlobalPosition() {
        CoordFrame2D frame2D = new CoordFrame2D(CoordFrame2D.identity().getMatrix());
        if (myParent != null) {
            frame2D = frame2D.translate(myParent.getGlobalPosition().getX(), myParent.getGlobalPosition().getY()).rotate(myParent.getGlobalRotation()).scale(myParent.getGlobalScale(), myParent.getGlobalScale());
            return frame2D.getMatrix().multiply(myTranslation.asHomogenous()).asPoint2D();
        }
        else return myTranslation;
    }

    /**
     * Compute the object's rotation in the global coordinate frame
     * 
     * @return the global rotation of the object (in degrees) and 
     * normalized to the range (-180, 180) degrees. 
     */
    public float getGlobalRotation() {
        if (myParent != null) return MathUtil.normaliseAngle(myParent.getGlobalRotation() + myRotation);
        else return MathUtil.normaliseAngle(myRotation);
    }

    /**
     * Compute the object's scale in global terms
     * 
     * @return the global scale of the object 
     */
    public float getGlobalScale() {
        if (myParent != null) return myParent.getGlobalScale() * myScale;
        else return myScale;
    }

    /**
     * Change the parent of a scene object.
     * 
     * @param parent
     */
    public void setParent(SceneObject parent) {
        // when it is reparented. You may need to add code before and/or after
        // the fragment of code that has been provided - depending on your approach

        Matrix3 parentMatrix = new CoordFrame2D(CoordFrame2D.identity().getMatrix())
                .scale(1f/parent.getGlobalScale(), 1f/parent.getGlobalScale())
                .rotate(-parent.getGlobalRotation())
                .translate(-parent.getGlobalPosition().getX(), -parent.getGlobalPosition().getY())
                .getMatrix();
        myTranslation = parentMatrix.multiply(this.getGlobalPosition().asHomogenous()).asPoint2D();

        myRotation = this.getGlobalRotation() - parent.getGlobalRotation();
        myRotation = MathUtil.normaliseAngle(myRotation);
        
        myScale = this.getGlobalScale() / parent.getGlobalScale();

        myParent.myChildren.remove(this);
        myParent = parent;
        myParent.myChildren.add(this);
        
    }
    

}
