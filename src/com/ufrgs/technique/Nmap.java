package com.ufrgs.technique;

import com.ufrgs.model.Entity;
import com.ufrgs.model.Rectangle;

import java.util.List;

public class Nmap {

    private Entity treeRoot;


    public Nmap(List<Entity> entityList, Rectangle rectangle) {
        treeRoot = equalWeight(entityList, rectangle);
    }

    private Entity equalWeight(List<Entity> entityList, Rectangle rectangle) {

        if (entityList.size() == 1) {
            entityList.get(0).setRectangle(rectangle);
            // TODO: set point as well when making it dynamic
        } else {

            // Define if we should bisect the data vertically or horizontally and sort the data accordingly
            if (rectangle.width > rectangle.height) {
                entityList.sort((a, b) -> ((Double) a.getPoint().x).compareTo(b.getPoint().x));
            } else {
                entityList.sort((a, b) -> ((Double) a.getPoint().y).compareTo(b.getPoint().y));
            }
            
            int cutIndex = findCutElement(entityList);
            List<Entity> entityListA = entityList.subList(0, cutIndex);
            List<Entity> entityListB = entityList.subList(cutIndex, entityList.size());

            double sumA = entityListA.stream().mapToDouble(Entity::getWeight).sum();
            double sumB = entityListB.stream().mapToDouble(Entity::getWeight).sum();
            double sumTotal = sumA + sumB;
            Rectangle rectangleA, rectangleB;

            if (rectangle.width > rectangle.height) {
                double rectangleWidthA = (sumA / sumTotal) * rectangle.width;
                double rectangleWidthB = (sumB / sumTotal) * rectangle.width;
            }

        }
    }

    private int findCutElement(List<Entity> entityList) {

        int cutElement = 1;
        double sumA = 0;
        double sumB = entityList.stream().mapToDouble(Entity::getWeight).sum();

        double minDiff = Double.MAX_VALUE;
        for(int i = 0; i < entityList.size(); ++i){
            sumA += entityList.get(i).getWeight();
            sumB -= entityList.get(i).getWeight();
            if(Math.abs(sumA - sumB) < minDiff){
                minDiff = Math.abs(sumA - sumB);
                cutElement = i + 1;
            } else {
                break;
            }
        }
        return cutElement;
    }
}
