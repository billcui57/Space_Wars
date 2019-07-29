/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Entities.Spaceship;
import GUI.SpacePanel;
import java.awt.Color;

/**
 *
 * @author billc
 */
public class HomingTorpedo extends Torpedo {

    public HomingTorpedo(double x, double y, double velx, double vely, double mass, Spaceship owner, SpacePanel world) {
        super(x, y, velx, vely,mass, world);
        this.owner = owner;
    }

    Spaceship owner;
    Spaceship target;
    double PROP_CONSTANT = 0.001;

    @Override
    public void update() {
        accx=0;
        accy=0;
        System.out.println("hi");
        try {
            double minDistance = Double.MAX_VALUE;
            for (int i = 0; i < world.getEntities().size(); i++) {
                if ((world.getEntities().get(i) instanceof Spaceship) && (!world.getEntities().get(i).equals(this.owner))) {
                    if (this.getDistanceFrom(world.getEntities().get(i)) < minDistance) {
                        this.target = (Spaceship) world.getEntities().get(i);
                        minDistance = this.getDistanceFrom(target);
                    }
                }
            }
            
            System.out.println(target);
            
            double dx = target.x - this.x;
            double dy = target.y - this.y;
            accx+=dx * PROP_CONSTANT;
            accy+=dy * PROP_CONSTANT;
//
//            velx = dx * PROP_CONSTANT;
//            vely = dy * PROP_CONSTANT;
        } catch (NullPointerException e) {

        }

         super.update();
    }

    @Override
    public void draw() {
        world.g.setColor(Color.blue);
        world.g.fillOval((int) x - WIDTH / 2, (int) y - HEIGHT / 2, WIDTH, HEIGHT);
    }

}
