package com.ufrgs.technique;

import com.ufrgs.model.Entity;
import com.ufrgs.model.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Nmap {

    private int revision;

    public Nmap(Entity root, Rectangle rectangle, int revision) {

        this.revision = revision;
        nmap(root.getChildren(), rectangle);
    }

    private void nmap(List<Entity> entityList, Rectangle rectangle) {

        List<Entity> entityCopy = new ArrayList<>();
        entityCopy.addAll(entityList);

        for (Entity entity : entityCopy) {
            entity.setMovingPoint(entity.getAnchorPoint().x, entity.getAnchorPoint().y);
        }

        equalWeight(entityList, rectangle);

        for (Entity entity : entityCopy) {
            if (!entity.isLeaf()) {
                nmap(entity.getChildren(), entity.getRectangle());
            }
        }
    }

    private void equalWeight(List<Entity> entityList, Rectangle rectangle) {

        if (entityList.size() == 1) {
            // Done dividing
            entityList.get(0).setRectangle(rectangle);
            //entityList.get(0).setPoint(rectangle.x + rectangle.width/2, rectangle.y + rectangle.height/2);
            // System.out.println("ctx.rect(" + rectangle.x + ", " + rectangle.y + ", " + rectangle.width + ", " + rectangle.height + ");");
        } else {
            // Define if we should bisect the data vertically or horizontally and sort the data accordingly
            if (rectangle.width > rectangle.height) {
                entityList.sort((a, b) -> ((Double) a.getAnchorPoint().x).compareTo(b.getAnchorPoint().x));
            } else {
                entityList.sort((a, b) -> ((Double) a.getAnchorPoint().y).compareTo(b.getAnchorPoint().y));
            }

            int cutIndex = findCutElement(entityList);
            List<Entity> entityListA = entityList.subList(0, cutIndex);
            List<Entity> entityListB = entityList.subList(cutIndex, entityList.size());

            double sumA = entityListA.stream().mapToDouble(entity -> entity.getWeight(revision)).sum();
            double sumB = entityListB.stream().mapToDouble(entity -> entity.getWeight(revision)).sum();
            double sumTotal = sumA + sumB;
            Rectangle rectangleA, rectangleB;

            if (rectangle.width > rectangle.height) {

                double rectangleWidthA = (sumA / sumTotal) * rectangle.width;
                double rectangleWidthB = (sumB / sumTotal) * rectangle.width;
                double boundary = (entityListA.get(entityListA.size() - 1).getAnchorPoint().x + entityListB.get(0).getAnchorPoint().x) / 2;

                rectangleA = new Rectangle(rectangle.x, rectangle.y,
                        boundary - rectangle.x, rectangle.height);

                rectangleB = new Rectangle(rectangle.x + rectangleA.width, rectangle.y,
                        rectangle.width - rectangleA.width, rectangle.height);

                double affineMatrixA[] = {rectangleWidthA / rectangleA.width, 0, 0, 1, rectangle.x * (1 - (rectangleWidthA / rectangleA.width)), 0};

                double affineMatrixB[] = {rectangleWidthB / rectangleB.width, 0, 0, 1, (rectangle.x + rectangle.width) * (1 - (rectangleWidthB / rectangleB.width)), 0};

                affineTransformation(entityListA, affineMatrixA);
                affineTransformation(rectangleA, affineMatrixA);
                affineTransformation(entityListB, affineMatrixB);
                affineTransformation(rectangleB, affineMatrixB);

            } else {

                double rectangleHeightA = (sumA / sumTotal) * rectangle.height;
                double rectangleHeightB = (sumB / sumTotal) * rectangle.height;
                double boundary = (entityListA.get(entityListA.size() - 1).getAnchorPoint().y + entityListB.get(0).getAnchorPoint().y) / 2;

                rectangleA = new Rectangle(rectangle.x, rectangle.y,
                        rectangle.width, boundary - rectangle.y);
                rectangleB = new Rectangle(rectangle.x, rectangle.y + rectangleA.height,
                        rectangle.width, rectangle.height - rectangleA.height);

                double affineMatrixA[] = {1, 0, 0, rectangleHeightA / rectangleA.height, 0, rectangle.y * (1 - (rectangleHeightA / rectangleA.height))};
                double affineMatrixB[] = {1, 0, 0, rectangleHeightB / rectangleB.height, 0, (rectangle.y + rectangle.height) * (1 - (rectangleHeightB / rectangleB.height))};

                affineTransformation(entityListA, affineMatrixA);
                affineTransformation(rectangleA, affineMatrixA);
                affineTransformation(entityListB, affineMatrixB);
                affineTransformation(rectangleB, affineMatrixB);

            }

            equalWeight(entityListA, rectangleA);
            equalWeight(entityListB, rectangleB);
        }
    }


    private void affineTransformation(List<Entity> entityList, double m[]) {
        // Transform points
        for (Entity entity : entityList) {
            double x = entity.getMovingPoint().x * m[0] + entity.getMovingPoint().y * m[2] + m[4];
            double y = entity.getMovingPoint().x * m[1] + entity.getMovingPoint().y * m[3] + m[5];
            entity.setMovingPoint(x, y);
        }
    }

    private void affineTransformation(Rectangle rectangle, double m[]) {
        // Transform rectangle
        double x0, y0, x1, y1;
        x0 = rectangle.x * m[0] + rectangle.y * m[2] + m[4];
        y0 = rectangle.x * m[1] + rectangle.y * m[3] + m[5];

        x1 = (rectangle.x + rectangle.width) * m[0] + (rectangle.y + rectangle.height) * m[2] + m[4];
        y1 = (rectangle.x + rectangle.width) * m[1] + (rectangle.y + rectangle.height) * m[3] + m[5];

        rectangle.setValues(x0, y0, x1 - x0, y1 - y0);
    }

    private int findCutElement(List<Entity> entityList) {

        int cutElement = 1;
        double sumA = 0;
        double sumB = entityList.stream().mapToDouble(entity -> entity.getWeight(revision)).sum();

        double minDiff = Double.MAX_VALUE;
        for (int i = 0; i < entityList.size(); ++i) {
            sumA += entityList.get(i).getWeight(revision);
            sumB -= entityList.get(i).getWeight(revision);
            if (Math.abs(sumA - sumB) < minDiff) {
                minDiff = Math.abs(sumA - sumB);
                cutElement = i + 1;
            } else {
                break;
            }
        }
        return cutElement;
    }
}
