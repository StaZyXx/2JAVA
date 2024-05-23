package fr.newstaz.istore.ui.panel.stores;

import fr.newstaz.istore.controller.Controller;
import fr.newstaz.istore.model.Store;
import fr.newstaz.istore.model.User;
import fr.newstaz.istore.response.StoreResponse;
import fr.newstaz.istore.ui.component.ToastComponent;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UsersInStorePanel extends JPanel {

    private final JFrame mainFrame;
    private final Controller controller;
    private final Store store;
    private JPanel usersPanel;

    public UsersInStorePanel(JFrame mainFrame, Controller controller, Store store) {
        this.mainFrame = mainFrame;
        this.controller = controller;
        this.store = store;
        init();
    }

    public void init() {
        setLayout(new BorderLayout());

        usersPanel = new JPanel();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
        displayUsers(controller.getStoreController().getEmployees(store));

        JScrollPane scrollPane = new JScrollPane(usersPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        if (controller.getAuthenticationController().getLoggedUser().getRole() == User.Role.ADMIN) {
            JButton addEmployeeButton = new JButton("AJOUTER UN EMPLOYÉ");
            addEmployeeButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
                mainFrame.setContentPane(new AddUserToStorePanel(controller, mainFrame, store));
                mainFrame.revalidate();
            }));
            bottomPanel.add(addEmployeeButton);
        }

        JButton backButton = new JButton("BACK");
        backButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            mainFrame.setContentPane(new StoreManagement(controller, mainFrame));
            mainFrame.revalidate();
        }));
        bottomPanel.add(backButton);

        add(scrollPane, BorderLayout.CENTER); // Add the scrollPane instead of usersPanel directly
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void displayUsers(List<User> userList) {
        usersPanel.removeAll();

        for (User user : userList) {
            JPanel userRow = new JPanel(new BorderLayout());
            JPanel userDetailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            userDetailsPanel.add(new JLabel("Email: " + user.getEmail()));

            if (controller.getAuthenticationController().getLoggedUser().getRole() == User.Role.ADMIN){
                if (controller.getStoreController().getEmployeesPermissions(store).contains(user)) {
                    JButton removePermissionsButton = new JButton("REMOVE PERMISSIONS");
                    removePermissionsButton.addActionListener(e -> {
                        StoreResponse.RemovePermissionResponse removePermission = controller.getStoreController().removePermission(store, user);
                        if (removePermission.success()) {
                            ToastComponent.showSuccessToast(this, removePermission.message());
                        } else {
                            ToastComponent.showFailedToast(this, removePermission.message());
                        }
                        displayUsers(userList);
                    });
                    userDetailsPanel.add(removePermissionsButton);
                }else {
                    JButton setPermissionsButton = new JButton("SET PERMISSIONS");
                    setPermissionsButton.addActionListener(e -> {
                        StoreResponse.AddPermissionResponse addPermissionResponse = controller.getStoreController().addPermission(store, user);
                        if (addPermissionResponse.success()) {
                            ToastComponent.showSuccessToast(this, addPermissionResponse.message());
                        } else {
                            ToastComponent.showFailedToast(this, addPermissionResponse.message());
                        }
                        displayUsers(userList);
                    });
                    userDetailsPanel.add(setPermissionsButton);
                }
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton deleteButton = new JButton("DELETE");
                deleteButton.addActionListener(e -> {
                    StoreResponse.RemoveEmployeeResponse removeEmployeeResponse = controller.getStoreController().removeEmployee(store, user);
                    if (removeEmployeeResponse.success()) {
                        ToastComponent.showSuccessToast(this, removeEmployeeResponse.message());
                        SwingUtilities.invokeLater(() -> {
                            mainFrame.setContentPane(new UsersInStorePanel(mainFrame, controller, store));
                            mainFrame.revalidate();
                        });
                    } else {
                        ToastComponent.showFailedToast(this, removeEmployeeResponse.message());
                    }
                });
                buttonPanel.add(deleteButton);
                userRow.add(buttonPanel, BorderLayout.EAST);
            }
            userRow.add(userDetailsPanel, BorderLayout.CENTER);
            usersPanel.add(userRow);
        }

        usersPanel.revalidate();
        usersPanel.repaint();
    }
}