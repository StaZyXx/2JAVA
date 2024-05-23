package fr.newstaz.istore.ui.panel.stores;

import fr.newstaz.istore.controller.Controller;
import fr.newstaz.istore.model.Store;
import fr.newstaz.istore.model.User;
import fr.newstaz.istore.ui.component.ToastComponent;
import fr.newstaz.istore.ui.panel.HomePanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StoreManagement extends JPanel {

    private final Controller controller;
    private final JFrame mainFrame;

    private JTextField searchTextField;
    private JPanel storePanel;

    public StoreManagement(Controller controller, JFrame mainFrame) {
        this.controller = controller;
        this.mainFrame = mainFrame;
        init();
    }

    public void init() {
        setLayout(new BorderLayout());

        // Search field and button
        searchTextField = new JTextField();
        JButton searchButton = new JButton("SEARCH");

        searchButton.addActionListener(e -> displayStores(controller.getStoreController().searchStores(searchTextField.getText())));

        // Panel for stores
        storePanel = new JPanel();
        storePanel.setLayout(new BoxLayout(storePanel, BoxLayout.Y_AXIS));

        displayStores(controller.getStoreController().getAllStores());

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // Add store button
        JButton addButton = new JButton("AJOUTER UN MAGASIN");
        addButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            mainFrame.setContentPane(new AddStorePanel(controller, mainFrame));
            mainFrame.revalidate();
        }));
        if (controller.getAuthenticationController().getLoggedUser().getRole() == User.Role.ADMIN) {
            bottomPanel.add(addButton);
        }

        // Return button
        JButton backButton = new JButton("RETOUR");
        backButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            mainFrame.setContentPane(new HomePanel(controller, mainFrame));
            mainFrame.revalidate();
        }));
        bottomPanel.add(backButton);

        // Add components to the panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchTextField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new BorderLayout());
        JScrollPane comp = new JScrollPane(storePanel);
        comp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        comp.getVerticalScrollBar().setUnitIncrement(16);
        centerPanel.add(comp, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Method to display stores with buttons "MODIFIER"
    public void displayStores(List<Store> storeList) {
        storePanel.removeAll();
        for (Store store : storeList) {
            JPanel storeRow = new JPanel(new BorderLayout());
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            if (controller.getAuthenticationController().getLoggedUser().getRole() == User.Role.ADMIN) {
                JButton deleteButton = new JButton("DELETE");
                deleteButton.addActionListener(e -> {
                    boolean success = controller.getStoreController().deleteStore(store);
                    if (success) {
                        ToastComponent.showSuccessToast(this, "Store deleted");
                        SwingUtilities.invokeLater(() -> {
                            mainFrame.setContentPane(new StoreManagement(controller, mainFrame));
                            mainFrame.revalidate();
                        });
                    } else {
                        ToastComponent.showFailedToast(this, "Store not deleted");
                    }
                });
                buttonPanel.add(deleteButton, BorderLayout.WEST);
            }
            if (controller.getAuthenticationController().getLoggedUser().getRole() == User.Role.ADMIN || controller.getStoreController().getEmployeesPermissions(store).contains(controller.getAuthenticationController().getLoggedUser())){
                JButton manageEmployeesButton = new JButton("MANAGE EMPLOYEES");
                manageEmployeesButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
                    mainFrame.setContentPane(new UsersInStorePanel(mainFrame, controller, store));
                    mainFrame.revalidate();
                }));
                buttonPanel.add(manageEmployeesButton);
            }
            JButton manageInventoryButton = new JButton("MANAGE INVENTORY");
            manageInventoryButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
                mainFrame.setContentPane(new InventoryManagement(mainFrame, controller, store));
                mainFrame.revalidate();
            }));
            JPanel storeDetailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            storeDetailsPanel.add(new JLabel("Store Name: " + store.getName()));

            buttonPanel.add(manageInventoryButton);
            storeRow.add(storeDetailsPanel, BorderLayout.CENTER);
            storeRow.add(buttonPanel, BorderLayout.EAST);

            storePanel.add(storeRow);
        }
        storePanel.revalidate();
        storePanel.repaint();
    }
}