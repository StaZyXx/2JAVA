package fr.newstaz.istore.ui;

import fr.newstaz.istore.controller.Controller;
import fr.newstaz.istore.ui.panel.LoginPanel;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame(Controller controller) {
        super("iStore");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LoginPanel loginPanel = new LoginPanel(controller, this);

        add(loginPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
