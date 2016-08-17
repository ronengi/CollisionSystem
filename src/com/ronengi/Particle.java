package com.ronengi;


import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.util.Random;


/**
 * Created by stimpy on 8/16/16.
 */
public class Particle {

    private static final double INFINITY = Double.POSITIVE_INFINITY;
    private double rx, ry;          // position
    private double vx, vy;          // velocity
    private final double radius;    // radius
    private final double mass;      // mass
    private int count;              // number of collisions

    public Particle(Particle[] particles, int myIndex) {
        Random rand = new Random();

        this.radius = 0.005;
        this.mass = 0.5;

        this.vx = (0.5 - rand.nextDouble()) / 7.0;
        this.vy = (0.5 - rand.nextDouble()) / 7.0;

        this.count = 0;

        this.rx = rand.nextDouble();
        this.ry = rand.nextDouble();

        if (rx < radius)        rx += radius;
        if ((1-rx) < radius)    rx -= radius;
        if (ry < radius)        ry += radius;
        if ((1-ry) < radius)    ry -= radius;

        for (int i = 0; i < particles.length; i++) {
            if (particles[i] == null)   break;
            if (i == myIndex)           continue;
            Particle other = particles[i];
            while (Math.abs(rx-other.rx) < (radius + other.radius)  &&  Math.abs(ry-other.ry) < (radius + other.radius)) {
                this.rx = rand.nextDouble();
                this.ry = rand.nextDouble();

                if (rx < radius)        rx += radius;
                if ((1-rx) < radius)    rx -= radius;
                if (ry < radius)        ry += radius;
                if ((1-ry) < radius)    ry -= radius;
            }
        }

    }

    public Particle(double rx, double ry, double vx, double vy, double radius, double mass, int count) {
        this.rx = rx;
        this.ry = ry;

        this.vx = vx;
        this.vy = vy;

        this.radius = radius;
        this.mass = mass;

        this.count = count;
    }


    public void move(double dt) {        // change position to reflect passage of time dt
        rx = rx + vx*dt;
        ry = ry + vy*dt;
    }


    private Color colorByMass() {
        if      (mass < 1.0)            return StdDraw.BLACK;
        else if (mass < 1.5)            return StdDraw.BOOK_LIGHT_BLUE;
        else if (mass < 2.0)            return StdDraw.BLUE;
        else if (mass < 2.5)            return StdDraw.GREEN;
        else if (mass < 3.0)            return StdDraw.LIGHT_GRAY;
        else if (mass < 3.5)            return StdDraw.DARK_GRAY;
        else if (mass < 4.0)            return StdDraw.ORANGE;
        else                            return StdDraw.RED;

    }

    public void draw() {
        // StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(colorByMass());
        StdDraw.filledCircle(rx, ry, radius);
    }

    public void draw(Color c) {
        // StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(c);
        StdDraw.filledCircle(rx, ry, radius);
    }

    public void mark(Color c) {
        // StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(c);
        StdDraw.circle(rx, ry, radius+0.01);
    }



    public double timeToHit(Particle that) {
        if (this == that) return INFINITY;
        double dx = that.rx - this.rx, dy = that.ry - this.ry;
        double dvx = that.vx - this.vx, dvy = that.vy - this.vy;
        double dvdr = dx*dvx + dy*dvy;
        if( dvdr >= 0) return INFINITY;      // no collision
        double dvdv = dvx*dvx + dvy*dvy;
        double drdr = dx*dx + dy*dy;
        double sigma = this.radius + that.radius;
        double d = (dvdr*dvdr) - dvdv * (drdr - sigma*sigma);
        if (d < 0) return INFINITY;         // no collision

        double dt = -((dvdr + Math.sqrt(d)) / dvdv);

        if (dt < 0)
            return 0.0;

        if (Double.isNaN(dt))
            return INFINITY;

        return dt;
    }

    public double timeToHitVerticalWall()  {
        if      (vx < 0.0)
            return (rx - this.radius) / -vx;           // left wall
        else if (vx > 0.0)
            return (1 - rx - this.radius) / vx;        // right wall
        return INFINITY;
    }

    public double timeToHitHorizontalWall() {
        if      (vy < 0.0)
            return (ry - this.radius) / -vy;           // bottom wall
        else if (vy > 0.0)
            return (1 - ry - this.radius) / vy;        // top wall
        return INFINITY;
    }



    public void bounceOff(Particle that) {
        double dx = that.rx - this.rx, dy = that.ry - this.ry;
        double dvx = that.vx - this.vx, dvy = that.vy - this.vy;
        double dvdr = dx*dvx + dy*dvy;
        double dist = this.radius + that.radius;
        double J = 2 * this.mass * that.mass * dvdr / ((this.mass + that.mass) * dist);
        double Jx = J * dx / dist;
        double Jy = J * dy / dist;
        this.vx += Jx / this.mass;
        this.vy += Jy / this.mass;
        that.vx -= Jx / that.mass;
        that.vy -= Jy / that.mass;
        this.count++;
        that.count++;
    }

    public void bounceOffVerticalWall() {
        this.vx = -this.vx;
        this.count++;
    }

    public void bounceOffHorizontalWall() {
        this.vy = -this.vy;
        this.count++;
    }


    public int getCount() {
        return count;
    }


}