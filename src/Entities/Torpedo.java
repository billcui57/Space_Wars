package Entities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import Entities.Entity;
import GUI.SpacePanel;
import java.awt.Color;

/**
 *
 * @author billc
 */
public class Torpedo extends Entity{

    public Torpedo(double x, double y, double velx, double vely, double mass, boolean fixed, SpacePanel world) {
        super(x, y, velx, vely, mass, fixed, world);
        this.WIDTH = 5;
        this.HEIGHT = 5;
        
    }

 
    
    @Override
    public void update() {
        accx = 0;
        accy = 0;
        this.testCollide();
        this.updateAccDueToGravity();

        
        if((this.x>world.getWidth())||(this.x<0)||(this.y>world.getHeight())||(this.y<0)){
            world.rmEntity(this);
        }
        
        velx += accx;
        vely += accy;

        x += velx;
        y += vely;
    }

    @Override
    public void draw() {
        world.g.setColor(Color.red);
        world.g.fillOval((int) x - WIDTH / 2, (int) y - HEIGHT / 2, WIDTH, HEIGHT);
    }

}
