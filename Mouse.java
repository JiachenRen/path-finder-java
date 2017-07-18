import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Created by Jiachen on 7/16/17.
 * This class represents a mouse whose sole purpose of living is to get from origin to the destination.
 */
public class Mouse {
    private Gene gene;
    private int curSteerIndex;
    private PVector pos;
    private PVector dir;
    private boolean hasExpired;
    private boolean survived;
    private static float radius = 15;
    private static float maxAcc = 15;
    public static float strokeWeight = 2;
    public static boolean tailVisible = true;
    public static int color;
    private ArrayList<PVector> prevPos;

    Mouse(Gene gene) {
        this.gene = gene;
        this.pos = new PVector(Simulation.origin.x, Simulation.origin.y);
        prevPos = new ArrayList<>(500);
        if (tailVisible) {
            prevPos.add(new PVector(pos.x, pos.y));
        }
        this.dir = new PVector(0, 0);
    }

    void display(PApplet parent) {
        if (tailVisible) drawTail(parent);
        parent.pushMatrix();
        parent.translate(pos.x, pos.y);
        parent.rotate((float) (this.dir.heading() + Math.PI / 2));
        parent.beginShape();
        parent.strokeWeight(strokeWeight);
        parent.stroke(color);
        parent.bezier(0, -radius / 2, -radius / 4, -radius / 6, -radius / 2, radius / 3, 0, radius / 2);
        parent.bezier(0, radius / 2, radius / 2, radius / 3, radius / 4, -radius / 6, 0, -radius / 2);
        parent.endShape();
        parent.popMatrix();
    }

    private void drawTail(PApplet parent) {
        parent.strokeWeight(0.5f); //or 2
        parent.stroke(color);
        parent.beginShape();
        for (int i = 0; i < prevPos.size()-1; i++) {
            parent.line(prevPos.get(i).x, prevPos.get(i).y, prevPos.get(i + 1).x, prevPos.get(i + 1).y);
        }
    }

    void update() {
        if (hasExpired()) return;
        if (curSteerIndex == gene.getSteers().size() - 1 || isOutOfBounds()) {
            hasExpired = true;
        }
        Steer extracted = gene.getSteers().get(curSteerIndex);
        if (extracted.isExpired()) {
            extracted.reset();
            curSteerIndex++;
            return;
        }
        PVector heading = extracted.getDirection();
        dir.add(heading);//.limit(maxAcc);
        pos.add(dir);
        if (tailVisible) prevPos.add(new PVector(pos.x, pos.y));
    }

    private boolean isOutOfBounds() {
        return pos.x - radius < 0
                || pos.x + radius > Simulation.w
                || pos.y - radius < 0
                || pos.y + radius > Simulation.h;
    }

    Mouse breedWith(Mouse other) {
        return new Mouse(this.gene.performMeiosisWith(other.gene));
    }

    boolean hasExpired() {
        return hasExpired;
    }

    static float getRadius() {
        return radius;
    }

    PVector getPos() {
        return this.pos;
    }

    Gene getGene() {
        return this.gene;
    }

    void expire() {
        this.hasExpired = true;
    }

    float distTo(PVector pos) {
        return sqrt(sq(this.pos.x - pos.x) + sq(this.pos.y - pos.y));
    }

    private float sq(float x) {
        return (float) Math.pow(x, 2);
    }

    private float sqrt(float x) {
        return (float) Math.sqrt(x);
    }

    public String toString() {
        return "dist to des: " + distTo(Simulation.destination);
    }

    void survive() {
        this.survived = true;
        this.expire();
    }

    boolean hasSurvived() {
        return survived;
    }

    int curIndex() {
        return curSteerIndex;
    }

    void setCurIndex(int index) {
        this.curSteerIndex = index;
    }

}
