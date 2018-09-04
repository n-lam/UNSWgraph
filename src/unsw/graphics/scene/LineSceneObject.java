package unsw.graphics.scene;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import unsw.graphics.CoordFrame2D;
import unsw.graphics.Point2DBuffer;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Line2D;
import unsw.graphics.geometry.Point2D;

import java.awt.*;

public class LineSceneObject extends SceneObject {

    private Line2D myLine;
    private Color myLineColor;

    public LineSceneObject(SceneObject parent, Color lineColor) {
        super(parent);
        myLine = new Line2D(0, 0, 1, 0);
        myLineColor = lineColor;
    }

    public LineSceneObject(SceneObject parent, float x0, float y0, float x1, float y1, Color lineColor) {
        super(parent);
        myLine = new Line2D(x0, y0, x1, y1);
        myLineColor = lineColor;
    }

    /**
     * Draw the polygon in the given coordinate frame.
     * @param gl
     */
    public void drawSelf(GL3 gl, CoordFrame2D frame) {
        if (myLineColor != null) {
            Shader.setPenColor(gl, myLineColor);
            myLine.draw(gl, frame);
        }
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

}
