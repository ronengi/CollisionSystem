package com.ronengi;


import edu.princeton.cs.algs4.StdDraw;


/**
 * Created by stimpy on 8/16/16.
 */
public class CollisionSystem {

    private static final double INFINITY = Double.POSITIVE_INFINITY;
    private static final double TIME_UNIT = 0.01;

    private MinPQ<Event> pq;        // the priority queue
    private double sTime;           // simulation clock time
    private Particle[] particles;   // the array of particles
    private int N;                  // number of particles


    public CollisionSystem(Particle[] particles) {
        this.particles = particles;
        this.N = particles.length;
        this.sTime = 0.0;

        simulate();
    }


    /**
     * Add to PQ all particle-wall and particle-particle collisions involving this particle
     * @param a this particle
     */
    private void predict(Particle a) {
        if (a == null) return;
        double dt;

        for (int i = 0; i < this.N; i++) {                  // add events to collisions with other particles
            dt = a.timeToHit(particles[i]);
            if (dt < 0.0)   dt = 0.0;
            if (dt != INFINITY)
                pq.insert(new Event(dt, a, particles[i]));
        }

        dt = a.timeToHitVerticalWall();
        if (dt < 0.0)   dt = 0.0;
        if (dt != INFINITY)
            pq.insert(new Event(dt, a, null));       // add events to collisions with walls

        dt = a.timeToHitHorizontalWall();
        if (dt < 0.0)   dt = 0.0;
        if (dt != INFINITY)
            pq.insert(new Event(dt, null, a));
    }


    public void simulate() {
        pq = new MinPQ<Event>(50000);

        for (int i = 0; i < this.N; i++)                                        // add all collision events
            predict(particles[i]);

        pq.insert(new Event(0, null, null));                                    // first thing - redraw

        Event lastEvent = null, nextEvent = null;
        while (!pq.isEmpty()) {                                                 // simulation loop

            System.out.println(pq.size() + "events");

            Event event = pq.delMin();                                          // get next event

            if (!event.isValid())                                               // event particle/s were since involved in other collisions
                continue;


//            // mark this and last events
//            if (event.a != null)    event.a.mark(StdDraw.RED);
//            if (event.b != null)    event.b.mark(StdDraw.RED);
//            if (lastEvent != null) {
//                if (lastEvent.a != null) lastEvent.a.mark(StdDraw.GREEN);
//                if (lastEvent.b != null) lastEvent.b.mark(StdDraw.GREEN);
//            }
//            StdDraw.enableDoubleBuffering();
//            StdDraw.show();
//            StdDraw.pause(1);


            // System.out.println("Event: " + event.eTime);


            // simulation glitches / errors
            if (sTime - event.eTime >= 0.3) {                                    // check if event time has passed
                String msg = "Event time has passed by " + (sTime - event.eTime) + " seconds";
                if (lastEvent != null)
                    msg += "\n This event== " + event.eTime + ", Last event== " + lastEvent.eTime;
                throw new IllegalEventException(msg);
            }

            nextEvent = pq.min();                                               // check for simultaneous events
//            if (nextEvent.isValid()  &&  Math.abs(event.eTime - nextEvent.eTime) <= 0.0001)
//                throw new IllegalEventException("Simultaneous events");


            moveAll(event.eTime);                                               // get to event time: update positions and time


            Particle a = event.a;                                               // process this event
            Particle b = event.b;
            if      (a != null && b != null)    a.bounceOff(b);
            else if (a != null && b == null)    a.bounceOffVerticalWall();
            else if (a == null && b != null)    b.bounceOffHorizontalWall();
            else if (a == null && b == null)    redraw();


            // process here simultaneous events
            // ignore invalid events for (count + number of simultaneous events processed) only


            predict(a);                                                         // predict new events, based on changes
            predict(b);

            lastEvent = event;
        }
    }





    private void redraw() {
        int delay = 1;
        //System.out.println("redraw \t time: " + sTime + "\t events: " + pq.size());
        StdDraw.clear();
        for (int i = 0; i < this.N; i++)
            particles[i].draw();
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
        StdDraw.pause(delay);
    }


    /**
     * smooth motion until closest event
     *
     * @param eTime  event's time (target time)
     */
    private void moveAll(double eTime) {

        double time;
        for (time = sTime; time < eTime; time += TIME_UNIT) {       // move in TIME_UNIT steps
            for (int i = 0; i < this.N; i++)
                particles[i].move(TIME_UNIT);
            redraw();
        }

        for (int i = 0; i < this.N; i++)                            // complete final step
            particles[i].move(eTime - time);
        redraw();

        sTime = eTime;
    }



    private class Event implements Comparable<Event> {

        private double eTime;           // time of event
        private Particle a, b;          // particles involved in event
        private int countA, countB;     // collision counts for a and b


        public Event(double dt, Particle a, Particle b) {                        // create event

            if (Double.isNaN(dt))
                throw new IllegalEventException("illegal time: NaN");

            if (dt < 0.0)
                throw new IllegalEventException("negative time");

            this.eTime = dt + sTime;
            this.a = a;
            this.b = b;
            this.countA = (a != null) ? a.getCount() : 0;
            this.countB = (b != null) ? b.getCount() : 0;
        }


        public int compareTo(Event that) {                                      // ordered by time
            return Double.compare(this.eTime, that.eTime);
        }


        public boolean isValid() {                                              // invalid if intervening collision

            if (this.eTime == INFINITY)          return false;

            int oldCountA = (a != null) ? a.getCount() : 0;
            int oldCountB = (b != null) ? b.getCount() : 0;

            if (countA != oldCountA  ||  countB != oldCountB)
                return false;

            return true;
        }


    }


    public class IllegalEventException extends IllegalArgumentException {

        private String details;

        public IllegalEventException(String details) {
            this.details = details;
        }

        @Override
        public String toString() {
            return "Illegal event: " + details;
        }

    }


}
