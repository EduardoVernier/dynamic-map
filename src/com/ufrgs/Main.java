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
    public static Display DISPLAY = Display.ANALISYS;

    private static Entity root;
    private static Rectangle canvas;
    public static String outputDir;
    public static Technique technique;

    public static void main(String[] args) {

        if (args.length == 5) {
            technique = Technique.valueOf(args[0]);
            String inputDir = args[1];
            int width = Integer.valueOf(args[2]);
            int height = Integer.valueOf(args[3]);
            outputDir = args[4];

            canvas = new Rectangle(width, height);
            root = DataHelper.buildHierarchy(inputDir);
            SwingUtilities.invokeLater(() -> createAndShowGUI());
        } else {
            argsError();
        }
    }

    private static void createAndShowGUI() {

        Rectangle canvas = new Rectangle(1000, 1000);
        JFrame frame = new JFrame("Dynamic Treemap");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Panel panel = new Panel(root, canvas, frame, outputDir);
        frame.getContentPane().add(panel);
        frame.addKeyListener(panel);
        frame.setSize((int) canvas.width, (int) canvas.height);
        frame.setVisible(true);
    }

    private static void argsError() {
        System.out.println("Usage: java Main technique input_dir width height output_dir");
        System.out.println("Techniques:\n" +
                "\tNMAP_ALTERNATE_CUT,\n" +
                "\tNMAP_EQUAL_WEIGHT,\n" +
                "\tSQUARIFIED_TREEMAP,\n" +
                "\tORDERED_TREEMAP_PIVOT_BY_MIDDLE,\n" +
                "\tORDERED_TREEMAP_PIVOT_BY_SIZE,\n" +
                "\tSLICE_AND_DICE,\n" +
                "\tSTRIP,\n" +
                "\tSPIRAL.");
        System.out.println("Width and Height are given in pixels (integers).");
    }
}
