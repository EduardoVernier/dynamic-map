package com.ufrgs.view;

import com.ufrgs.Main;
import com.ufrgs.model.Entity;
import com.ufrgs.model.Rectangle;
import com.ufrgs.technique.Nmap;
import com.ufrgs.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;



public class Panel extends JPanel implements KeyListener, ActionListener {

    // Data
    private Entity root;
    private Rectangle canvas;
    int revision = 0;
    // Drawing
    List<Entity> entityList;
    double maxWeight;
    Font bigFont = new Font("Bitstream Vera Sans", Font.PLAIN, 22);
    Font mediumFont = new Font("Bitstream Vera Sans", Font.PLAIN, 18);
    Font smallFont = new Font("Bitstream Vera Sans", Font.PLAIN, 14);
    // Animation
    private double lastRevisionWeight = 0;
    private double progress = 0.0;
    private Timer timer;
    private int DELAY = 0;

    public Panel(Entity root, Rectangle canvas) {

        this.root = root;
        this.canvas = canvas;
        this.setLayout(null);

        maxWeight = root.getMaximumWeight();
        entityList = new ArrayList<>();
        flattenTree(root);
        computeNmap();


        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void flattenTree (Entity parent) {

        entityList.add(parent);
        for (Entity child : parent.getChildren()) {
            if (child.isLeaf()) {
                entityList.add(child);
            } else {
                flattenTree(child);
            }
        }
    }

    private void computeNmap() {
        new Nmap(root, canvas, revision);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics = (Graphics2D) g;
        double maxWeight = 0;
        for (int i = 0; i < root.getNumberOfRevisions(); ++i) {
            if (root.getWeight(i) > maxWeight) {
                maxWeight = root.getWeight(i);
            }
        }

        double scale = (progress * (root.getWeight(revision)) + (1 - progress) * lastRevisionWeight) / maxWeight;
        graphics.scale(scale, scale);

        // Draw leafs
        for (Entity entity : entityList) {
            if (entity.getWeight(revision) > 0 && entity.isLeaf()) {
                entity.draw(graphics, progress);
            }
        }

        // Draw parents
        for (Entity entity : entityList) {
            if (entity.getWeight(revision) > 0 && !entity.isLeaf()) {
                entity.draw(graphics, progress);
            }
        }

        // Set font size
        if (scale > 0.8) {
            setFont(smallFont);
        } else if (scale > .5) {
            setFont(mediumFont);
        } else {
            setFont(bigFont);
        }

        // Draw labels
        for (Entity entity : entityList) {
            if (entity.getWeight(revision) > 0 && entity.isLeaf()) {
                entity.drawLabel(graphics, progress, scale);
            }
        }


        // Improves graphics on Linux
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    @Override
    public void keyPressed(KeyEvent keyEvent) {}

    @Override
    public void keyReleased(KeyEvent keyEvent) {

        if (keyEvent.getKeyCode() == KeyEvent.VK_X) {
            if (revision < root.getNumberOfRevisions() - 1) {
                lastRevisionWeight = root.getWeight(revision);
                revision++;
                progress = 0.0;
                computeNmap();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (progress < 1) {
            progress += 0.01;
            repaint();
        } else  {
            if (Main.DISPLAY == Constants.ANIMATION) {
                lastRevisionWeight = root.getWeight(revision);
                revision++;
                progress = 0.0;
                computeNmap();
            } else if (Main.DISPLAY == Constants.STEP) {
                progress = 1;
            }
        }
    }
}
