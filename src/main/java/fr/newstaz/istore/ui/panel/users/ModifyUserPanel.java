package fr.newstaz.istore.ui.panel.users;

import fr.newstaz.istore.controller.Controller;
import fr.newstaz.istore.model.User;
import fr.newstaz.istore.response.UserResponse;
import fr.newstaz.istore.ui.component.ToastComponent;

import javax.swing.*;
import java.awt.*;

public class ModifyUserPanel extends JPanel {

    private final Controller controller;
    private final JFrame mainFrame;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<User.Role> roleBox;


    public ModifyUserPanel(Controller controller, JFrame mainFrame, User user) {
        this.controller = controller;
        this.mainFrame = mainFrame;
        init(user);
    }

    public void init(User user) {
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
        emailField.setText(user.getEmail());
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
        roleBox = new JComboBox(User.Role.values());
        roleBox.setSelectedItem(user.getRole());
        add(roleBox, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;
        JButton editButton = new JButton("Modifier");
        editButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> performEdit(user));
        });
        add(editButton, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;
        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> performDelete(user));
        });
        add(deleteButton, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;
        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            mainFrame.setContentPane(new UserManagementPanel(controller, mainFrame));
            mainFrame.revalidate();
        }));
        add(cancelButton, gbc);

        if (controller.getAuthenticationController().getLoggedUser().equals(user)) {
            roleBox.setEnabled(false);
            deleteButton.setEnabled(false);
        }
    }

    private void performEdit(User user) {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        User.Role role = (User.Role) roleBox.getSelectedItem();

        if (email.isEmpty()) {
            email = user.getEmail();
        }
        if (password.isEmpty()) {
            password = user.getPassword();
        }

        UserResponse.EditUserResponse editUserResponse = controller.getUserController().editUser(user, email, password, role);
        if (!editUserResponse.success()) {
            ToastComponent.showFailedToast(this, editUserResponse.message());
            return;
        }

        ToastComponent.showSuccessToast(this, editUserResponse.message());
        SwingUtilities.invokeLater(() -> {
            mainFrame.setContentPane(new UserManagementPanel(controller, mainFrame));
            mainFrame.revalidate();
        });
    }

    private void performDelete(User user) {
        UserResponse.DeleteUserResponse deleteUserResponse = controller.getUserController().deleteUser(user);
        if (!deleteUserResponse.success()) {
            ToastComponent.showFailedToast(this, deleteUserResponse.message());
            return;
        }

        ToastComponent.showSuccessToast(this, deleteUserResponse.message());
        SwingUtilities.invokeLater(() -> {
            mainFrame.setContentPane(new UserManagementPanel(controller, mainFrame));
            mainFrame.revalidate();
        });
    }
}
