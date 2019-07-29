package GUI;

import Entities.Body;
import Entities.Entity;
import Entities.MysteryBox;
import Entities.Spaceship;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author billc
 */
public class SpacePanel extends javax.swing.JPanel {

    /**
     * Creates new form Space
     */
    int numPlayers;

    public SpacePanel(int numPlayers, int selectedMap) {
        initComponents();
        this.preset = preset;
        this.setBackground(Color.BLACK);
        this.validate();
        this.numPlayers = numPlayers;
        this.preset = selectedMap;
    }

    int preset;

    ArrayList<Entity> entities = new ArrayList<Entity>();
    ArrayList<Point> coords = new ArrayList<Point>();
    boolean showLines = false;
    boolean tracePaths = false;
    public boolean updateBodies = true;

    /**
     * @return ArrayList of Entities
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    /**
     * Adds a new Entity at the end of the ArrayList of Entities
     *
     * @param newEntity the new Entity
     */
    public void addEntity(Entity newEntity) {
        this.entities.add(newEntity);

    }

    /**
     * Removes a specified Entity from the ArrayList of Entities
     *
     * @param entity
     */
    public void rmEntity(Entity entity) {
        this.entities.remove(entity);
    }

    /**
     * Removes the Entity at a specified index from the ArrayList of Entities
     *
     * @param index
     */
    public void rmEntity(int index) {
        this.entities.remove(index);
    }

    boolean initialized = false;

    Spaceship player1;
    Spaceship player2;

    public Graphics2D g;

    /**
     * The map drawing and updating method
     *
     * @param g graphics
     */
    public void paintComponent(Graphics g) {
        if (!initialized) {
            mapSetUp();
        }
        super.paintComponent(g);


//        for(int i=0;i<entities.size();i++){
//            if(entities.get(i) instanceof Body){
//                System.out.println("p1 " + entities.get(i).getDistanceFrom(player1) + "\t" + (entities.get(i).WIDTH + player1.WIDTH));
//                System.out.println("p2 " +entities.get(i).getDistanceFrom(player2)+ "\t" + (entities.get(i).WIDTH + player2.WIDTH));
//            }
//        }

        this.g = (Graphics2D) g;
        this.requestFocus();

        updateAndDrawEntities();

        ensureMysteryBoxPresent();

//        testWin();
    }

    /**
     * Set up initial positions and velocities of players and bodies
     */
    private void mapSetUp() {
        double INITIALRADIUS = 80;
        double INITIALVELOCITY = 1;

        switch (numPlayers) {
            case 1:

                player1 = new Spaceship(10, 10, 0, 0, 20, 1, false, this);
                player2 = new Spaceship(this.getWidth() - 10, this.getHeight() - 10, 0, 0, 20, 2, true, this);
                break;
            case 2:
                player1 = new Spaceship(10, 10, 0, 0, 20, 1, false, this);
                player2 = new Spaceship(this.getWidth() - 10, this.getHeight() - 10, 0, 0, 20, 2, false, this);
                break;
        }

        switch (preset) {
            //1 sun
            case 1:
                entities.add(new Body(this.getWidth() / 2, this.getHeight() / 2, 0, 0, 40, true, this));
                break;
            //2 suns not fixed
            case 2:

                entities.add(new Body(this.getWidth() / 2 + INITIALRADIUS, this.getHeight() / 2, 0, INITIALVELOCITY, 20, false, this));
                entities.add(new Body(this.getWidth() / 2 - INITIALRADIUS, this.getHeight() / 2, 0, -INITIALVELOCITY, 20, false, this));
                break;
            // 3 suns fixed
            case 3:

                entities.add(new Body(this.getWidth() / 2, this.getHeight() / 2 - INITIALRADIUS, 0, 0, 40, true, this));
                entities.add(new Body(this.getWidth() / 2 - INITIALRADIUS * Math.cos(Math.PI / 6), this.getHeight() / 2 + INITIALRADIUS * Math.sin(Math.PI / 6), 0, 0, 20, true, this));
                entities.add(new Body(this.getWidth() / 2 + INITIALRADIUS * Math.cos(Math.PI / 6), this.getHeight() / 2 + INITIALRADIUS * Math.sin(Math.PI / 6), 0, 0, 20, true, this));
                break;

            // no suns
            case 4:
                break;

        }
        entities.add(player1);
        entities.add(player2);

        initialized = true;

    }

    /**
     * Updates Entities if needed and draws them
     */
    private void updateAndDrawEntities() {
        try {
            for (int i = 0; i < entities.size(); i++) {
                if (updateBodies) {
                    this.entities.get(i).update();
                }
                this.entities.get(i).draw();

            }
        } catch (IndexOutOfBoundsException e) {

        }
    }

    /**
     * Ensures that there always exists a Mystery Box in the map
     */
    private void ensureMysteryBoxPresent() {
        boolean hasMysteryBox = false;
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i) instanceof MysteryBox) {
                hasMysteryBox = true;
            }
        }

        if (!hasMysteryBox) {
            entities.add(MysteryBox.generateNew(this));
        }
    }

    /**
     * Tests win conditions
     */
    private void testWin() {
        if (!entities.contains(player1)) {
            g.setColor(player2.getColor());
            g.setFont(new Font("Big boi font", Font.BOLD, 30));
            if (player2.isAI) {
                g.drawString("AI wins!", this.getWidth() / 2, this.getHeight() / 2);
            } else {
                g.drawString("Player 2 wins!", this.getWidth() / 2, this.getHeight() / 2);
            }
            updateBodies = false;
        }

        if (!entities.contains(player2)) {
            g.setColor(player1.getColor());
            g.setFont(new Font("Big boi font", Font.BOLD, 30));
            if (player1.isAI) {
                g.drawString("AI wins!", this.getWidth() / 2, this.getHeight() / 2);
            } else {
                g.drawString("Player 1 wins!", this.getWidth() / 2, this.getHeight() / 2);
            }
            updateBodies = false;
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(800, 800));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    double beginx;
    double beginy;
    double endx;
    double endy;

    boolean pressing;

    /**
     * Key pressed registration for controlling players
     *
     * @param evt
     */
    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
        switch (evt.getKeyChar()) {
            case 'a':
                player1.startTurningLeft();
                break;
            case 'd':
                player1.startTurningRight();
                break;
            case 'w':
                player1.startPropel();
                break;
            case ' ':
                player1.startFireTorpedo();
                break;
            case 'j':
                player2.startTurningLeft();
                break;
            case 'l':
                player2.startTurningRight();
                break;
            case 'i':
                player2.startPropel();
                break;
            case '.':
                player2.startFireTorpedo();
                break;
        }

    }//GEN-LAST:event_formKeyPressed

    /**
     * Key released registration for controlling players
     *
     * @param evt
     */
    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        // TODO add your handling code here:
        switch (evt.getKeyChar()) {
            case 'a':
                player1.stopTurningLeft();
                break;
            case 'd':
                player1.stopTurningRight();
                break;
            case 'w':
                player1.stopPropel();
                break;
            case ' ':
                player1.stopFireTorpedo();
                break;
            case 'j':
                player2.stopTurningLeft();
                break;
            case 'l':
                player2.stopTurningRight();
                break;
            case 'i':
                player2.stopPropel();
                break;
            case '.':
                player2.stopFireTorpedo();
                break;
        }
    }//GEN-LAST:event_formKeyReleased

    public Timer t1;

    public void timer() {
        t1 = new Timer(10, new TimerListener());
    }

    private class TimerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            SpacePanel.super.repaint();

        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
