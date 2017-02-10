package com.ufrgs;

import com.ufrgs.model.Entity;
import com.ufrgs.model.Rectangle;
import com.ufrgs.technique.SquarifiedTreemap;
import com.ufrgs.util.Constants;
import com.ufrgs.util.DataHelper;
import com.ufrgs.view.Panel;

import javax.swing.*;

public class Main {

    // Settings
    public static boolean STRATEGY = Constants.ALTERNATE_CUT;
    public static boolean DISPLAY = Constants.STEP;

    private static Entity root;
    private static Rectangle canvas = new Rectangle(1200, 900);

    public static void main(String[] args) {

        // Prepare data for Dmap
        root = DataHelper.buildHierarchy("dataset/calcuta");
        SquarifiedTreemap squarifiedTreemap = new SquarifiedTreemap(root, canvas);
        root = squarifiedTreemap.getTreeRoot();

        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {

        JFrame frame = new JFrame("Dynamic Treemap");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Panel panel = new Panel(root, canvas);
        frame.add(panel);
        frame.addKeyListener(panel);
        frame.setSize(1200, 900);
        frame.setVisible(true);
    }
}
