package com.ufrgs;

import com.ufrgs.model.Entity;
import com.ufrgs.model.Rectangle;
import com.ufrgs.technique.Nmap;

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
    // Animation
    private double progress = 0.0;
    private Timer timer;
    private int DELAY = 3;

    public Panel(Entity root, Rectangle canvas) {

        this.root = root;
        this.canvas = canvas;


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
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        for (Entity entity : entityList) {
            entity.draw(graphics, progress);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    @Override
    public void keyPressed(KeyEvent keyEvent) {}

    @Override
    public void keyReleased(KeyEvent keyEvent) {

        if (keyEvent.getKeyCode() == KeyEvent.VK_X) {
            revision++;
            progress = 0.0;
            computeNmap();
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (progress < 1) {
            progress += 0.001;
            repaint();
        } else  {
            progress = 1;
        }
    }
}
