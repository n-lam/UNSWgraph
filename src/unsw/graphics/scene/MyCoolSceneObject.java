package unsw.graphics.scene;

import java.awt.*;
import java.util.ArrayList;

/**
 * Display an analogue clock with a special time
 */
public class MyCoolSceneObject extends SceneObject {

    private CircularSceneObject clockFace;
    private ArrayList<LineSceneObject> markings;
    private LineSceneObject hourHand;
    private LineSceneObject minuteHand;
    private CircularSceneObject centreBolt;
    private static final int FPS = 25;
    private static final int MIN_IN_HR = 60;
    private static final int HR_IN_CLOCK = 12;

    // Set time to 04:20
    private int time = 6500;

    public MyCoolSceneObject(SceneObject parent) {
        super(parent);

        clockFace = new CircularSceneObject(this, Color.WHITE, Color.BLACK);
        centreBolt = new CircularSceneObject(this, 0.05f, Color.YELLOW, null);
        markings = new ArrayList<>();
        float hourAngle = -((float) time / (FPS * MIN_IN_HR * HR_IN_CLOCK) * 360f);
        float minuteAngle = -((float) time % (FPS * MIN_IN_HR) / (FPS * MIN_IN_HR) * 360f);
        minuteHand = new LineSceneObject(centreBolt,
                0,0,
                0, 0.7f,
                Color.RED
        );
        hourHand = new LineSceneObject(centreBolt,
                0,0,
                0, 0.4f,
                Color.GREEN
        );
        minuteHand.rotate(minuteAngle);
        hourHand.rotate(hourAngle);
        float length;
        for (int i = 0; i < 12; i++) {
            float angle = (float) (i * Math.PI * 2 / 12);
            if (i % 3 == 0) length = 0.25f;
            else length = 0.125f;
            markings.add(new LineSceneObject(
                    clockFace,
                    (1 - length) * (float) Math.cos(angle),
                    (1 - length) * (float) Math.sin(angle),
                    1f * (float) Math.cos(angle),
                    1f * (float) Math.sin(angle),
                    Color.BLACK)
            );
        }
    }
}
