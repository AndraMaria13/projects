
package com.mycompany.e.trenuri;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Statie {
    
    public Statie(int id, String name){
        this.id = id;
        this.nume = name;
    }
    private int id;
    private String nume;
    
    public String getNume() {
        return nume;
    }
    public boolean isId(int id){
        return this.id == id;
    }

    @Override
    public String toString() {
        return nume; 
    }
    
    public static ArrayList<Statie> initStatii() {
        ArrayList<Statie> statii = new ArrayList<>();
        BufferedReader br;
        try {
            File file = new File("e-trenuri\\src\\main\\java\\com\\mycompany\\e\\trenuri\\statii.txt");
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Fisierul de staii nu a fost gasit", "Error", JOptionPane.ERROR_MESSAGE);
            return statii;
        }

        try {
            String st;

            while ((st = br.readLine()) != null) {
                String[] data = st.split(",");
                statii.add(new Statie(Integer.parseInt(data[0]), data[1]));
            }

        } catch (IOException | NumberFormatException e) {
            statii.removeAll(statii);
            JOptionPane.showMessageDialog(null, "A aparut o eroare la citirea Statiilor din fisier", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return statii;

    }
    
    
}


