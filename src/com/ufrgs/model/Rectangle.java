package com.ufrgs.model;

public class Rectangle {

    public double x, y, width, height;

    public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle(double width, double height) {
        this.x = 0;
        this.y = 0;
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

    public void setValues(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean intersects(Rectangle b) {
        return (((this.x + this.width > b.x) || (b.x + b.width > this.x)) &&
                ((this.y + this.height > b.y) || (b.y + b.height > this.y)));
    }

    public Rectangle intersection(Rectangle b) {

        double x0 = Math.max(this.x, b.x);
        double y0 = Math.max(this.y, b.y);
        double x1 = Math.min(this.x + this.width, b.x + b.width);
        double y1 = Math.min(this.y + this.height, b.y + b.height);
        return new Rectangle(x0, y0, x1 - x0, y1 - y0);
    }
}
