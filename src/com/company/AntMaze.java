package com.company;

import java.util.*;

public class AntMaze {

    // M x N matrix
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private int M;
    private int N;
    private int min_dist = 0;
    private static int kroki = 0;
    private int sciezki = 0;
    private int iledoNajkr = 0;
    private int ktoraTrasa;

    // ############## EDYCJA CZYNNIKÓW ################
    private final float wartoscStartowaFeromonu = 1.0F;
    private final float wspolczynnikParowania = 0.65F;
    private final float Q = 2.0F;
    // ################################################

    private List<Node> listaNode = new ArrayList<>();
    //private List<Integer> prawdopodobienstwo = new ArrayList<>();
    private List<Node> tempNodes = new ArrayList<>();
    private List<Node> randomNodes = new ArrayList<>();
    private List<Node> ostatecznaLista = new ArrayList<>();
    private List<Node> uzyty = new ArrayList<>();
    private static float feromony[][] = new float[0][0];
    private int matKopia[][];
    private int matTrasaKopia[][];
    public static int pokaztrase = 0;

    // Below arrays details all 4 possible movements from a cell
    private final int row[] = { -1, 0, 0, 1 };
    private final int col[] = { 0, -1, 1, 0 };

    private int wspolTrasa[][] = new int[9000000][2]; // wierzcholki wybranej trasy
    //private int odwiedzone[][] = new int [9000000][2]; // odwiedzone wierzchołki

    //private Node trasa[] = new Node[9000000];
    private int numerNajkrotszej = 0;

    public int stworzListe(Node node){
        Node tmp = node;
        while(tmp != null){
            listaNode.add(tmp);
            tmp = tmp.parent;
        }
        return listaNode.size();
    }
    public void wyczyscListe()
    {
        listaNode.clear();
    }


    public AntMaze(int m, int n){
        M = m; // przypisz rozmiar labiryntu
        N = n;

        if(feromony.length == 0){
            feromony = new float[M][N];
            for(int i = 0; i < M; i++)
                for(int j = 0; j< N; j++)
                    feromony[i][j] = wartoscStartowaFeromonu;
        }
    }

    private boolean isValid(int mat[][], int row, int col, boolean visited[][])
    {
        return (row >= 0) && (row < M) && (col >= 0) && (col < N)
                && mat[row][col] == 0 && !visited[row][col];
    }


    public void startAlgorithm(int mat[][], int i, int j, int x, int y) // ## te klase odpalamy w mainie ##
    {
        matKopia = mat;
        // construct a matrix to keep track of visited cells
        boolean[][] visited = new boolean[M][N];

        Stack<Node> q = new Stack<>();

        // mark source cell as visited and enqueue the source node
        visited[i][j] = true;
        q.add(new Node(i, j, 0, null));

        // stores length of longest path from source to destination
        min_dist = Integer.MAX_VALUE;
        Node node = null;

        // run till queue is not empty
        while (!q.isEmpty())
        {

            //System.out.println("Kolejka: " + q);
            // pop front node from queue and process it
            node = q.pop();

            i = node.x;
            j = node.y;

            feromony[i][j] += Q;
            //System.out.println(node);
            int dist = node.dist;
            //System.out.println("["+node.x + ", " + node.y + "] - " + node.getManhattan());
            //odwiedzone[kroki][0] = i;
            //odwiedzone[kroki][1] = j;

            kroki++;
            // if destination is found, update min_dist and stop
            if (i == x && j == y)
            {

                //trasa[sciezki] = node;
                sciezki++;
                min_dist = stworzListe(node);
                numerNajkrotszej = sciezki;
                iledoNajkr = kroki;
                //odparowanie();

                break;
            }

            // sprawdz komórki wokół wierzchołka, jeżeli wolne to dodaj do kolejki
            for (int k = 0; k < 4; k++)
            {
                if (isValid(mat, i + row[k], j + col[k], visited))
                {
                    visited[i + row[k]][j + col[k]] = true;
                    tempNodes.add(new Node(i + row[k], j + col[k], dist + 1, node));
                    //System.out.println(tempNodes.size());
                    //q.add(new Node(i + row[k], j + col[k], dist + 1, node));
                }
            }

            if(tempNodes.size() != 0) {

                for (int k = 0; k < tempNodes.size(); k++) {
                    //prawdopodobienstwo.add(T(tempNodes, tempNodes.get(k)));
                    for (double a = 0.000; a < T(tempNodes, tempNodes.get(k)); a+=0.001) {
                        randomNodes.add(tempNodes.get(k));
                    }
                }

                boolean flaga = false;

                Random generator = new Random();
                Node wylosowany = randomNodes.get(generator.nextInt(randomNodes.size()));
                int tnSize = tempNodes.size();
                for(int k = 0; k < tnSize; k++) {

                if(uzyty.size() != 0)
                    while(!sprawdzCzyBylUzyty(uzyty, wylosowany)){
                        wylosowany = randomNodes.get(generator.nextInt(randomNodes.size()));
                    }

                    uzyty.add(wylosowany);

                    ostatecznaLista.add(wylosowany);

                }

                for(int o = ostatecznaLista.size()-1; o >= 0; o-- ) {
                    //System.out.println(ostatecznaLista.get(o));
                    q.add(ostatecznaLista.get(o));
                }
                tempNodes.clear();
                randomNodes.clear();
                ostatecznaLista.clear();
                uzyty.clear();
            }
            tempNodes.clear();
            randomNodes.clear();
            ostatecznaLista.clear();

        }

        //System.out.println("przed petla: "+ pokaztrase);
        //if(pokaztrase == 0) {
            //System.out.println(pokaztrase);
            if (min_dist != Integer.MAX_VALUE) {
//                System.out.println("ścieżka "
//                        + "ma dystans: " + (min_dist - 1) + "\nLiczba wszystkich kroków: " + (kroki - 1));

                ktoraTrasa = numerNajkrotszej;
                //printPath(trasa[ktoraTrasa-1], mat); // wybierz trase do narysowania
                printPath(mat);
            } else {
                System.out.println("Destination can't be reached from source");
            }

        //}
    }

    private void printPath( int[][] mat) {

        int k = 0;
        Node node = getOstatniNode();
        while (node != null) {
            mat[node.x][node.y] = 2;
            wspolTrasa[k][0] = node.x;
            wspolTrasa[k][1] = node.y;
            node = node.parent;
            k++;
        }

//        for (int i = 0; i < M; i++) {
//            System.out.println(Arrays.toString(mat[i]));
//        }
        System.out.println();
        System.out.println();
        System.out.println("Trasa feromonu:");
        matTrasaKopia = mat;
        pokazFeromony();

        //System.out.println("Aktualnie wyświetlasz ścieżkę nr: " + ktoraTrasa);
    }

    private void pokazFeromony(){
        System.out.println();
        for (int i = 0; i < M; i++) {
            System.out.print("[\t\t");
            for(int j = 0; j < N; j++){
                if(matTrasaKopia[i][j] == 2){
                    System.out.print(ANSI_GREEN);
                    System.out.printf("%.3f",feromony[i][j]);
                    System.out.print(ANSI_RESET);
                }
                else if(matKopia[i][j] == 1){
                    System.out.print("███");
                }
//                else if(String.format("%.2f", feromony[i][j]).equals("0,00")){
//                    System.out.print("⌈  ⌋");
//                }
                else{
                    System.out.printf("%.3f",feromony[i][j]);
                }
                System.out.print("\t\t");
            }
            System.out.print("]");
            System.out.println();
            System.out.println();
            System.out.println();
        }
    }

    private double T (List<Node> nodes, Node sprawdzany)
    {
        double suma = 0;
        for(int i = 0; i < nodes.size(); i++){
            suma += feromony[nodes.get(i).x][nodes.get(i).y];
        }
        double p = (feromony[sprawdzany.x][sprawdzany.y]/suma);
        //System.out.println("int P = "+(int)(p)+",  double P = " + p);
        return p;
    }

    private boolean sprawdzCzyBylUzyty(List<Node> uzyte, Node wylosowana)
    {
        boolean flaga = true;
        for(int i = 0; i < uzyte.size(); i++){
            if(uzyte.get(i).x == wylosowana.x && uzyte.get(i).y == wylosowana.y)
                flaga = false;
        }
        return flaga;
    }

    public void odparowanie()
    {
        for(int i = 0; i < feromony.length; i++)
            for(int j = 0; j < feromony.length; j++)
                feromony[i][j] = feromony[i][j]*wspolczynnikParowania;
    }

    public int[][] getWspolTrasy(){
        return wspolTrasa;
    }
//    public int[][] getOdwiedzone(){
//        return odwiedzone;
//    }
    public int getKroki() {
        return kroki;
    }
    public int getMin_dist() {
        return (min_dist-1);
    }
    public Node getOstatniNode(){
        return listaNode.get(0);
    }
}
