package com.ufrgs.model;

import java.util.ArrayList;
import java.util.List;

public class Entity {

    private String id;
    private List<Double> weightList;
    private double normalizedWeight;
    private Point point;
    private Rectangle rectangle;
    private List<Entity> children;

    public Entity(String id, int numberOfRevisions) {

        this.id = id;
        // Initialize lists
        children  = new ArrayList<>();
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

    // Only used on Squarified Treemap
    public double getNormalizedWeight() {
        return normalizedWeight;
    }

    public void setNormalizedWeight(double normalizedWeight) {
        this.normalizedWeight = normalizedWeight;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
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

}
