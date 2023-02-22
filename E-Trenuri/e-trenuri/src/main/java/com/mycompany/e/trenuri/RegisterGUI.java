package com.mycompany.e.trenuri;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

public class RegisterGUI extends JFrame {

    private JTextField usernameField;
    private JTextField cnpField;
    private JComboBox tipComboBox;
    private JTextField numeField;
    private JTextField prenumeField;
    private JSpinner varstaSpinner;
    private JPasswordField passwordField;
    private JLabel cnpLabel;
    private JLabel tipLabel;
    private JLabel numeLabel;
    private JLabel prenumeLabel;
    private JLabel varstaLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton registerButton;
    private JLabel errorLabel;
    private LoginGUI loginGUI;

    public RegisterGUI(LoginGUI frame) throws HeadlessException {
        this.loginGUI = frame;
        init();
    }

    public RegisterGUI(LoginGUI frame, Image icon) throws HeadlessException {
        this.loginGUI = frame;
        this.setIconImage(icon);
        init();
    }

    private void init() {
        this.setTitle("Register");

        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.red);
      //  errorLabel.setHorizontalAlignment(JLabel.CENTER);


        passwordField = new JPasswordField();


        numeLabel = new JLabel("\nFirst Name:");
        //numeLabel.setHorizontalAlignment(JLabel.CENTER);


        prenumeLabel = new JLabel("Last Name:");


        tipLabel = new JLabel("Category:");


        cnpLabel = new JLabel("CNP:");


        varstaLabel = new JLabel("Age:");


        usernameLabel = new JLabel("Username:");


        passwordLabel = new JLabel("Password:");


        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(0,153,0));
        registerButton.setForeground(Color.WHITE);


        usernameField = new JTextField();


        numeField = new JTextField();


        prenumeField = new JTextField();


        cnpField = new JTextField();

        String [] tipuri = new String[4];
        
        tipuri[0]="Adult";
        tipuri[1]="Soldat";
        tipuri[2]="Student";
        tipuri[3]="Pensionar";
        
        tipComboBox = new JComboBox(tipuri);

        SpinnerModel model = new SpinnerNumberModel(1, 1, 100, 1);
        varstaSpinner = new JSpinner(model);


        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                register();
            }
        });

        numeLabel.setHorizontalAlignment(JLabel.CENTER);
        prenumeLabel.setHorizontalAlignment(JLabel.CENTER);
        varstaLabel.setHorizontalAlignment(JLabel.CENTER);
        tipLabel.setHorizontalAlignment(JLabel.CENTER);
        cnpLabel.setHorizontalAlignment(JLabel.CENTER);
        usernameLabel.setHorizontalAlignment(JLabel.CENTER);
        passwordLabel.setHorizontalAlignment(JLabel.CENTER);
        errorLabel.setHorizontalAlignment(JLabel.CENTER);

        this.add(passwordLabel);
        this.add(passwordField);
        this.add(usernameLabel);
        this.add(usernameField);
        this.add(prenumeLabel);
        this.add(prenumeField);
        this.add(numeLabel);
        this.add(numeField);
        this.add(tipLabel);
        this.add(tipComboBox);
        this.add(cnpLabel);
        this.add(cnpField);
        this.add(varstaLabel);
        this.add(varstaSpinner);
        this.add(errorLabel);
        this.add(registerButton);

        this.setLayout(new GridLayout(0, 2, 35, 35));

        this.setSize(800, 800);
        this.setVisible(true);
    }

    private String validateRegistration() {

        String err = "";
        if (prenumeField.getText().isEmpty() || prenumeField.getText().isBlank()) {
            err += "lipseste prenumele\n";
        }
        if (numeField.getText().isEmpty() || numeField.getText().isBlank()) {
            err += "lipseste numele\n";
        }
        if (usernameField.getText().isEmpty() || usernameField.getText().isBlank()) {
            err += "lipseste numele de utilizatior\n";
        }
        if (passwordField.getText().isEmpty() || passwordField.getText().isBlank()) {
            err += "lipseste parola\n";
        }
        if (cnpField.getText().isEmpty() || cnpField.getText().isBlank()) {
            err += "lipseste cnp-ul\n";
        }
        if ((int) varstaSpinner.getValue() <= 5) {
            err += "lipsește vârsta sau ești prea tânăr\n";
        }
        if (cnpField.getText().length() != 13) {
            err += "Cnp Invalid\n";
        }
        return err;
    }

    private void register() {
        String err = validateRegistration();
        if (!err.isEmpty()) {
            JOptionPane.showMessageDialog(this, err, "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String line = "\n" + usernameField.getText() + ","
                + String.valueOf(passwordField.getPassword()) + ","
                + cnpField.getText() + ","
                + tipComboBox.getSelectedItem()+ ","
                + numeField.getText() + ","
                + prenumeField.getText() + ","
                + varstaSpinner.getValue();
        System.out.println(line);
        try {
            File file = new File("e-trenuri\\src\\main\\java\\com\\mycompany\\e\\trenuri\\users.txt");
            FileWriter fr = new FileWriter(file, true);
            fr.write(line);
            fr.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Am Întampinat o problemă la înregistrare", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(this, "Te-ai înregistrat cu succes!");
        this.dispose();
    }

}
