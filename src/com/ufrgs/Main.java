package com.ufrgs;

import com.ufrgs.model.Entity;
import com.ufrgs.model.Rectangle;
import com.ufrgs.technique.Nmap;
import com.ufrgs.technique.SquarifiedTreemap;
import com.ufrgs.util.DataHelper;

import javax.swing.*;

public class Main {

    private static Entity root;

    public static void main(String[] args) {

        Rectangle canvas = new Rectangle(800, 600);
        root = DataHelper.buildHierarchy("dataset/exoplayer");
        SquarifiedTreemap squarifiedTreemap = new SquarifiedTreemap(root, canvas);
        root = squarifiedTreemap.getTreeRoot();
        new Nmap(root, canvas, 0);


        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? " +
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Swing Paint Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new Panel(root));
        f.setSize(800, 600);
        f.setVisible(true);
    }
}
