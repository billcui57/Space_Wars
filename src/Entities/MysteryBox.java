/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import World.Space;
import java.awt.Color;
import java.util.Random;

/**
 *
 * @author billc
 */
public class MysteryBox extends Entity{

    
    public MysteryBox(double x, double y, double velx, double vely, double mass, boolean fixed, Space world) {
        super(x, y, velx, vely, mass, fixed, world);
        Random ran = new Random();
        
        this.containedTorpedo=ran.nextInt(NUM_NONDEFAULT_TORPEDO_TYPES)+2;
        this.WIDTH=10;
        this.HEIGHT=10;
    }
    
    public MysteryBox(double x, double y, double velx, double vely, double mass, boolean fixed, int containedTorpedo, Space world) {
        super(x, y, velx, vely, mass, fixed, world);
        this.containedTorpedo=containedTorpedo;
        this.WIDTH=10;
        this.HEIGHT=10;
    }

    final int NUM_NONDEFAULT_TORPEDO_TYPES=2;
    int containedTorpedo;
    /**
    * 1 Default Torpedo
    * 2 Homing Torpedo
    */
    
    @Override
    public void testCollide(){
         for (int i = 0; i < world.getEntities().size(); i++) {
            if ((!this.world.getEntities().get(i).equals(this)) && (this.getDistanceFrom(world.getEntities().get(i)) < this.WIDTH / 2 + world.getEntities().get(i).WIDTH / 2)) {
               if(this.world.getEntities().get(i) instanceof Spaceship){
                   Spaceship ship = (Spaceship)this.world.getEntities().get(i);
                   ship.setTorpedoType(containedTorpedo);
                   world.rmEntity(this);
               }
            }
        }
    }
    
    @Override
    public void update() {
        this.testCollide();
        this.updateAccDueToGravity();
        System.out.println(this.x + "\t" + this.y);
    }
   
    public static MysteryBox generateNew(Space world){
        Random ran = new Random();
        
        return new MysteryBox(10+ran.nextInt(world.getWidth())-20, 10+ran.nextInt(world.getHeight())-20, 0, 0, 10, true, world);
    }

    
    @Override
    public void draw() {
        System.out.println("hi");
        world.g.setColor(Color.green);
        world.g.fillOval((int)this.x, (int)this.y, WIDTH, HEIGHT);
    }
    
}
