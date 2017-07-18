import processing.core.PVector;

/**
 * Created by Jiachen on 7/16/17.
 * This class represents a specific DNA in a gene.
 */
class Steer {
    private int lifeSpan;
    private int iterationsLeft;
    private float angle;

    Steer(int framesUntilExpiration, float angle) {
        this.lifeSpan = framesUntilExpiration;
        this.angle = angle;
        this.reset();
    }

    Steer(Steer clone){
        this.lifeSpan = clone.lifeSpan;
        this.angle = clone.angle;
        this.reset();
    }

    PVector getDirection() {
        float x = (float) Math.cos(angle);
        float y = (float) Math.sin(angle);
        iterationsLeft--;
        return new PVector(x, y).normalize().mult(0.3f);
    }

    int getLifeSpan() {
        return lifeSpan;
    }

    void setLifeSpan(int lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    boolean isExpired() {
        return iterationsLeft <= 0;
    }

    void reset() {
        iterationsLeft = lifeSpan;
    }

    float getAngle() {
        return angle;
    }

    void setAngle(float angle) {
        this.angle = angle;
    }
}
