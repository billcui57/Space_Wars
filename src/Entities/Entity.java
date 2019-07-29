package Entities;

import GUI.SpacePanel;
import java.awt.Point;
import javafx.scene.shape.Circle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author billc
 */
public abstract class Entity {

    double mass;
    double x;
    double y;
    double velx = 0;
    double vely = 0;
    double accx = 0;
    double accy = 0;
    public int WIDTH;
    public int HEIGHT;
    final int GFIELD = 20;
    SpacePanel world;
    boolean fixed;

    /**
     * @param x x position 
     * @param y y position
     * @param velx x component of velocity
     * @param vely y component of velocity
     * @param mass mass
     * @param fixed fixed in space or not (subject to forces?)
     * @param world world it is in
     */
    public Entity(double x, double y, double velx, double vely, double mass, boolean fixed, SpacePanel world) {
        this.velx = velx;
        this.vely = vely;
        this.mass = mass;
        this.fixed = fixed;
        this.x = x;
        this.y = y;
        this.world = world;
    }

    /**
     * Gets distance from another Entity
     * @param other the other Entity
     * @return the distance
     */
    public double getDistanceFrom(Entity other) {
        //System.out.println(x + "\t" +other.x + "\t" +other.y+ "\t"+Math.pow(x-other.x, 2)+ "\t"+Math.pow(y-other.y, 2));
        return (Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2)));

    }

    /**
     * Tests if two Entities collide and removes them according to the following rules:
     * 1. If the two Entities are the same type (Spaceship, Body, etc..) they both get destroyed
     * 2. If one Entity is a Body then the other Entity is destroyed
     * 3. If one Entity is a Torpedo and the other is a Spaceship then the Spaceship is destroyed
     */
    public void testCollide() {
        
        for (int i = 0; i < world.getEntities().size(); i++) {
//           
//            if(this instanceof Body && !(world.getEntities().get(i) instanceof Body) && !(world.getEntities().get(i) instanceof MysteryBox)){
//            System.out.println(this.world.getEntities().get(i) + "\t"+this.getDistanceFrom(world.getEntities().get(i)) + "\t" + (this.WIDTH / 2 + world.getEntities().get(i).WIDTH / 2));
//            }
            if ((!this.world.getEntities().get(i).equals(this)) && ((this.getDistanceFrom(world.getEntities().get(i)) < this.WIDTH / 2 + world.getEntities().get(i).WIDTH / 2))) {
                
                if ((this.getClass() == this.world.getEntities().get(i).getClass()) && (this instanceof Torpedo) && (this instanceof Bomb)) {
                    this.world.rmEntity(i);
                    this.world.rmEntity(this);
                } else {
                    
                    if (this instanceof Body) {
                        this.world.rmEntity(i);
                       
                    } 
                    if (this instanceof Torpedo && this.world.getEntities().get(i) instanceof Spaceship){
                         this.world.rmEntity(i);
                    }
                }

            }
        }
    }

    public Point getPoint() {
        return new Point((int) this.x, (int) this.y);
    }

    public void testIfLeaveBoundary(){
          if((this.x>world.getWidth())||(this.x<0)||(this.y>world.getHeight())||(this.y<0)){
            world.rmEntity(this);
        }
    }
    
    
    public void updateAccDueToGravity() {
        
        if (!fixed) {
            for (int i = 0; i < world.getEntities().size(); i++) {
                if ((!world.getEntities().get(i).equals(this)) && ((world.getEntities().get(i) instanceof Body))) {
                    Body otherBody = (Body) world.getEntities().get(i);
                    double GMm = (GFIELD * mass * otherBody.mass);
                    double force = GMm / Math.pow(this.getDistanceFrom(otherBody), 2);

                    double forcex = force * ((otherBody.x - this.x) / getDistanceFrom(otherBody));
                    double forcey = force * ((otherBody.y - this.y) / getDistanceFrom(otherBody));

                    accx += forcex / mass;
                    accy += forcey / mass;

                }
            }
            
        }
    }

    public abstract void update();

    public abstract void draw();

}
