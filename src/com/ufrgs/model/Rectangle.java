package com.ufrgs.model;

public class Rectangle {

    public double x, y, width, height;

    public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getShortEdge() {
        return (width < height) ? width : height;
    }

    public Rectangle cutArea(double area) {

        if (width >= height) {
            double areaWidth = area / height;
            double newWidth = width - areaWidth;
            return new Rectangle(x + areaWidth, y, newWidth, height);
        } else {
            double areaHeight = area / this.width;
            double newheight = this.height - areaHeight;
            return new Rectangle(x, y + areaHeight, width, newheight);
        }
    }
}
