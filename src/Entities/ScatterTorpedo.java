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
public class ScatterTorpedo extends Torpedo {
    
    /*behaves like a regular torpedo but once it reaches a certain proximity with the enemy it
     will explode into several homing missiles
    */
    public ScatterTorpedo(double x, double y, double velx, double vely,double mass,Spaceship owner, SpacePanel world) {
        super(x, y, velx, vely,mass, world);
        this.owner=owner;
        this.WIDTH=10;
        this.HEIGHT=10;
    }
    
    Spaceship owner;
    Spaceship target;
     final double DETONATION_DISTANCE=200;
     final int PAYLOAD_NUM=20;
     final double EXPLOSION_RADIUS=5;
     final double EXPLOSION_VEL=2;
    
    @Override
    public void update() {
       
        
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
            
            if(minDistance<DETONATION_DISTANCE){
                double stepTheta=2*Math.PI/PAYLOAD_NUM;
                for(int i=0;i<PAYLOAD_NUM;i++){
                     world.addEntity(new HomingTorpedo(this.x+EXPLOSION_RADIUS*Math.cos(stepTheta*i),this.y+EXPLOSION_RADIUS*Math.sin(stepTheta*i),this.velx+EXPLOSION_VEL*Math.cos(stepTheta*i),this.vely+EXPLOSION_VEL*Math.sin(stepTheta*i),this.mass/PAYLOAD_NUM,this.owner,this.world));
                }
               
                world.rmEntity(this);
            }
            
            
        } catch (NullPointerException e) {

        }
        
        
        super.update();
        
        
    }

    @Override
    public void draw() {
        world.g.setColor(Color.MAGENTA);
        world.g.fillOval((int) x - WIDTH / 2, (int) y - HEIGHT / 2, WIDTH, HEIGHT);
    }

    
    
    
    
}
