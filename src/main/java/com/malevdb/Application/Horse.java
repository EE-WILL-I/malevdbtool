package com.malevdb.Application;

import java.util.Random;
import java.util.Stack;

public class Horse {
    private final int fieldSize = 8;
    private int[][] field;
    private final int[][] moves = {
            { -1, -2 },
            { -2, -1 },
            { -2, 1 },
            { -1, 2 },
            { 1, 2 },
            { 2, 1 },
            { 2, -1 },
            { 1, -2 }
    };
    private int cellCount;

    public Horse() {
        initField();
    }

    public void initField() {
        if(fieldSize <= 0)
            throw new IllegalArgumentException("Negative size");
        field = new int[fieldSize][fieldSize];
        for(int x = 0; x < fieldSize; x++)
            for(int y = 0; y < fieldSize; y++)
            field[x][y] = 0;
        cellCount = fieldSize * fieldSize;
    }

    public boolean checkStep(int x, int y) {
        return ((x >= 0 && x < fieldSize) && (y >= 0 && y < fieldSize));
    }

    public boolean checkCell(int x, int y) {
        return field[x][y] == 0;
    }

    public boolean checkMovementBranch(int x, int y, int counter) {
        field[x][y] = counter;
        if(counter >= cellCount - 1)
            return true;
        for(int[] move : moves) {
            int nx = x + move[0];
            int ny = y + move[1];
            if(checkStep(nx, ny) && checkCell(nx, ny) && checkMovementBranch(nx, ny, counter + 1)) {
                return true;
            }
        }
        field[x][y] = 0;
        return false;
    }

    public void findSolution() {
        Random r = new Random();
        int x, y;
        do {
            x = Math.abs(r.nextInt()) % fieldSize;
            y = Math.abs(r.nextInt()) % fieldSize;
            System.out.println("Start: x: " + x + "; y: " + y);
        }
        while(!checkMovementBranch(x, y, 1));
        printField();
    }

    public void printField() {
        for (int i = 0; i < fieldSize; i++)
            System.out.print("*");
        System.out.println("");
        for(int x = 0; x < fieldSize; x++) {
            for (int y = 0; y < fieldSize; y++) {
                String val = Integer.toString(field[x][y]);
                if(val.length() == 1)
                    System.out.print(val + "  ");
                else
                    System.out.print(val + " ");
            }
            System.out.println("");
        }
        for (int i = 0; i < fieldSize; i++)
            System.out.print("*");
        System.out.println("\n");
    }
}
