package com.ufrgs;

import com.ufrgs.model.Entity;
import com.ufrgs.model.Rectangle;
import com.ufrgs.util.Constants;
import com.ufrgs.util.DataHelper;
import com.ufrgs.view.Panel;

import javax.swing.*;

public class Main {

    // Settings
    public static boolean DISPLAY = Constants.STEP;

    private static Entity root;
    private static Rectangle canvas = new Rectangle(1000, 1000);

    public static void main(String[] args) {

        // Prepare data for Dmap
        root = DataHelper.buildHierarchy("dataset/calcuta");

        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {

        JFrame frame = new JFrame("Ordered Treemap");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Panel panel = new Panel(root, canvas, frame);
        frame.getContentPane().add(panel);
        frame.addKeyListener(panel);
        frame.setSize(1000, 1000);
        frame.setVisible(true);

//        JFrame ganttFrame = new JFrame("Gantt Chart");
//        ganttFrame.setSize(1650, 1080);
//        ganttFrame.getContentPane().add(new GanttPanel(root, new Rectangle(1600, 1000)));
//        ganttFrame.setVisible(true);
    }
}
