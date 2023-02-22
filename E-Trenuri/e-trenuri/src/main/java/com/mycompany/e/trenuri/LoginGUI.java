package com.mycompany.e.trenuri;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LoginGUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton loginButton;
    private JButton registerButton;
    private JButton loginCasierButton;
    private JLabel errorLabel;

    private JLabel optionLabel;

    private ImageIcon userIcon;
    private JLabel userImage;

    ArrayList<User> users;

    public LoginGUI() {
        init();
    }

    public LoginGUI(Image icon) {
        this.setIconImage(icon);
        init();
    }



    private void init() {
        this.setTitle("Log in");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        users = User.initUsers();

        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.red);
        errorLabel.setBounds(220, 75, 200, 100);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 230, 150, 30);
        JCheckBox passwordCheck = new JCheckBox("Show Password");
        passwordCheck.setBounds(100,260,150,30);

        passwordCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(passwordCheck.isSelected()){
                    passwordField.setEchoChar((char)0);
                }else{
                    passwordField.setEchoChar('*');
                }
            }
        });

        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(20, 170, 80, 30);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 230, 80, 30);

        loginButton = new JButton("Log in");
        loginButton.setBounds(170, 300, 130, 30);
        loginButton.setBackground(new Color(51,120,255));
        loginButton.setForeground(Color.WHITE);

        loginCasierButton = new JButton("Casier");
        loginCasierButton.setBounds(400, 420, 80, 30);

        registerButton = new JButton("Register");
        registerButton.setBounds(170, 380, 130, 30);
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(new Color(0,153,0));

        usernameField = new JTextField();
        usernameField.setBounds(100, 170, 150, 30);

        optionLabel = new JLabel("Don't have an account? Register one!");
        optionLabel.setBounds(130, 340, 240, 30);
        Font optionFont = new Font(optionLabel.getFont().getName(),Font.ITALIC,optionLabel.getFont().getSize());
        optionLabel.setFont(optionFont);

        userImage = new JLabel();


        try {
            userIcon = new ImageIcon("C:\\Users\\maria\\OneDrive\\Desktop\\images.png");
            userImage.setIcon(userIcon);
            userImage.setBounds(130,10,240,140);
        }catch(Exception v){
            JOptionPane.showMessageDialog(null,"Au fost probleme la incarcarea imaginii user");

        }



        this.add(passwordField);
        this.add(usernameLabel);
        this.add(passwordLabel);
        this.add(loginButton);
        this.add(loginCasierButton);
        this.add(registerButton);
        this.add(usernameField);
        this.add(errorLabel);
        this.add(optionLabel);
        this.add(userImage);
        this.add(passwordCheck);
        this.setLayout(null);



        loginButton.addActionListener((ActionEvent e) -> {
            User user = login();
            if (user != null) {

                var mf = new MainFrame(user, this.getIconImage(), this);

                this.dispose();
            }
        });

        LoginGUI a = this;
        registerButton.addActionListener((ActionEvent e) -> {
            RegisterGUI frame = new RegisterGUI(a);
        });

        loginCasierButton.addActionListener((ActionEvent e) -> {
            var mf = new MainFrame(null, this.getIconImage(), this);
            this.setVisible(false);
        });



        this.setSize(500, 500);
        this.setVisible(true);
    }

    private User login() {

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if ("WUN".equals(user.tryLogin(usernameField.getText(), String.valueOf(passwordField.getPassword())))) {
                continue;
            }
            if ("WP".equals(user.tryLogin(usernameField.getText(), String.valueOf(passwordField.getPassword())))) {
                JOptionPane.showMessageDialog(this, "Wrong Password");
                return null;
            }
            return user;
        }
        JOptionPane.showMessageDialog(this, "Wrong Username");
        return null;
    }

}
