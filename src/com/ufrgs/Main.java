package com.ufrgs;

import com.ufrgs.model.Entity;
import com.ufrgs.technique.SquarifiedTreemap;
import com.ufrgs.util.DataHelper;

public class Main {

    public static void main(String[] args) {

        Entity root = DataHelper.buildHierarchy("dataset/ExoPlayer.full.data");
        SquarifiedTreemap squarifiedTreemap = new SquarifiedTreemap(root, 800, 600);
        root = squarifiedTreemap.getTreeRoot();
        root = root;
    }
}
