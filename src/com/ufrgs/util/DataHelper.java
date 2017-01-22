package com.ufrgs.util;

import com.ufrgs.model.Entity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DataHelper {

    private static List<Entity> parseCSV(String csvFile) {

        List<Entity> entityList = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
            String currentLine = bufferedReader.readLine();
            String[] header = currentLine.split(",");

            if (!header[0].equals("id") || !header[1].equals("weight")) {
                System.err.println("Error parsing csv file");
                System.exit(-1);
            }

            while ((currentLine = bufferedReader.readLine()) != null) {

                String[] split = currentLine.split(",");

                if (split.length != 2) {
                    System.err.println("Error parsing csv file");
                    System.exit(-1);
                } else {
                    Entity entity = new Entity(split[0], Double.parseDouble(split[1]));
                    entityList.add(entity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entityList;
    }

    public static Entity buildHierarchy(String csvFile) {

        List<Entity> entityList = parseCSV(csvFile);


        List<Entity> auxList = new ArrayList<>();
        Entity root = new Entity("", 0);
        auxList.add(root);

        // Lexicographic sort
        entityList.sort(Comparator.comparing(Entity::getId));

        for (int i = 0; i < entityList.size(); ++i) {
            Entity entity = entityList.get(i);
            int dividerIndex = entity.getId().lastIndexOf(".");

            if (dividerIndex != -1) {
                String prefix = entity.getId().substring(0, dividerIndex);

                if (contains(auxList, prefix)) {
                    // Add child to parent known parent
                    int parentIndex = find(auxList, prefix);
                    Entity parentEntity = auxList.get(parentIndex);
                    parentEntity.addChild(entity);
                    // Remove child from list
                    entityList.remove(i);
                    --i;
                } else {
                    // Initialize package with its first found child
                    Entity newParent = new Entity(prefix, entity.getWeight());
                    newParent.addChild(entity);
                    // Remove child from list
                    entityList.remove(i);
                    --i;
                    auxList.add(newParent);
                }
            } else {
                // Add to root
                root.addChild(entity);
                entityList.remove(i);
                --i;
            }
        }

        // Make root carry full tree
        Collections.reverse(auxList);
        for (int i = 0; i < auxList.size(); ++i) {
            for (int j = i + 1; j < auxList.size(); ++j) {
                Entity entityA = auxList.get(i);
                Entity entityB = auxList.get(j);

                if (entityA.getId().contains(entityB.getId())) {
                    entityB.addChild(entityA);
                    auxList.remove(i);
                    i--;
                    break;
                }
            }
        }

        return root;
    }

    private static int find(List<Entity> entityList, String prefix) {

        for (int i = 0; i < entityList.size(); ++i) {
            if (entityList.get(i).getId().equals(prefix)){
                return i;
            }
        }
        return -1;
    }

    private static boolean contains(List<Entity> entityList, String prefix) {

        for (Entity entity : entityList) {
            if (entity.getId().equals(prefix)){
                return true;
            }
        }
        return false;
    }
}
