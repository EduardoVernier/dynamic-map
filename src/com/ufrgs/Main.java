package com.ufrgs;

import com.ufrgs.model.Entity;
import com.ufrgs.model.Rectangle;
import com.ufrgs.technique.Nmap;
import com.ufrgs.technique.SquarifiedTreemap;
import com.ufrgs.util.DataHelper;

public class Main {

    public static void main(String[] args) {

        Rectangle canvas = new Rectangle(800, 600);
        Entity root = DataHelper.buildHierarchy("dataset/exoplayer");
        SquarifiedTreemap squarifiedTreemap = new SquarifiedTreemap(root, canvas);
        root = squarifiedTreemap.getTreeRoot();
        Nmap nmap = new Nmap(root, canvas, 0);
    }
}
