package unsw.graphics.geometry;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import unsw.graphics.CoordFrame2D;
import unsw.graphics.Point2DBuffer;
import unsw.graphics.Shader;

import java.util.ArrayList;
import java.util.List;

public class Circle2D extends Polygon2D {
    private static final int VERTICES = 64;
    private List<Point2D> points;

    public Circle2D(float radius) {
        points = new ArrayList<>();
        for (int i = 0; i < VERTICES; i++) {
            float angle = (float) (i * Math.PI * 2 / VERTICES);
            float x = radius * (float) Math.cos(angle);
            float y = radius * (float) Math.sin(angle);
            Point2D p = new Point2D(x, y);
            points.add(p);
        }
    }

    /**
     * Draw the circle in the given coordinate frame.
     * @param gl
     */
    public void draw(GL3 gl, CoordFrame2D frame) {
        Point2DBuffer buffer = new Point2DBuffer(points);

        int[] names = new int[1];
        gl.glGenBuffers(1, names, 0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, names[0]);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, points.size() * 2 * Float.BYTES,
                buffer.getBuffer(), GL.GL_STATIC_DRAW);

        gl.glVertexAttribPointer(Shader.POSITION, 2, GL.GL_FLOAT, false, 0, 0);
        Shader.setModelMatrix(gl, frame.getMatrix());
        gl.glDrawArrays(GL.GL_TRIANGLE_FAN, 0, points.size());

        gl.glDeleteBuffers(1, names, 0);
    }

    @Override
    public void drawOutline(GL3 gl, CoordFrame2D frame) {
        // It should draw an outline of a polygon using GL_LINE_LOOP
        Point2DBuffer buffer = new Point2DBuffer(points);

        int[] names = new int[1];
        gl.glGenBuffers(1, names, 0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, names[0]);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, points.size() * 2 * Float.BYTES,
                buffer.getBuffer(), GL.GL_STATIC_DRAW);

        gl.glVertexAttribPointer(Shader.POSITION, 2, GL.GL_FLOAT, false, 0, 0);
        Shader.setModelMatrix(gl, frame.getMatrix());
        gl.glDrawArrays(GL.GL_LINE_LOOP, 0, points.size());

        gl.glDeleteBuffers(1, names, 0);
    }
}
