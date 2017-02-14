package com.ufrgs.util;

import com.ufrgs.model.Entity;

import javax.swing.*;
import java.awt.*;

public class TransparentText extends JPanel {

    private Entity entity;

    public TransparentText(Entity entity) {
        super();
        this.entity = entity;
    }

    public void paint(Graphics g) {

        Graphics2D graphics = (Graphics2D) g;

    }
}
