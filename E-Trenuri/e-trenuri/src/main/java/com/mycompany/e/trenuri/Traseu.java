package com.mycompany.e.trenuri;

import javax.swing.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Traseu {

    private ArrayList<Drum> drumuri = new ArrayList<>();
    private Ora oraPlecare;
    private Tren tren;

    public Traseu(ArrayList<Drum> drumuri, Ora oraPlecare, Tren tren) {
        this.drumuri = drumuri;
        this.oraPlecare = oraPlecare;
        this.tren = tren;
    }

    public Ora getOraPlecare() {
        return oraPlecare;
    }

    public Drum getDrum(int index) {
        return drumuri.get(index);
    }

    public ArrayList<Drum> getDrumuri() {
        return drumuri;
    }

    public Tren getTren() {
        return tren;
    }

    @Override
    public String toString() {
        String drum = "Traseu: ";
        for (Drum d : drumuri) {
            drum += (drumuri.indexOf(d) + 1);
            drum += ". ";
            drum += d.toPlecareShortString();
            drum += " -> ";
            if (drumuri.indexOf(d) == drumuri.size() - 1) {
                drum += d.toSosireShortString();
            }
        }
        return drum;
    }

    public Ora getOraSosire() {
        return drumuri.get(drumuri.size() - 1).getOraSosirePeTraseu(this);
    }

    public String toBuyString(int clasa, String tipPersoana, String tranzit) {

        int multiplier = 0;
        if (clasa == 0) {
            multiplier = 25;
        }
        if (clasa == 1) {
            multiplier = 15;
        }

        switch (tipPersoana) {
            case "Soldat" ->
                multiplier *= 0.5;
            case "Student" ->
                multiplier *= 0.8;
            case "Pensionar" ->
                multiplier *= 0.7;
        }
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        return "Tren #"
                + tren.getId()
                + " | "
                + drumuri.get(0).getPlecare().getNume()
                + ((tranzit.equals("-")) ? "" : " => ")
                + ((tranzit.equals("-")) ? "" : tranzit)
                + " -> "
                + drumuri.get(drumuri.size() - 1).getSosire().getNume()
                + "| "
                + oraPlecare
                + " => "
                + getOraSosire()
                + "| "
                + getTotalLength()
                + " km | "
                + getTotalTimeAsString()
                + " | "
                + numberFormat.format(getTotalTime() * multiplier)
                + " RON";
    }

    public float getTotalLength() {
        float total = 0.0f;
        for (Drum drum : drumuri) {
            total += drum.getDistanta();
        }
        return total;
    }

    public float getTotalTime() {
        int minutesPaused = drumuri.size() * 10;
        int speed;
        speed = switch (tren.getTip()) {
            case "Rapid" ->
                500;
            case "Accelerat" ->
                250;
            case "Personal" ->
                100;
            case "Intercity" ->
                50;
            default ->
                0;
        };
        return (getTotalLength() + minutesPaused / 60) / speed;
    }

    public String getTotalTimeAsString() {
        Ora s = getOraSosire();

        Ora a = new Ora(s.getOra(), s.getMinut());
        a.Minus(oraPlecare);

        return a.getOra() + "h " + a.getMinut() + "m";
    }

    public static ArrayList<Traseu> initTrasee(ArrayList<Drum> drumuri, ArrayList<Tren> trenuri) {
        ArrayList<Traseu> trasee = new ArrayList<>();

        BufferedReader br;
        try {
            File file = new File(
                    "e-trenuri\\src\\main\\java\\com\\mycompany\\e\\trenuri\\trasee.txt");
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Fisierul de trasee nu a fost gasit", "Error", JOptionPane.ERROR_MESSAGE);
            return trasee;
        }

        try {
            String st;

            while ((st = br.readLine()) != null) {
                String[] data = st.split(",");
                Tren tren = null;
                ArrayList<Drum> d = new ArrayList<>();

                int id = Integer.parseInt(data[1]);
                for (Tren t : trenuri) {
                    if (t.itsId(id)) {
                        tren = t;
                    }
                }

                if (tren == null) {
                    JOptionPane.showMessageDialog(null, "Nu am gasit trenul pentru traseul " + id);
                    continue;
                }

                var orasplit = data[2].split("/");

                for (int i = 3; i < data.length; i++) {
                    int idDrum = Integer.parseInt(data[i]);
                    for (Drum drum : drumuri) {
                        if (drum.itsId(idDrum)) {
                            d.add(drum);
                            break;
                        }
                    }
                }

                trasee.add(new Traseu(d, new Ora(Integer.parseInt(orasplit[3]), Integer.parseInt(orasplit[4])), tren));
            }
            return trasee;

        } catch (IOException | NumberFormatException e) {
            drumuri.removeAll(drumuri);
            JOptionPane.showMessageDialog(null, "a aparut o eroare la citirea Statiilor din fisier", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return trasee;
    }
}
