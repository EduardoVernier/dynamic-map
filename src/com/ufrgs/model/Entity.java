package com.ufrgs.model;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Entity {

    private String id;
    private List<Double> weightList;
    private double normalizedWeight;
    private Point movingPoint, anchorPoint;
    private Rectangle rectangle, pastRectangle;
    private List<Entity> children;

    public Entity(String id, int numberOfRevisions) {

        this.id = id;
        // Initialize lists
        children = new ArrayList<>();
        weightList = new ArrayList<>(numberOfRevisions);
        for (int i = 0; i < numberOfRevisions; ++i) {
            weightList.add(0.0);
        }
    }

    public String getId() {
        return id;
    }

    public int getNumberOfRevisions() {
        return weightList.size();
    }

    public double getWeight(int index) {
        return weightList.get(index);
    }

    public void setWeight(double weight, int revision) {
        weightList.set(revision, weight);
    }

    public double getMaximumWeight() {
        return weightList.stream().max(Double::compare).get();
    }

    // Only used on Squarified Treemap
    public double getNormalizedWeight() {
        return normalizedWeight;
    }

    public void setNormalizedWeight(double normalizedWeight) {
        this.normalizedWeight = normalizedWeight;
    }

    public Point getAnchorPoint() {
        return anchorPoint;
    }

    public Point getMovingPoint() {
        return movingPoint;
    }

    public void initPoint(Point point) {
        anchorPoint = new Point(point.x, point.y);
        movingPoint = new Point(point.x, point.y);
    }

    public void setMovingPoint(double x, double y) {
        //pastPoint.setValues(point.x, point.y);
        movingPoint.setValues(x, y);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Rectangle getPastRectangle() {
        return pastRectangle;
    }

    public void setRectangle(Rectangle newRectangle) {

        if (this.rectangle == null) {
            pastRectangle = newRectangle;
        } else {
            pastRectangle = this.rectangle;
        }
        this.rectangle = newRectangle;
    }

    public void addChild(Entity entity) {

        children.add(entity);
        for (int revision = 0; revision < getNumberOfRevisions(); ++revision) {
            weightList.set(revision, weightList.get(revision) + entity.getWeight(revision));
        }
    }

    public List<Entity> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return children.size() == 0;
    }

    public void draw(Graphics g1D, double progress) {

        Graphics2D g = (Graphics2D) g1D;

        g.setColor(Color.BLACK);
        double x = rectangle.x * progress + pastRectangle.x * (1 - progress);
        double y = rectangle.y * progress + pastRectangle.y * (1 - progress);
        double width = rectangle.width * progress + pastRectangle.width * (1 - progress);
        double height = rectangle.height * progress + pastRectangle.height * (1 - progress);

        g.draw(new Rectangle2D.Double(x, y, width, height));

        if (isLeaf()) {
//            double px = point.x * progress + pastPoint.x * (1 - progress);
//            double py = point.y * progress + pastPoint.y * (1 - progress);

            g.draw(new Rectangle2D.Double(anchorPoint.x, anchorPoint.y, 2, 2));
            g.draw(new Rectangle2D.Double(movingPoint.x, movingPoint.y, 2, 2));
            g.draw(new Line2D.Double(anchorPoint.x, anchorPoint.y, movingPoint.x, movingPoint.y));
        }

    }
}
