package fr.newstaz.istore.ui.panel.users;

import fr.newstaz.istore.controller.Controller;
import fr.newstaz.istore.model.User;
import fr.newstaz.istore.response.UserResponse;
import fr.newstaz.istore.ui.component.ToastComponent;

import javax.swing.*;
import java.awt.*;

public class AddUserPanel extends JPanel {

    private final Controller controller;
    private final JFrame mainFrame;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<User.Role> roleBox;

    public AddUserPanel(Controller controller, JFrame mainFrame) {
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
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel roleLabel = new JLabel("Role:");
        add(roleLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        roleBox = new JComboBox();
        roleBox.addItem(User.Role.USER);
        roleBox.addItem(User.Role.ADMIN);
        add(roleBox, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        JButton cancelButton = new JButton("CANCEL");
        cancelButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            mainFrame.setContentPane(new UserManagementPanel(controller, mainFrame));
            mainFrame.revalidate();
        }));
        add(cancelButton, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JButton addButton = new JButton("ADD");
        addButton.addActionListener(e -> SwingUtilities.invokeLater(this::performAdd));
        add(addButton, gbc);
    }

    private void performAdd() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        User.Role role = (User.Role) roleBox.getSelectedItem();

        if (email.isEmpty() || password.isEmpty()) {
            ToastComponent.showFailedToast(this, "Email or password empty");
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        UserResponse.CreateUserResponse createUserResponse = controller.getUserController().createUser(user);
        if (createUserResponse.success()) {
            ToastComponent.showSuccessToast(this, createUserResponse.message());
            SwingUtilities.invokeLater(() -> {
                mainFrame.setContentPane(new UserManagementPanel(controller, mainFrame));
                mainFrame.revalidate();
            });
        } else {
            ToastComponent.showFailedToast(this, createUserResponse.message());
        }
    }
}
