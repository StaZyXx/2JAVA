package fr.newstaz.istore.ui.panel.users;

import fr.newstaz.istore.controller.Controller;
import fr.newstaz.istore.model.User;
import fr.newstaz.istore.ui.panel.HomePanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserManagementPanel extends JPanel {

    private final Controller controller;
    private final JFrame mainFrame;

    private JTextField searchTextField;
    private JPanel userPanel;

    public UserManagementPanel(Controller controller, JFrame mainFrame) {
        this.controller = controller;
        this.mainFrame = mainFrame;
        init();
    }

    public void init() {
        setLayout(new BorderLayout());

        searchTextField = new JTextField();
        JButton searchButton = new JButton("SEARCH");

        searchButton.addActionListener(e -> displayUsers(controller.getUserController().searchUsers(searchTextField.getText())));

        userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));

        displayUsers(controller.getUserController().getAllUsers());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        if (controller.getAuthenticationController().getLoggedUser() != null && controller.getAuthenticationController().getLoggedUser().getRole() == User.Role.ADMIN) {
            JButton addButton = new JButton("AJOUTER UN UTILISATEUR");
            addButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
                mainFrame.setContentPane(new AddUserPanel(controller, mainFrame));
                mainFrame.revalidate();
            }));
            bottomPanel.add(addButton);
        }

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchTextField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new BorderLayout());
        JScrollPane comp = new JScrollPane(userPanel);

        comp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        comp.getVerticalScrollBar().setUnitIncrement(16);

        centerPanel.add(comp, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("RETOUR");
        backButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            mainFrame.setContentPane(new HomePanel(controller, mainFrame));
            mainFrame.revalidate();
        }));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void displayUsers(List<User> userList) {
        userPanel.removeAll();

        for (User user : userList) {
            User loggedUser = controller.getAuthenticationController().getLoggedUser();
            JPanel userRow = new JPanel(new BorderLayout());

            if (loggedUser != null && loggedUser.getRole() == User.Role.ADMIN) {
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton modifyButton = new JButton("MODIFIER");
                modifyButton.addActionListener(e -> {
                    SwingUtilities.invokeLater(() -> {
                        mainFrame.setContentPane(new ModifyUserPanel(controller, mainFrame, user));
                        mainFrame.revalidate();
                    });
                });

                JButton verifyButton = new JButton("VERIFIER");
                if (!user.isVerified()) {
                    verifyButton.addActionListener(e -> {
                        SwingUtilities.invokeLater(() -> {
                            controller.getUserController().verifyUser(user);
                            mainFrame.setContentPane(new UserManagementPanel(controller, mainFrame));
                            mainFrame.revalidate();
                        });
                    });
                    buttonPanel.add(verifyButton);
                }

                buttonPanel.add(modifyButton);


                JPanel userDetailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                userDetailsPanel.add(new JLabel("Email: " + user.getEmail()));
                userDetailsPanel.add(new JLabel("Role: " + user.getRole()));

                userRow.add(userDetailsPanel, BorderLayout.CENTER);
                userRow.add(buttonPanel, BorderLayout.EAST);
            } else {
                JPanel userDetailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                userDetailsPanel.add(new JLabel("Email: " + user.getEmail()));
                userDetailsPanel.add(new JLabel("Role: " + user.getRole()));

                userRow.add(userDetailsPanel, BorderLayout.CENTER);
            }

            userPanel.add(userRow);
        }

        userPanel.revalidate();
        userPanel.repaint();
    }
}

