/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import World.Space;
import java.awt.Color;

/**
 *
 * @author billc
 */
public class HomingTorpedo extends Torpedo {

    public HomingTorpedo(double x, double y, double velx, double vely, double mass, boolean fixed, Spaceship owner, Space world) {
        super(x, y, velx, vely, mass, fixed, world);
        this.owner = owner;
    }

    Spaceship owner;
    Spaceship target;
    double PROP_CONSTANT = 0.05;

    @Override
    public void update() {

        this.testCollide();
        this.accDueToGravity();

        if ((this.x > world.getWidth()) || (this.x < 0) || (this.y > world.getHeight()) || (this.y < 0)) {
            world.rmEntity(this);
        }

        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < world.getEntities().size(); i++) {
            if ((world.getEntities().get(i) instanceof Spaceship) && (!world.getEntities().get(i).equals(this.owner))) {
                if (this.distanceFrom(world.getEntities().get(i)) < minDistance) {
                    this.target = (Spaceship) world.getEntities().get(i);
                    minDistance = this.distanceFrom(target);
                }
            }
        }

        double dx=target.x - this.x;
        double dy=target.y - this.y;
        
       
        velx=dx*PROP_CONSTANT;
        vely=dy*PROP_CONSTANT;

        
        velx += accx;
        vely += accy;

        x += velx;
        y += vely;
    }

    @Override
    public void draw() {
        world.g.setColor(Color.blue);
        world.g.fillOval((int) x, (int) y, WIDTH, HEIGHT);
    }

}
