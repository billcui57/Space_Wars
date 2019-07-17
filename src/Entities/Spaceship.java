package Entities;

import GUI.SpacePanel;
import Entities.Entity;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author billc
 */
public class Spaceship extends Entity {

    int playerNum;
    public boolean isAI;
    public Spaceship(double x, double y, double velx, double vely,double mass,int playerNum,boolean AI, SpacePanel world) {
        super(x, y, velx, vely, mass, false, world);
        commandedTurnLeft = false;
        commandedTurnRight = false;
        commandedPropel = false;
        this.isAI=AI;
        this.HEIGHT = 5;
        this.WIDTH = 5;
        orientAngle = 180;
        this.playerNum=playerNum;
        
        Random ran = new Random();
        switch(ran.nextInt(8)+1){
            case 1:
                tailColor=Color.BLUE;
                break;
            case 2:
                tailColor=Color.CYAN;
                break;
            case 3:
                tailColor=Color.GREEN;
                break;
            case 4:
                tailColor=Color.MAGENTA;
                break;
            case 5:
                tailColor=Color.ORANGE;
                break;
            case 6:
                tailColor=Color.PINK;
                break;
            case 7: 
                tailColor=Color.RED;
                break;
            case 8:
                tailColor=Color.YELLOW;
                break;
            
            
        }
    }

    Color tailColor;
    double MAXTHRUST = 0.05;
    public boolean commandedTurnLeft;
    public boolean commandedTurnRight;
    public boolean commandedPropel;
    public boolean commandedFire;
    public double orientAngle;
    double MUZZLE_VEL = 5;
    double FIRE_RATE = 500;
    boolean canFire = false;
    double timeLastFire;

    int equipedTorpedoType = 3;

    /**
     * 1 Default Torpedo 2 Homing Torpedo
     */
    public void setTorpedoType(int torpedo) {
        this.equipedTorpedoType = torpedo;
    }

    @Override
    public void update() {
        accx = 0;
        accy = 0;
        this.testCollide();
        this.updateAccDueToGravity();

        this.testIfLeaveBoundary();

        if (this.commandedTurnLeft) {
            orientAngle -= 0.1;
        }

        if (this.commandedTurnRight) {
            orientAngle += 0.1;
        }

        if (this.commandedPropel) {
            this.accx += MAXTHRUST * Math.cos(orientAngle);
            this.accy += MAXTHRUST * Math.sin(orientAngle);
        }

        if (System.currentTimeMillis() - timeLastFire > FIRE_RATE) {
            canFire = true;
        }

        if (this.commandedFire && canFire) {

            double torpedoVelx;
            double torpedoVely;
            if ((this.velx == 0) && (this.vely == 0)) {
                torpedoVelx = Math.abs(this.velx + MUZZLE_VEL) * Math.cos(orientAngle);
                torpedoVely = Math.abs(this.vely + MUZZLE_VEL) * Math.sin(orientAngle);
            } else {
                torpedoVelx = Math.abs(this.velx + Math.signum(velx) * MUZZLE_VEL) * Math.cos(orientAngle);
                torpedoVely = Math.abs(this.vely + Math.signum(vely) * MUZZLE_VEL) * Math.sin(orientAngle);
            }

            /**
             * 1 Default Torpedo 2 Homing Torpedo
             */
            switch (equipedTorpedoType) {
                case 1:
                    world.addEntity(new Torpedo(this.x + 20 * Math.cos(orientAngle), this.y + 20 * Math.sin(orientAngle), torpedoVelx, torpedoVely, 2, this.world));
                    break;
                case 2:
                    world.addEntity(new HomingTorpedo(this.x + 20 * Math.cos(orientAngle), this.y + 20 * Math.sin(orientAngle), torpedoVelx, torpedoVely, 2, this, this.world));
                    equipedTorpedoType = 1;
                    break;
                case 3:
                    world.addEntity(new ScatterTorpedo(this.x + 20 * Math.cos(orientAngle), this.y + 20 * Math.sin(orientAngle), torpedoVelx, torpedoVely, 2, this, this.world));
                    equipedTorpedoType = 1;
                    break;
                case 4:
                    world.addEntity(new Bomb(this.x + 20 * Math.cos(orientAngle), this.y + 20 * Math.sin(orientAngle), torpedoVelx, torpedoVely, 2, this, this.world));
                    equipedTorpedoType = 1;
                    break;
            }

            canFire = false;
            timeLastFire = System.currentTimeMillis();
        }

        velx += accx;
        vely += accy;

        x += velx;
        y += vely;

    }

//    public void AIControl(){
//        Spaceship target;
//        double minDis=Double.MAX_VALUE;
//        for(int i=0;i<world.getEntities().size();i++){
//            if((this.distanceFrom(world.getEntities().get(i))<minDis)&&(world.getEntities().get(i) instanceof Spaceship)){
//                minDis=this.distanceFrom(world.getEntities().get(i));
//                target=(Spaceship)world.getEntities().get(i);
//            }
//        }
//        
//    }
    public void startTurningLeft() {
        commandedTurnLeft = true;
    }

    public void stopTurningLeft() {
        commandedTurnLeft = false;
    }

    public void startTurningRight() {
        commandedTurnRight = true;
    }

    public void stopTurningRight() {
        commandedTurnRight = false;
    }

    public void startPropel() {
        commandedPropel = true;
    }

    public void stopPropel() {
        commandedPropel = false;
    }

    public void startFireTorpedo() {
        commandedFire = true;
    }

    public void stopFireTorpedo() {
        commandedFire = false;
    }

    public Color getColor(){
        return this.tailColor;
    }
    
    @Override
    public void draw() {
        
        world.g.setColor(tailColor);
        world.g.setStroke(new BasicStroke(3));
        world.g.drawLine((int) x + WIDTH / 4, (int) y + HEIGHT / 4, (int) (x - WIDTH * (Math.cos(orientAngle)) + WIDTH / 4), (int) (y - WIDTH * (Math.sin(orientAngle)) + HEIGHT / 4));
        world.g.setColor(Color.WHITE);
        world.g.fillOval((int) x - WIDTH / 2, (int) y - HEIGHT / 2, WIDTH, HEIGHT);
        world.g.setColor(tailColor);
        if(this.isAI){
            world.g.drawString("AI", (int)this.x, (int)this.y);

        }else{
            world.g.drawString("Player "+Integer.toString(playerNum), (int)this.x, (int)this.y);

        }
        
    }

}
