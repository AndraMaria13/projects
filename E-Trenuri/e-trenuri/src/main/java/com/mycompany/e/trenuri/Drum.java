package com.mycompany.e.trenuri;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Drum {

    private int Id;
    private Statie plecare, sosire;
    private float distanta;

    public Drum(int Id, Statie plecare, Statie sosire, float distanta) {
        this.Id = Id;
        this.plecare = plecare;
        this.sosire = sosire;
        this.distanta = distanta;
    }

    @Override
    public String toString() {
        return "Drum: [" + (plecare != null ? plecare.getNume() : "NONE") + " -> " + (sosire != null ? sosire.getNume() : "NONE") + "] " + distanta + " km";
    }

    public String toShortString() {
        return (plecare != null ? plecare.getNume() : "NONE") + " -> " + (sosire != null ? sosire.getNume() : "NONE");
    }

    public String toPlecareShortString() {
        return (plecare != null ? plecare.getNume() : "NONE");
    }

    public String toSosireShortString() {
        return (sosire != null ? sosire.getNume() : "NONE");
    }

    public boolean itsId(int id) {
        return this.Id == id;
    }

    public float getDistanta() {
        return distanta;
    }

    public Statie getPlecare() {
        return plecare;
    }

    public Statie getSosire() {
        return sosire;
    }

    public Ora getOraPlecarePeTraseu(Traseu traseu) {
        Ora plecareInitiala = new Ora(traseu.getOraPlecare().getOra(), traseu.getOraPlecare().getMinut());
        float nrKm = 0.0f;
        int minutesTraveled = 0;

        for (Drum drum : traseu.getDrumuri()) {
            if (drum != this) {
                nrKm += drum.getDistanta();
                minutesTraveled += 10; //adaug 10 minute pentru ca atat sta trenul in statie
            } else {
                break;
            }
        }

        int speed = 0;
        speed = switch (traseu.getTren().getTip()) {
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

        minutesTraveled += nrKm / speed * 60;

        plecareInitiala.adaugaMinute(minutesTraveled);    // adds one hour
        return plecareInitiala;
    }

    public Ora getOraSosirePeTraseu(Traseu traseu) {
        Ora plecareInitiala = getOraPlecarePeTraseu(traseu);//adaug 10 minute pentru ca atat sta trenul in statie

        int speed = 0;
        speed = switch (traseu.getTren().getTip()) {
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
        var mins = distanta / speed * 60 + 10;
        plecareInitiala.adaugaMinute((int) mins);    // adds one hour
        return plecareInitiala;
    }

    public static ArrayList<Drum> initDrumuri(ArrayList<Statie> statii) {
        ArrayList<Drum> drumuri = new ArrayList<>();
        BufferedReader br;
        try {
            File file = new File("e-trenuri\\src\\main\\java\\com\\mycompany\\e\\trenuri\\drumuri.txt");
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Fisierul de drumuri nu a fost gasit", "Error", JOptionPane.ERROR_MESSAGE);
            return drumuri;
        }

        try {
            String st;

            while ((st = br.readLine()) != null) {
                String[] data = st.split(",");

                Statie plecare = null;
                int plecareId = Integer.parseInt(data[1]);
                Statie sosire = null;
                int sosireId = Integer.parseInt(data[2]);
                for (Statie statie : statii) {
                    if (plecare == null && statie.isId(plecareId)) {
                        plecare = statie;
                    }
                    if (sosire == null && statie.isId(sosireId)) {
                        sosire = statie;
                    }
                    if (plecare != null && sosire != null) {
                        break;
                    }
                }
                drumuri.add(new Drum(Integer.parseInt(data[0]), plecare, sosire, Float.parseFloat(data[3])));
            }

        } catch (IOException | NumberFormatException e) {
            drumuri.removeAll(drumuri);
            JOptionPane.showMessageDialog(null, "A aparut o eroare la citirea Statiilor din fisier", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return drumuri;

    }

}
