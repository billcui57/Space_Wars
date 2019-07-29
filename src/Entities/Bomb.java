/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import GUI.SpacePanel;
import java.awt.Color;

/**
 *
 * @author billc
 */
public class Bomb extends Entity {

    final double DECELERATION = 0.07;

    Spaceship owner;
    Spaceship target;
    final double DETONATION_DISTANCE = 100;
    final int PAYLOAD_NUM = 20;
    final double EXPLOSION_RADIUS = 30;
    final double EXPLOSION_VEL = 2;

    public Bomb(double x, double y, double velx, double vely, double mass,Spaceship owner, SpacePanel world) {
        super(x, y, velx, vely, mass, false, world);
        this.WIDTH = 10;
        this.HEIGHT = 10;
        this.owner = owner;
    }

    @Override
    public void update() {
        
        this.testCollide();
        accx = 0;
        accy = 0;

        if ((this.x > world.getWidth()) || (this.x < 0) || (this.y > world.getHeight()) || (this.y < 0)) {
            world.rmEntity(this);
        }

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

            if (minDistance < DETONATION_DISTANCE) {
                double stepTheta = 2 * Math.PI / PAYLOAD_NUM;
                for (int i = 0; i < PAYLOAD_NUM; i++) {
                    world.addEntity(new Torpedo(this.x + EXPLOSION_RADIUS * Math.cos(stepTheta * i), this.y + EXPLOSION_RADIUS * Math.sin(stepTheta * i), this.velx + EXPLOSION_VEL * Math.cos(stepTheta * i), this.vely + EXPLOSION_VEL * Math.sin(stepTheta * i), this.mass / PAYLOAD_NUM, this.world));
                }

                world.rmEntity(this);
            }

        } catch (NullPointerException e) {

        }

        if (this.velx > 0) {
            accx = -DECELERATION;
        } else if (this.velx < 0) {
            accx = DECELERATION;
        } else {
            accx = 0;
        }

        if (this.vely > 0) {
            accy = -DECELERATION;
        } else if (this.vely < 0) {
            accy = DECELERATION;
        } else {
            accy = 0;
        }

        velx += accx;
        vely += accy;

        x += velx;
        y += vely;

    }

    @Override
    public void draw() {
        world.g.setColor(Color.LIGHT_GRAY);
        world.g.fillOval((int) x - WIDTH / 2, (int) y - HEIGHT / 2, WIDTH, HEIGHT);
    }

}
