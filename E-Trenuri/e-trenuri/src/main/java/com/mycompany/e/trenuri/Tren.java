package com.mycompany.e.trenuri;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Tren {

    private int id;
    private String tip;
    private int nrLocuriClasaI;
    private int nrLocuriClasaII;

    public Tren(int id, String tip, int nrLocuriClasaI, int nrLocuriClasaII) {
        this.id = id;
        this.tip = tip;
        this.nrLocuriClasaI = nrLocuriClasaI;
        this.nrLocuriClasaII = nrLocuriClasaII;
    }

    public boolean itsId(int id) {
        return this.id == id;
    }
    
    public String getTip() {
        return tip;
    }
    public int getId() {
        return id;
    }
    public int getNrLocuriClasaI() {
        return nrLocuriClasaI;
    }
    public int getNrLocuriClasaII() {
        return nrLocuriClasaII;
    }

    @Override
    public String toString() {
        return "Tren: " + id + "(" + tip + ")" + " Locuri:[I:" + nrLocuriClasaI + " ,II:" + nrLocuriClasaII + "]";
    }

    public static ArrayList<Tren> initTrenuri() {
        ArrayList<Tren> trenuri = new ArrayList<>();
        BufferedReader br;
        try {
            File file = new File(
                    "e-trenuri\\src\\main\\java\\com\\mycompany\\e\\trenuri\\trenuri.txt");
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Fisierul de trenuri nu a fost gasit", "Error", JOptionPane.ERROR_MESSAGE);
            return trenuri;
        }

        try {
            String st;

            while ((st = br.readLine()) != null) {
                String[] data = st.split(",");
                trenuri.add(new Tren(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3])));
            }

        } catch (IOException | NumberFormatException e) {
            trenuri.removeAll(trenuri);
            JOptionPane.showMessageDialog(null, "A aparut o eroare la citirea Statiilor din fisier", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return trenuri;

    }

}
