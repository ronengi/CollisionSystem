package com.ronengi;


import edu.princeton.cs.algs4.StdDraw;
import java.util.Random;

/**
 * Created by stimpy on 8/15/16.
 */
public class Ball {

    private double rx, ry;          // position
    private double vx, vy;          // velocity
    private final double radius;    // radius


    public Ball() {
        // initialize radius, position and velocity
        Random rand = new Random();

        radius = 0.01;

        rx = rand.nextDouble();
        ry = rand.nextDouble();

        vx = rand.nextDouble() / 100.0;
        vy = rand.nextDouble() / 100.0;
    }


    public void move(double dt) {
        if ((rx + vx*dt < radius)  ||  (rx + vx*dt > 1.0 - radius)) {   vx = -vx;   }   // check for collision with walls
        if ((ry + vy*dt < radius)  ||  (ry + vy*dt > 1.0 - radius)) {   vy = -vy;   }
        rx = rx + vx*dt;
        ry = ry + vy*dt;
    }


    public void draw() {
        StdDraw.filledCircle(rx, ry, radius);
    }


}
