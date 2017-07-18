import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Simulation extends PApplet {
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Mouse> mice;
    private static Breeder breeder;
    private static int offset = 200;
    static Position origin;
    static Position destination;
    static float w;
    static float h;

    public static void main(String[] args) {
        String sketch = Thread.currentThread().getStackTrace()[1].getClassName();
        Thread proc = new Thread(() -> PApplet.main(sketch));
        proc.start();
    }

    public void settings() {
//        pixelDensity(2);
        size(1300, 600, FX2D);
    }

    public void setup() {
        Simulation.w = width;
        Simulation.h = height;

        //initialization of origin and destination
//        origin = new Position(width - 50, height / 2, color(0, 0, 255), 8);
//        destination = new Position(50, height / 2, color(255, 0, 0, 70), 35);
        int size = 35;
        origin = new Position((int) random(width - offset+size, width -size), (int) random(size, height - size), color(0, 255, 0, 70), size);
        destination = new Position((int) random(size, offset-size), (int) random(size, height - size), color(255, 0, 0,70), size);

        //initialization of the Breeder singleton
        breeder = new Breeder(1000, 100);

        //set up obstacles
        obstacles = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            PVector pos = getRandomPos();
            float length = (float) Math.random() * 200;
            obstacles.add(new Obstacle(this, pos.x, pos.y, length));
        }

        frameRate(300);

        //set up initial mice array
        mice = new ArrayList<>();
        mice.addAll(breeder.getInitialGeneration());
        Mouse.color = color(0, 255, 0);

    }

    private PVector getRandomPos() {
        float posX = (float) (Math.random()) * width;
        posX = posX > width - offset ? width - offset : posX < offset ? offset : posX;
        return new PVector(posX, (float) (Math.random()) * height);
    }

    public void draw() {
        background(255);

        //draws the origin and the destination
        origin.display(this);
        destination.display(this);

        //display the obstacles, check collision with mice
        for (Obstacle obstacle : obstacles) {
            obstacle.checkCollisionWith(mice);
            obstacle.display();
        }

        //display and updates the mice ArrayList.
        for (Mouse mouse : mice) {
            if (!mouse.hasExpired())
                mouse.update();
            mouse.display(this);
        }

        //check if any mice reached the destination
        if (checkCollisionWith(destination))
            setup();

        try {
            if (iterationCompleted()) {
                mice = breeder.nextGenerationOf(mice);
            }
        } catch (IllegalArgumentException e) {
            //fresh start!
            mice = breeder.getInitialGeneration();
        }
    }

    private boolean checkCollisionWith(PVector pos) {
        for (Mouse mouse : mice) {
            if (mouse.distTo(pos) < Mouse.getRadius() + destination.getRadius()) {
                mouse.survive();
                return true;
            }
        }
        return false;
    }

    private boolean iterationCompleted() {
        for (Mouse mouse : mice)
            if (!mouse.hasExpired())
                return false;
        return true;
    }

    public void keyPressed() {
        switch (key) {
            case 'T':
                Mouse.tailVisible = !Mouse.tailVisible;
                break;
            case 'R':
                setup();
                break;
            case 'E':
                mice = breeder.getInitialGeneration();
        }
    }

    public class Position extends PVector {
        private float size = 8;
        private int color = 0;

        Position(int x, int y, int color, int size) {
            super(x, y);
            this.size = size;
            this.color = color;
        }

        void display(PApplet parent) {
            parent.strokeWeight(size);
            parent.stroke(color);
            parent.point(super.x, super.y);
        }

        float getRadius() {
            return size / 2;
        }
    }

}
