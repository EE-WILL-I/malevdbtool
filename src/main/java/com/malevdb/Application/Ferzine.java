package com.malevdb.Application;

import java.util.ArrayList;
import java.util.Random;

public class Ferzine {
    private final int fieldSize = 8, figureNumber = 8;
    private ArrayList<int[]> figures = new ArrayList<>(8);

    private boolean checkPosition(int x, int y) {
        for(int[] figure : figures) {
            int fx = figure[0];
            int fy = figure[1];
            if(x == fx || y == fy)
                return false;
            if(Math.abs(x-fx) == Math.abs(y-fy))
                return false;
        }
        return true;
    }

    private boolean checkPositionBranch(int count) {
        if (count > figureNumber)
            return true;
        for (int x = 0; x < fieldSize; x++)
            for (int y = 0; y < fieldSize; y++)
                if (checkPosition(x, y)) {
                    figures.add(new int[]{x, y});
                    if(checkPositionBranch(count + 1))
                        return true;
                    else
                        figures.remove(count - 1);
                }
        return false;
    }

    public void findSolution() {
        if(checkPositionBranch(1)) {
            System.out.println(figures.size());
            for (int i = 0; i < figureNumber; i++)
                System.out.println(i + " x: " + figures.get(i)[0] + "; y: " + figures.get(i)[1]);
        }
    }
}
