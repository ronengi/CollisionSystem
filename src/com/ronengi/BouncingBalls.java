package com.ronengi;


import edu.princeton.cs.algs4.StdDraw;

/**
 * Created by stimpy on 8/15/16.
 */
public class BouncingBalls {

    public static void main(String[] args) {

        int N = 600;
        Particle[] particles = new Particle[N];
        particles[0] = new Particle(0.5, 0.5, 0.0, 0.0, 0.03, 3.5, 0);
        for (int i = 1; i < N; i++)
            particles[i] = new Particle(particles, i);

//        int N = 6;
//        Particle[] particles = new Particle[N];
//        particles[0] = new Particle(0.42, 0.5, 0.00, 0.0, 0.02, 1, 0);
//        particles[1] = new Particle(0.46, 0.5, 0.00, 0.0, 0.02, 1, 0);
//        particles[2] = new Particle(0.50, 0.5, 0.00, 0.0, 0.02, 1, 0);
//        particles[3] = new Particle(0.54, 0.5, 0.00, 0.0, 0.02, 1, 0);
//        particles[4] = new Particle(0.66, 0.5, 0.05, 0.0, 0.02, 1, 0);
//        particles[5] = new Particle(0.70, 0.5, 0.05, 0.0, 0.02, 1, 0);

//        int N = 11;
//        Particle[] particles = new Particle[N];
//        particles[0]  = new Particle(0.50, 0.20, 0.00, 0.10, 0.02, 1.5, 0);
//        particles[1]  = new Particle(0.50, 0.75, 0.00, 0.00, 0.02, 1, 0);
//        particles[2]  = new Particle(0.48, 0.78, 0.00, 0.00, 0.02, 1, 0);
//        particles[3]  = new Particle(0.52, 0.78, 0.00, 0.00, 0.02, 1, 0);
//        particles[4]  = new Particle(0.46, 0.81, 0.00, 0.00, 0.02, 1, 0);
//        particles[5]  = new Particle(0.50, 0.81, 0.00, 0.00, 0.02, 1, 0);
//        particles[6]  = new Particle(0.54, 0.81, 0.00, 0.00, 0.02, 1, 0);
//        particles[7]  = new Particle(0.44, 0.84, 0.00, 0.00, 0.02, 1, 0);
//        particles[8]  = new Particle(0.48, 0.84, 0.00, 0.00, 0.02, 1, 0);
//        particles[9]  = new Particle(0.52, 0.84, 0.00, 0.00, 0.02, 1, 0);
//        particles[10] = new Particle(0.56, 0.84, 0.00, 0.00, 0.02, 1, 0);




        CollisionSystem cs = new CollisionSystem(particles);

        /*
        Ball[] balls = new Ball[N];
        for (int i = 0; i < N; i++)
            balls[i] = new Ball();
        while(true) {
            StdDraw.clear();
            for (int i = 0; i < N; i++) {
                balls[i].move(0.5);
                balls[i].draw();
            }
            StdDraw.enableDoubleBuffering();
            StdDraw.show();
            StdDraw.pause(30);
        }
        */
    }

}
