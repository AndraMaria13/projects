package com.mycompany.e.trenuri;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class MainFrame extends JFrame {

    private User me;
    private ArrayList<Statie> statii;
    private ArrayList<Drum> drumuri;
    private ArrayList<Tren> trenuri;
    private ArrayList<Traseu> trasee;
    private ArrayList<Traseu> traseeDirecteGasite;
    private JPanel buyPanel;
    private JComboBox garaTranzitComboBox;
    private JComboBox garaPlecareComboBox;
    private JComboBox garaSosireComboBox;
    private JComboBox tipTrenComboBox;
    private JComboBox tipClasaComboBox;
    private JComboBox oraPlecareComboBox;
    private JComboBox oraSosireComboBox;
    private JButton cautareBtn;
    private JSpinner maxCostSpinner;
    private JComboBox tipPersoanaComboBox;
    private JTextField cnpField;
    private JLabel garaPlecareLabel;
    private JLabel garaTranzitLabel;
    private JLabel garaSosireLabel;
    private JLabel tipClasaLabel;
    private JLabel costMaxLabel;
    private JLabel oraPlecareLabel;
    private JLabel oraSosireLabel;
    private JLabel tipTrenLabel;
    private JLabel tipBiletLabel;
    private JLabel cnpLabel;
    private LoginGUI loginGUI;
    private ImageIcon trenImage;
    private JLabel trenLabel;


    public MainFrame(User user, LoginGUI login) {
        this.loginGUI = login;
        me = user;
        init();
    }

    public MainFrame(User user, Image icon, LoginGUI login) {
        this.loginGUI = login;
        me = user;
        this.setIconImage(icon);
        init();
    }

    private void init() {
        this.setTitle("Trenuri");
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        this.setSize(700, 700);


        statii = Statie.initStatii();
        drumuri = Drum.initDrumuri(statii);
        trenuri = Tren.initTrenuri();
        trasee = Traseu.initTrasee(drumuri, trenuri);

        ArrayList<Statie> statiiCuUnaGoala = new ArrayList<>(statii);
        statiiCuUnaGoala.add(0, new Statie(0, "-"));

        Ora[] ore = new Ora[60 * 24];  //1440
        for (int k = 0; k < 24; k++) {
            for (int j = 0; j < 60; j++) {
                ore[k * 60 + j] = new Ora(k, j);
            }

        }
        String[] tipuriDeTren = {"Personal", "Accelerat", "Rapid", "Intercity"};
        String[] tipPersoana = {"Adult", "Soldat", "Student", "Pensionar"};

        cnpField = new JTextField();
        cnpField.setBounds(560, 100, 100, 30);
        cnpLabel = new JLabel("CNP: ");
        cnpLabel.setBounds(475,100,100,30);
     //   this.add(cnpLabel);

        garaTranzitComboBox = new JComboBox(statiiCuUnaGoala.toArray());
        garaTranzitComboBox.setBounds(100, 50, 100, 30);
        garaTranzitLabel = new JLabel("Gara tranzit: ");
        garaTranzitLabel.setBounds(0,50,100,30);
        this.add(garaTranzitLabel);

        garaPlecareComboBox = new JComboBox(statii.toArray());
        garaPlecareComboBox.setBounds(100, 0, 100, 30);
        garaPlecareLabel = new JLabel("Gara plecare: ");
        garaPlecareLabel.setBounds(0,0,100,30);

        garaSosireComboBox = new JComboBox(statii.toArray());
        garaSosireComboBox.setBounds(100, 100, 100, 30);
        garaSosireLabel = new JLabel("Gara sosire: ");
        garaSosireLabel.setBounds(0,100,100,30);
        this.add(garaSosireLabel);

        cautareBtn = new JButton("Cauta");
        cautareBtn.setBounds(315, 200, 100, 30);

        tipPersoanaComboBox = new JComboBox(tipPersoana);
        tipPersoanaComboBox.setBounds(560, 50, 100, 30);
        tipBiletLabel = new JLabel("Tip persoana: ");
        tipBiletLabel.setBounds(475,50,100,30);


        buyPanel = new JPanel();
        buyPanel.setLayout(null);
        buyPanel.setBackground(Color.WHITE);
        buyPanel.setBounds(0, 250, 1000, 750);



        cautareBtn.addActionListener((ActionEvent e) -> {
            traseeDirecteGasite = cauta();
            buyPanel.removeAll();
            buyPanel.add(trenLabel);

            if (traseeDirecteGasite.isEmpty()) {
                buyPanel.setVisible(false);
                return;
            }
            for (Traseu traseu : traseeDirecteGasite) {
                System.out.println(traseu);
                JLabel label = new JLabel(traseu.toBuyString(tipClasaComboBox.getSelectedIndex(),
                        me == null ? tipPersoanaComboBox.getSelectedItem().toString() : me.getTip(),
                        garaTranzitComboBox.getSelectedItem().toString()));
                label.setBounds(0, 50 * traseeDirecteGasite.indexOf(traseu), 800, 50);

                JButton button = new JButton("Buy");
                button.addActionListener((ActionEvent ev) -> {

                    int idTren = traseu.getTren().getId();
                    String tipTren = traseu.getTren().getTip();
                    int nrLocuri = tipClasaComboBox.getSelectedIndex() == 0 ? traseu.getTren().getNrLocuriClasaI() : traseu.getTren().getNrLocuriClasaII();
                    Random rand = new Random();

                    int nrLoc = rand.nextInt(nrLocuri);

                    String tip = me == null ? tipPersoanaComboBox.getSelectedItem().toString() : me.getTip();
                    String cnp = (!"Adult".equals(tip) ? (me == null ? cnpField.getText() : me.getCNP()) : "");

                    String numeFisier = cnp.isEmpty() ? "Anonym" : cnp + "" + nrLoc;

                    try {
                        File file = new File("e-trenuri\\src\\main\\java\\com\\mycompany\\e\\trenuri\\" + numeFisier + ".txt");
                        FileWriter fr = new FileWriter(file, true);
                        fr.write("Tren: #" + idTren + "\nTip Tren: " + tipTren + "\nNumarul Locului: " + nrLoc + "\n" + (cnp.isEmpty() ? "" : ("CNP: " + cnp)));
                        fr.close();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Am Întampinat o problemă la cumpărarea biletului", "Eroare", JOptionPane.ERROR_MESSAGE);
                    }
                    JOptionPane.showMessageDialog(this, "Biletul a fost emis cu succes!");

                });
                button.setBounds(600, 50 * traseeDirecteGasite.indexOf(traseu), 70, 50);


                buyPanel.add(label);
                buyPanel.add(button);
            }
            buyPanel.repaint();
            buyPanel.setVisible(true);

        });

        trenImage = new ImageIcon("C:\\Users\\maria\\OneDrive\\Desktop\\tren.png");
        trenLabel = new JLabel();
        trenLabel.setIcon(trenImage);
        trenLabel.setBounds(475,210,300,200);
        buyPanel.add(trenLabel);


        maxCostSpinner = new JSpinner();
        maxCostSpinner.setBounds(325, 0, 100, 30);
        costMaxLabel = new JLabel("Cost bilet: ");
        costMaxLabel.setBounds(250,0,100,30);
        this.add(costMaxLabel);




        this.add(garaPlecareLabel);
        this.add(garaTranzitComboBox);
        this.add(garaPlecareComboBox);
        this.add(garaSosireComboBox);
        this.add(cautareBtn);
        this.add(maxCostSpinner);

        if (me == null) {
            this.add(tipPersoanaComboBox);
            this.add(cnpField);
            this.add(tipBiletLabel);
            this.add(cnpLabel);
        }

        this.add(buyPanel);
        this.setVisible(true);

        oraPlecareComboBox = new JComboBox(ore);
        oraPlecareComboBox.setBounds(325, 50, 100, 30);
        this.add(oraPlecareComboBox);
        oraPlecareLabel = new JLabel("Ora plecare: ");
        oraPlecareLabel.setBounds(250,50,100,30);
        this.add(oraPlecareLabel);

        oraSosireComboBox = new JComboBox(ore);
        oraSosireComboBox.setBounds(325, 100, 100, 30);
        oraSosireComboBox.setSelectedIndex(60 * 24 - 1);
        this.add(oraSosireComboBox);
        oraSosireLabel = new JLabel("Ora sosire: ");
        oraSosireLabel.setBounds(250,100,100,30);
        this.add(oraSosireLabel);

        tipTrenComboBox = new JComboBox(tipuriDeTren);
        tipTrenComboBox.setBounds(560, 0, 100, 30);
        tipTrenComboBox.setVisible(true);
        this.add(tipTrenComboBox);
        tipTrenLabel = new JLabel("Tip tren: ");
        tipTrenLabel.setBounds(475,0,100,30);
        this.add(tipTrenLabel);

        String[] tipuriDeClase = {"Clasa 1", "Clasa 2"};
        tipClasaComboBox = new JComboBox(tipuriDeClase);
        tipClasaComboBox.setBounds(100, 150, 100, 30);
        tipClasaComboBox.setVisible(true);
        tipClasaLabel = new JLabel("Clasa: ");
        tipClasaLabel.setBounds(0,150,100,30);
        this.add(tipClasaComboBox);
        this.add(tipClasaLabel);




        MainFrame main = this;

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               loginGUI.setVisible(true);
               main.dispose();
            }
        });
        

    }

    ArrayList<Traseu> cauta() {
        Statie plecare = (Statie) garaPlecareComboBox.getSelectedItem();
        Statie tranzit = (Statie) garaTranzitComboBox.getSelectedItem();
        Statie sosire = (Statie) garaSosireComboBox.getSelectedItem();
        String tipTren = (String) tipTrenComboBox.getSelectedItem();

        ArrayList<Traseu> traseeDirecte = new ArrayList<Traseu>();

        for (Traseu traseu : trasee) {

            if (!tipTren.equals(traseu.getTren().getTip())) {
                continue;
            }
            boolean foundPlecare = false;
            boolean foundSosire = false;
            boolean foundTranzit = tranzit.isId(0);
            Drum drumPlecare = null, drumTranzit = null, drumSosire = null;

            int i = 0, j = 0, k = 0;

            var drumuri = traseu.getDrumuri();

            ArrayList<Traseu> posibileTraseeDirecte = new ArrayList<>();

            while (i < drumuri.size() - 1 && j < drumuri.size() - 1 && k < drumuri.size() - 1) {

                if (!foundPlecare) {
                    if (drumuri.get(i).getPlecare() == plecare) {
                        drumPlecare = drumuri.get(i);
                        foundPlecare = true;
                    } else {
                        i++;
                        j++;
                        k++;
                    }
                } else {
                    if (!foundTranzit) {
                        if (drumuri.get(j).getPlecare() == tranzit || drumuri.get(j).getSosire() == tranzit) {
                            drumTranzit = drumuri.get(j);
                            foundTranzit = true;
                        } else {
                            j++;
                            k++;
                        }
                    } else {
                        if (!foundSosire) {
                            if (drumuri.get(k).getSosire() == sosire) {
                                drumSosire = drumuri.get(k);
                                foundSosire = true;
                            } else {
                                k++;
                            }
                        }
                    }

                }

                if (foundPlecare && foundTranzit && foundSosire) {
                    Ora oraPlecare = drumPlecare.getOraPlecarePeTraseu(traseu);
                    ArrayList<Drum> drumuriMiniTraseu = new ArrayList<>();
                    boolean add = false;
                    for (Drum drum : traseu.getDrumuri()) {
                        if (drum == drumPlecare) {
                            add = true;
                        }
                        if (add) {
                            drumuriMiniTraseu.add(drum);
                        }
                        if (drum == drumSosire) {
                            break;
                        }
                    }

                    posibileTraseeDirecte.add(new Traseu(drumuriMiniTraseu, oraPlecare, traseu.getTren()));
                    i++;
                    j = i;
                    k = i;
                    foundPlecare = false;
                    foundTranzit = tranzit.isId(0);
                    foundSosire = false;
                    drumPlecare = null;
                    drumTranzit = null;
                    drumSosire = null;
                }

            }
            for (Traseu t : posibileTraseeDirecte) {
                Ora oraPlecare = t.getOraPlecare();
                Ora oraSosire = t.getDrumuri().get(t.getDrumuri().size() - 1).getOraSosirePeTraseu(t);
                if (oraPlecare.isLessThan((Ora) oraPlecareComboBox.getSelectedItem()) || oraSosire.isGreaterThan((Ora) oraSosireComboBox.getSelectedItem())) {

                    continue;
                }
                if ((int) maxCostSpinner.getValue() > 0) {
                    int multiplier = 0;
                    if (tipClasaComboBox.getSelectedIndex() == 0) {
                        multiplier = 25;
                    }
                    if (tipClasaComboBox.getSelectedIndex() == 1) {
                        multiplier = 15;
                    }

                    switch (me == null ? tipPersoanaComboBox.getSelectedItem().toString() : me.getTip()) {
                        case "Soldat" ->
                            multiplier *= 0.5;
                        case "Student" ->
                            multiplier *= 0.8;
                        case "Pensionar" ->
                            multiplier *= 0.7;
                    }
                    if (t.getTotalTime() * multiplier > (int) maxCostSpinner.getValue()) {
      
                        continue;
                    }
                }
                traseeDirecte.add(t);
            }
            posibileTraseeDirecte.removeAll(posibileTraseeDirecte);
        }
        return traseeDirecte;
    }

    public void printData() {

        for (Statie statie : statii) {
            System.err.println(statie);
        }

        for (Drum drum : drumuri) {
            System.err.println(drum);
        }

        for (Tren tren : trenuri) {
            System.err.println(tren);
        }
        for (Traseu traseu : trasee) {
            System.err.println(traseu);
        }
    }
}
