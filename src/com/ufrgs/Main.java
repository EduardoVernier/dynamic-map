package com.ufrgs;

import com.ufrgs.model.Entity;
import com.ufrgs.model.Rectangle;
import com.ufrgs.util.DataHelper;
import com.ufrgs.util.Display;
import com.ufrgs.util.Technique;
import com.ufrgs.view.Panel;

import javax.swing.*;
import java.io.File;

public class Main {

    // Settings
    public static Technique TECHNIQUE = Technique.SLICE_AND_DICE;
    public static Display DISPLAY = Display.STEP;

    public static String technique;

    public static void main(String[] args) {

        File[] directories = new File("dataset").listFiles(File::isDirectory);

        for (File dirFile : directories) {

            DISPLAY = Display.ANALISYS;
            String dir = dirFile.toString();

            Entity root = DataHelper.buildHierarchy(dir);

            String dataset = dir.split("/")[dir.split("/").length-1];
            technique = "snd";

            SwingUtilities.invokeLater(() -> createAndShowGUI(dataset, root));
        }

    }

    private static void createAndShowGUI(String dataset, Entity root) {

        Rectangle canvas = new Rectangle(1000, 1000);
        JFrame frame = new JFrame("Dynamic Treemap");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Panel panel = new Panel(root, canvas, frame, dataset);
        frame.getContentPane().add(panel);
        frame.addKeyListener(panel);
        frame.setSize(1000, 1000);
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
