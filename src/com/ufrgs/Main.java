package com.ufrgs;

import com.ufrgs.model.Entity;
import com.ufrgs.model.Rectangle;
import com.ufrgs.util.DataHelper;
import com.ufrgs.util.Display;
import com.ufrgs.util.Technique;
import com.ufrgs.view.Panel;

import javax.swing.*;

public class Main {

    // Settings
    public static Technique TECHNIQUE = Technique.SQUARIFIED_TREEMAP;
    public static Display DISPLAY = Display.STEP;

    private static Entity root;
    private static Rectangle canvas = new Rectangle(700, 700);

    public static void main(String[] args) {

        root = DataHelper.buildHierarchy("dataset/loc/gimp");

        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {

        JFrame frame = new JFrame("Dynamic Treemap");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Panel panel = new Panel(root, canvas, frame);
        frame.getContentPane().add(panel);
        frame.addKeyListener(panel);
        frame.setSize(700, 700);
        frame.setVisible(true);
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


//        JFrame ganttFrame = new JFrame("Gantt Chart");
//        ganttFrame.setSize(1650, 1080);
//        ganttFrame.getContentPane().add(new GanttPanel(root, new Rectangle(1600, 1000)));
//        ganttFrame.setVisible(true);
    }
}
