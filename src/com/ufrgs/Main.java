package com.ufrgs;

import com.ufrgs.model.Entity;
import com.ufrgs.util.DataHelper;

public class Main {

    public static void main(String[] args) {

        Entity root = DataHelper.buildHierarchy("dataset/ExoPlayer.full.data");

    }
}
