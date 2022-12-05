package com.company;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static int q = 6;
    static int max = 16;
    static int edgQ = 0;
    static int comQ = 1;
    static String[] vertexes = new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
    static String[] edges = new String[max];
    static String[] coms = new String[max];
    static double modularity;

    public static void main(String[] args) {
        int[][] graph = createGraph();
        coms = detectComm(graph);
        calcMod(graph);
        System.out.println("mod = " + modularity);
        edges = createEdges(graph);
        girvanNewmanIter(graph);
        calcMod(graph);
        System.out.println("mod = " + modularity);
        girvanNewmanIter(graph);
        calcMod(graph);
        System.out.println("mod = " + modularity);

    }

    public static void girvanNewmanIter(int[][] graph) {
        String[][] paths;
        paths = dijkstra(graph);
        calcEdgeBtw(edges, paths);
        deleteEdge(edges, graph);
        coms = detectComm(graph);
    }

    public static String[][] createPaths(int[][] paths) {
        String[][] paths_s = new String[max][max];
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                paths_s[i][j] = "";
            }
        }
        for (int x = 0; x < max; x++) {
            for (int i = 0; i < max; i++) {
                int point = i;
                if (!(point == x || paths[x][i] == i)) {
                    while(point != x){
                        paths_s[x][i] += point;
                        point = paths[x][point];
                    }
                    paths_s[x][i] += x;
                }
            }
        }
        return paths_s;
    }

    public static String[] createEdges(int[][] graph) {
        int pnt = 0;
        String[] edges = new String[max];
        for (int i = 0; i < q; i++) {
            for (int j = 0; j < i + 1; j++) {
                if (graph[i][j] == 1) {
                    edges[pnt] = i + Integer.toString(j) + "0";
                    pnt++;
                }
            }
        }
        return edges;
    }

    public static String[][] dijkstra(int[][] graph) {
        int[][] lengths = new int[max][max];
        boolean[] marked = new boolean[max];
        int curr = 0;
        int[][] paths = new int[max][max];


        for (int x = 0; x < max; x++) {


            for (int i = 0; i < max; i++) {
                paths[x][i] = x;
            }
            lengths[x][x] = 0;
            for (int i = 0; i < max; i++) {
                marked[i] = false;
                if (i != x) {
                    lengths[x][i] = graph[x][i];
                    //continue;
                }

            }
            marked[x] = true;
            for (int y = 0; y < max; y++) {

                int minLength = max;
                for (int i = 0; i < max; i++) {

                    if ((lengths[x][i] < minLength) && !marked[i] && i != x) {
                        curr = i;
                        minLength = lengths[x][i];
                    }
                }
                marked[curr] = true;

                for (int v = 0; v < max; v++) {
                    if (v != x && !marked[v] && (lengths[x][v] > (lengths[x][curr]) + graph[curr][v])) {
                        lengths[x][v] = (lengths[x][curr]) + graph[curr][v];
                        paths[x][v] = curr;
                    } else if (lengths[x][v] == max) {
                        paths[x][v] = v;
                    }
                }
            }
        }
        return createPaths(paths);
    }

    public static int[][] createGraph() {
        Scanner sc = new Scanner(System.in);
        int[][] graph = new int[max][max];
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                graph[i][j] = max;
            }
        }
        do {
            edgQ++;
            int x = sc.nextInt();
            int y = sc.nextInt();
            graph[x][y] = 1;
            graph[y][x] = 1;
        } while (sc.hasNextInt());

        return graph;
    }

    public static void calcEdgeBtw(String[] edges, String[][] paths) {
        for (int i = 0; i < q; i++) {
            for (int j = 0; j < q; j++) {
                char[] currPath = paths[i][j].toCharArray();
                int pnt = 0;
                if (i == j) continue;

                pntLoop:
                while (pnt + 1 < currPath.length) {
                    for (int k = 0; k < edges.length; k++) {
                        if (edges[k] == null) continue;
                        char[] currSample = new char[]{currPath[pnt], currPath[pnt + 1]};
                        char[] currEdge = new char[]{edges[k].charAt(0), edges[k].charAt(1)};
                        char[] currEdgeInv = new char[]{edges[k].charAt(1), edges[k].charAt(0)};


                        if (Arrays.equals(currSample, currEdge) || Arrays.equals(currSample, currEdgeInv)) {
                            int btw = Integer.parseInt(edges[k], 2, edges[k].length(), 10);
                            btw++;
                            edges[k] = new String(currEdge) + btw;
                            pnt++;
                            continue pntLoop;
                        }
                    }
                }

            }
        }

        for (int i = 0; i < edges.length; i++) {
            if (edges[i] != null) {
                int btw = Integer.parseInt(edges[i], 2, edges[i].length(), 10) / 2;
                edges[i] = edges[i].substring(0, 2) + btw;
            }
        }
    }

    public static void deleteEdge(String[] edges, int[][] graph) {
        int maxBtw = 0;
        char[] maxEdge = new char[2];
        int x, y;
        for (String edge : edges) {
            if (edge == null) continue;
            int btw = Integer.parseInt(edge, 2, edge.length(), 10);
            if (btw > maxBtw) {
                maxBtw = btw;
                maxEdge = edge.substring(0, 2).toCharArray();
            }
        }

        x = Character.getNumericValue(maxEdge[0]);
        y = Character.getNumericValue(maxEdge[1]);

        for (int i = 0; i < edges.length; i++) {
            if (edges[i] == null) continue;
            if (Arrays.equals(maxEdge, edges[i].substring(0, 2).toCharArray())) {
                edges[i] = null;
            }
        }
        graph[x][y] = max;
        graph[y][x] = max;
        edgQ--;
    }

    public static String[] detectComm(int[][] graph) {
        String[] communities = new String[max];
        for (int i = 0; i < max; i++) {
            communities[i] = "";
        }
        int pnt = 0;

        xLoop:
        for (int x = 0; x < q; x++) {
            for (int i = 0; i <= pnt; i++) {
                for (int pnt_c = 0; pnt_c < communities[i].length(); pnt_c++) {
                    int y = Integer.parseInt(communities[i].substring(pnt_c, pnt_c + 1));
                    if (graph[x][y] == 1) {
                        communities[i] += x;
                        vertexes[x] = String.valueOf(i);
                        continue xLoop;
                    }
                }
            }
            communities[pnt] += x;
            vertexes[x] = String.valueOf(pnt);
            pnt++;
            comQ = pnt;
        }

        return communities;
    }

    public static void calcVrtDegree(int[][] graph) {
        for (int x = 0; x < q; x++) {
            int dgr = 0;
            for (int y = 0; y < q; y++) {
                if (graph[x][y] == 1) {
                    dgr++;
                }
            }
            vertexes[x] += dgr;
        }
    }

    public static void calcMod(int[][] graph) {
        calcVrtDegree(graph);
        modularity = 0;
        /*for (int n = 0; n < comQ; n++) {

        }*/
        for (int i = 0; i < q; i++) {
            for (int j = 0; j < q; j++) {
                //if(i == j) continue;
                int di = Character.getNumericValue(vertexes[i].charAt(1));
                int dj = Character.getNumericValue(vertexes[j].charAt(1));
                int ci = Character.getNumericValue(vertexes[i].charAt(0));
                int cj = Character.getNumericValue(vertexes[j].charAt(0));
                modularity += (graph[i][j] - (((double) di * (double) dj) / (2 * edgQ))) * ((ci == cj) ? 1 : 0);
            }
        }

        modularity /= (2 * edgQ);
    }
}
