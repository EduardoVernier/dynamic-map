package com.ufrgs.model;

import java.util.ArrayList;
import java.util.List;

public class Entity {

    private String id;
    private double weight;
    private double normalizedWeight;
    private Point point;
    private Rectangle rectangle;
    private List<Entity> children =  new ArrayList<>();

    public Entity(String id, double weight) {
        this.id = id;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

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
        weight += entity.getWeight();
    }

    public List<Entity> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return children.size() == 0;
    }

}
