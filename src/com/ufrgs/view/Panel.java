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
import java.util.concurrent.TimeUnit;


public class Panel extends JPanel implements KeyListener, ActionListener {

    // Technique
    List<Entity> entityList;
    private Entity root;
    private Rectangle canvas;
    private int revision = 0;
    // Drawing
    private final JFrame frame;
    private double scale = 0;
    private final Font bigFont = new Font("Bitstream Vera Sans", Font.PLAIN, 22);
    private final Font mediumFont = new Font("Bitstream Vera Sans", Font.PLAIN, 18);
    private final Font smallFont = new Font("Bitstream Vera Sans", Font.PLAIN, 14);
    // Animation
    private double lastRevisionWeight = 0;
    private double progress = 0.0;
    private Timer timer;
    private int DELAY = 30;

    public Panel(Entity root, Rectangle canvas, JFrame frame) {

        this.root = root;
        this.canvas = canvas;
        this.frame = frame;
        this.setLayout(null);
        entityList = new ArrayList<>();

        flattenTree(root);
        for (Entity entity : entityList) {
            entity.setRectangle(null, 0);
        }
        root.setRectangle(canvas, 0);

        computeNmap();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void flattenTree (Entity entity) {

        entityList.add(entity);
        for (Entity child : entity.getChildren()) {
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

        scale = (progress * (root.getWeight(revision)) + (1 - progress) * lastRevisionWeight) / maxWeight;
        graphics.scale(scale, scale);

        // Draw leafs
        for (Entity entity : entityList) {
            if (entity.getWeight(revision) > 0 && entity.isLeaf()) {
                entity.draw(graphics, progress);
            }
        }

        // Draw borders
        for (Entity entity : entityList) {
            if (entity.getWeight(revision) > 0 && entity.isLeaf()) {
                entity.drawBorder(graphics, progress);
            }
        }

        for (Entity entity : entityList) {
            if (entity.getWeight(revision) > 0 && !entity.isLeaf()) {
                entity.drawBorder(graphics, progress);
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
        Entity.charWidth = graphics.getFontMetrics().stringWidth("X");
        graphics.setPaint(Color.BLACK);
        for (Entity entity : entityList) {
            if (entity.getWeight(revision) > 0 && entity.isLeaf()) {
                entity.drawLabel(graphics, progress);
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
                frame.setTitle("Dynamic - Revision " + revision);
            } else {
                printCsv();
            }
        }
    }

    private void printCsv() {

        for (int i = 0; i < root.getNumberOfRevisions(); ++i) {
            double sum = 0;
            for (Entity entity : entityList) {
                sum += entity.distanceList.get(i);
            }
            System.out.printf("%.8f\n", sum);
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (progress < 1) {
            progress += 0.02;
            repaint();
        } else  {
            if (Main.DISPLAY == Constants.ANIMATION) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lastRevisionWeight = root.getWeight(revision);
                revision++;
                progress = 0.0;
                computeNmap();
                frame.setTitle("Dynamic - Revision " + revision);
            } else if (Main.DISPLAY == Constants.STEP) {
                progress = 1;
            }
        }
    }
}
