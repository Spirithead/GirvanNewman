package com.company;

import java.util.Scanner;

public class Main {
    static int q = 6;

    public static void main(String[] args) {
        int[][] graph = createGraph();
        String[][] res = dijkstra(graph);
        for (int i = 0; i < q; i++) {
            for (int j = 0; j < q; j++) {
                System.out.print(res[i][j] + " ");
            }
            System.out.print('\n');
        }
    }

    public static String[][] createPaths(int[][] paths) {
        String[][] paths_s = new String[q][q];
        for (int i = 0; i < q; i++) {
            for (int j = 0; j < q; j++) {
                paths_s[i][j] = "";
            }

        }
        for (int x = 0; x < q; x++) {
            for (int i = 0; i < q; i++) {
                int point = i;
                if(point == x){
                    continue;
                }
                do {
                    paths_s[x][i] += point;
                    point = paths[x][point];
                } while (point != x);
                paths_s[x][i] += x;
            }
        }
        return paths_s;
    }

    public static String[][] dijkstra(int[][] graph) {
        int[][] lengths = new int[q][q];
        boolean[] marked = new boolean[q];
        int curr = 0;
        int[][] paths = new int[q][q];


        for (int x = 0; x < q; x++) {
            for (int i = 0; i < q; i++) {
                paths[x][i] = x;
            }
            lengths[x][x] = 0;
            for (int i = 0; i < q; i++) {
                marked[i] = false;
                if (i == x) continue;
                lengths[x][i] = graph[x][i];
            }
            for (int y = 0; y < q; y++) {

                marked[x] = true;

                for (int i = 0; i < q; i++) {
                    int minLength = 64;
                    if ((lengths[x][i] < minLength) && !marked[i] && i != x) {
                        curr = i;
                        minLength = lengths[x][i];
                    }
                }
                marked[curr] = true;

                for (int v = 0; v < q; v++) {
                    if (v != x && !marked[v] && (lengths[x][v] > (lengths[x][curr]) + graph[curr][v])) {
                        lengths[x][v] = (lengths[x][curr]) + graph[curr][v];
                        paths[x][v] = curr;
                    }
                }
            }
        }
        return createPaths(paths);
    }

    public static int[][] createGraph() {
        Scanner sc = new Scanner(System.in);
        int[][] graph = new int[q][q];
        for (int i = 0; i < q; i++) {
            for (int j = 0; j < q; j++) {
                graph[i][j] = 64;
            }
        }
        do {
            int x = sc.nextInt();
            int y = sc.nextInt();
            graph[x][y] = 1;
            graph[y][x] = 1;
        } while (sc.hasNextInt());

        return graph;
    }


}
