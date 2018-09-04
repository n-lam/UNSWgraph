package unsw.graphics.scene;

import com.jogamp.opengl.GL3;
import unsw.graphics.CoordFrame2D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Circle2D;

import java.awt.*;

public class CircularSceneObject extends PolygonalSceneObject {

    private Circle2D myCircle;
    private Color myFillColor;
    private Color myLineColor;

    /**
     * Create a circular scene object and add it to the scene tree
     * <p>
     * The line and fill colors can possibly be null, in which case that part of the object
     * should not be drawn.
     *
     * @param parent    The parent in the scene tree
     * @param fillColor The fill color
     * @param lineColor The outline color
     */
    public CircularSceneObject(SceneObject parent, Color fillColor, Color lineColor) {
        super(parent, null, fillColor, lineColor);
        myCircle = new Circle2D(1);
        myFillColor = fillColor;
        myLineColor = lineColor;
    }

    /**
     * Create a circular scene object and add it to the scene tree
     * <p>
     * The line and fill colors can possibly be null, in which case that part of the object
     * should not be drawn.
     *
     * @param parent    The parent in the scene tree
     * @param radius    Radius of the circle
     * @param fillColor The fill color
     * @param lineColor The outline color
     */
    public CircularSceneObject(SceneObject parent, float radius, Color fillColor, Color lineColor) {
        super(parent, null, fillColor, lineColor);
        myCircle = new Circle2D(radius);
        myFillColor = fillColor;
        myLineColor = lineColor;
    }

    /**
     * Get the fill color
     *
     * @return
     */
    public Color getFillColor() {
        return myFillColor;
    }

    /**
     * Set the fill color.
     *
     * Setting the color to null means the object should not be filled.
     *
     * @param fillColor The fill color
     */
    public void setFillColor(Color fillColor) {
        myFillColor = fillColor;
    }

    /**
     * Get the outline color.
     *
     * @return
     */
    public Color getLineColor() {
        return myLineColor;
    }

    /**
     * Set the outline color.
     *
     * Setting the color to null means the outline should not be drawn
     *
     * @param lineColor
     */
    public void setLineColor(Color lineColor) {
        myLineColor = lineColor;
    }

    @Override
    public void drawSelf(GL3 gl, CoordFrame2D frame) {

        if (myFillColor != null) {
            Shader.setPenColor(gl, myFillColor);
            myCircle.draw(gl, frame);
        }

        if (myLineColor != null) {
            Shader.setPenColor(gl, myLineColor);
            myCircle.drawOutline(gl, frame);
        }

        Shader.setPenColor(gl, Color.BLACK);
    }
}
