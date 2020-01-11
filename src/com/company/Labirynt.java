package com.company;
import java.io.BufferedReader;
import java.io.FileReader;

public class Labirynt {
    private int m; // liczba wierszy
    private int n; // liczba kolumn
    private int ilPrz; // ilość przeszkód
    private String nazwa; // nazwa pliku
    private int wspolPrz[][]; // tablica współrzędnych przeszkód
    private int labirynt[][]; // macierz labiryntu


    public Labirynt(String plik){

        nazwa = plik;

        try{ //wczytywanie danych labiryntu z pliku

            FileReader fr = new FileReader(plik);
            BufferedReader br = new BufferedReader(fr);
            String linia;

            linia=br.readLine();
            String s[] = linia.split(",");

            //liczba wierszy
            m = Integer.parseInt(s[0]);
            //liczba kolumn
            n = Integer.parseInt(s[1]);
            //liczba przeszkód
            ilPrz = Integer.parseInt(s[2]);
            wspolPrz = new int[ilPrz][2];

            //pobranie współrzędnych przeszkód
            for(int i = 0; i < ilPrz; i++){
                linia=br.readLine();
                s = linia.split(",");

                wspolPrz[i][0] = Integer.parseInt(s[0]);
                wspolPrz[i][1] = Integer.parseInt(s[1]);

            }

        }catch (java.io.IOException e)
        {
            System.out.println("Błąd odczytu z pliku!");
            e.printStackTrace();
        }

        stworzLabirynt();
    }

    public void stworzLabirynt(){ // wersja ze start/stop

        labirynt = new int[m][n]; //stwórz tablice

        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                for(int p = 0; p < ilPrz; p++){

                    if(i == wspolPrz[p][0] && j == wspolPrz[p][1]){ //sprawdź czy na aktualnych współrzędnych jest przeszkoda
                        labirynt[i][j] = 1;
                        break; // jeżeli jest, wpisz 1 i wyjdź z obecnej pętli
                    }
                    else{
                            labirynt[i][j] = 0;
                    }

                }
            }
        }

    }

    public int getM(){
        return m; //zwróć liczbe wierszy
    }
    public int getN(){
        return n; //zwróć liczbe kolumn
    }
    public int getIlPrz(){
        return ilPrz; //zwróć ilość przeszkód
    }
    public int[][] getLabirynt(){
        return labirynt; //zwróć tablice z labiryntem
    }
    public int[][] getWspolPrz(){
        return wspolPrz; //zwróć tablice z współrzędnymi przeszkód
    }

    public void wyswietlLabirynt(){

        System.out.println("Labirynt: " + nazwa);

        for(int i = 0; i < m; i++) {
            System.out.print("{");
            for (int j = 0; j < n; j++) {
                System.out.print(" "+labirynt[i][j]+" ");
            }
            System.out.print("}");
            System.out.println();
        }
    }

    public void wyswietlWspolPrzeszkod(){

        System.out.println("Współrzędne przeszkód " + nazwa);
        System.out.println(" x  y ");

        for(int i = 0; i < ilPrz; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(" "+wspolPrz[i][j]+" ");
            }
            System.out.println();
        }

    }
}
