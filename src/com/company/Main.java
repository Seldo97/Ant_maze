package com.company;

// ©Marcin Olek
// algorytm mrówkowy gęstościowy
// 01-2019

public class Main
{

    //##########

    static int lab = 2; // !!! wybór labiryntu do wyświetlenia od 1-9 !!!
    static int kolonia = 50; // liczba kolonii (ilosc iteracji)
    static int liczbaAgentow = 4; // liczba mrówek w kolonii

    //##########

    public static void main(String[] args){

        wczytajLabirynty();

    }

    public static void wczytajLabirynty(){

        Labirynt []labirynt = new Labirynt[9];

       for(int i = 0; i < 9; i++)
           labirynt[i] = new Labirynt("labirynt"+(i+1)+".txt");


        System.out.println("====================================");
        labirynt[lab-1].wyswietlLabirynt();
        labirynt[lab-1].wyswietlWspolPrzeszkod();
        System.out.println("====================================");

        Labirynt labKopia3 = new Labirynt("labirynt"+(lab)+".txt");

        // #########################################################
        // ################# PRZESZUKIWANIE MRÓWKOWE ###############
        // #########################################################
        //int test = 0;
        System.out.println("### PRZESZUKIWANIE MRÓWKOWE ###");
        AntMaze ant = null;
        for(int z = 0; z < kolonia; z++) {
            System.out.println("################## KOLONIA: " + z + "########################");
            for (int m = 0; m < liczbaAgentow; m++) {
                System.out.println("MRÓWKA nr: "+ m + ":");
                ant = new AntMaze(labirynt[lab - 1].getM(), labirynt[lab - 1].getN());
                ant.startAlgorithm(labKopia3.getLabirynt(), 0, 0, labirynt[lab - 1].getM() - 1, labirynt[lab - 1].getN() - 1);
                labKopia3 = new Labirynt("labirynt" + (lab) + ".txt");
                //System.out.println("petla: " + m);
                ant.pokaztrase++;
            }
            System.out.println("#######################################################");
            ant.odparowanie();
            ant.pokaztrase = 0;
            //test = 0;
        }
        System.out.println("Liczba kroków: " + ant.getKroki());
        System.out.println("Dystans ścieżki: " + ant.getMin_dist());
        System.out.println();

    }


}
