package fr.newstaz.istore.ui.panel;

import fr.newstaz.istore.controller.Controller;
import fr.newstaz.istore.response.LoginResponse;
import fr.newstaz.istore.ui.component.ToastComponent;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private final Controller controller;
    private final JFrame mainFrame;

    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginPanel(Controller controller, JFrame mainFrame) {
        this.controller = controller;
        this.mainFrame = mainFrame;
        init();
    }

    public void init() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel emailLabel = new JLabel("Email:");
        add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        emailField = new JTextField(20);
        add(emailField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel passwordLabel = new JLabel("Password:");
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            SwingUtilities.invokeLater(this::performLogin);
        });
        add(loginButton, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                mainFrame.getContentPane().removeAll();
                mainFrame.setContentPane(new RegisterPanel(controller, mainFrame));
                mainFrame.revalidate();
            });
        });
        add(registerButton, gbc);
    }

    private void performLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        LoginResponse loginResponse = controller.getAuthenticationController().login(email, password);
        if (loginResponse.success()) {
            SwingUtilities.invokeLater(() -> {
                mainFrame.getContentPane().removeAll();
                mainFrame.setContentPane(new HomePanel(controller, mainFrame));
                mainFrame.revalidate();
            });
            ToastComponent.showSuccessToast(this, loginResponse.message());
        } else {
            ToastComponent.showFailedToast(this, loginResponse.message());
        }
    }
}
