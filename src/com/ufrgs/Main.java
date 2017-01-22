package com.ufrgs;

import com.ufrgs.model.Entity;
import com.ufrgs.model.Rectangle;
import com.ufrgs.technique.Treemap;
import com.ufrgs.util.DataHelper;

public class Main {

    public static void main(String[] args) {

        Entity root = DataHelper.buildHierarchy("dataset/ExoPlayer.full.data");
        new Treemap(root, 800, 600);
    }
}
