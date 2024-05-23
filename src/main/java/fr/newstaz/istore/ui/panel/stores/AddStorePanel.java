package fr.newstaz.istore.ui.panel.stores;

import fr.newstaz.istore.controller.Controller;
import fr.newstaz.istore.response.StoreResponse;
import fr.newstaz.istore.ui.component.ToastComponent;
import fr.newstaz.istore.ui.panel.HomePanel;

import javax.swing.*;
import java.awt.*;

public class AddStorePanel extends JPanel {
    private final Controller controller;
    private final JFrame mainFrame;

    public AddStorePanel(Controller controller, JFrame mainFrame) {
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

        JLabel nameLabel = new JLabel("Name:");
        add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JTextField nameField = new JTextField(20);
        add(nameField, gbc);

        JButton createButton = new JButton("Create");
        gbc.gridx = 1;
        gbc.gridy = 1;
        createButton.addActionListener(e -> {
            String name = nameField.getText();
            if (name.isEmpty()) {
                ToastComponent.showFailedToast(this, "Name cannot be empty");
                return;
            }
            StoreResponse.CreateStoreResponse store = controller.getStoreController().createStore(name);
            if (store.success()) {
                ToastComponent.showSuccessToast(this, store.message());
                SwingUtilities.invokeLater(() -> {
                    mainFrame.setContentPane(new StoreManagement(controller, mainFrame));
                    mainFrame.revalidate();
                });
            } else {
                ToastComponent.showFailedToast(this, store.message());
            }
        });
        add(createButton, gbc);

        JButton backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 1;
        backButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                mainFrame.setContentPane(new HomePanel(controller, mainFrame));
                mainFrame.revalidate();
            });
        });
        add(backButton, gbc);

    }
}
