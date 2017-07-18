import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Created by Jiachen on 7/16/17.
 * Represents the obstacle that separates the start point and the destination
 */
class Obstacle {
    private float length;
    private PVector pos;
    private PApplet parent;
    private static float strokeWeight = 3;
    private static int color = 0;

    Obstacle(PApplet parent, float x, float y, float length) {
        this.pos = new PVector(x, y);
        this.length = length;
        this.parent = parent;
    }

    void display() {
        parent.pushMatrix();
        parent.translate(pos.x, pos.y);
        parent.strokeWeight(strokeWeight);
        parent.stroke(color);
        parent.line(0, 0 - length / 2, 0, 0 + length / 2);
        parent.popMatrix();
    }

    private boolean isCollidingWith(Mouse mouse) {
        return !(Math.abs(mouse.getPos().y - this.pos.y) > length / 2) && Math.abs(mouse.getPos().x - this.pos.x) <= Mouse.getRadius();
    }

    void checkCollisionWith(ArrayList<Mouse> mice){
        for (Mouse mouse: mice)
            if (this.isCollidingWith(mouse))
                mouse.expire();
    }

}
