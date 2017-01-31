package com.ufrgs;

import com.ufrgs.model.Entity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Panel extends JPanel {

    List<Entity> entityList;

    public Panel(Entity root) {

        entityList = new ArrayList<>();
        flattenTree(root);

    }

    private void flattenTree (Entity parent) {

        entityList.add(parent);
        for (Entity child : parent.getChildren()) {
            if (child.isLeaf()) {
                entityList.add(child);
            } else {
                flattenTree(child);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        for (Entity entity : entityList) {
            entity.getRectangle().paint(graphics);
        }
    }
}
