package sstiscanner.utils;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.utilities.Utilities;
import sstiscanner.core.Attacker;
import sstiscanner.engines.Engine;
import sstiscanner.engines.Engines;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class ConfigWindow {

    MontoyaApi api;
    Attacker attacker;
    Engines engines;

    public ConfigWindow(MontoyaApi api, Attacker attacker, Engines engines) {
        this.api = api;
        this.attacker = attacker;
        this.engines = engines;
    }
    private JTextField customPayloadTextField;

    private String customPayload;

    public Component getUIComponent() {
        customPayload = "curl";

        customPayloadTextField = new JTextField(customPayload);
        JButton updateButton = new JButton("Update Custom Payload");
        JButton disableAllButton = new JButton("Disable All Engines"); // Add this button


        updateButton.addActionListener(e -> {
            customPayload = customPayloadTextField.getText();
            attacker.cmd = customPayload;
            api.logging().logToOutput("Custom payload updated: " + customPayload);
        });

        JPanel existingPanel = new JPanel();

        existingPanel.add(new JLabel("Custom payload:"));
        existingPanel.add(customPayloadTextField);
        existingPanel.add(updateButton);
        existingPanel.add(disableAllButton); // Add the "Disable All Checkboxes" button

        // Create a panel to hold the checkboxes
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));

        // Create action listener to handle checkbox state changes
        ActionListener checkBoxListener = e -> {
            for (Engine engine : engines.getEngines()) {
                if (e.getActionCommand().equals(engine.name)) {
                    engines.toggle(engine.name);
                    api.logging().logToOutput("Engine " + engine.name + " is " + (engine.isActivated ? "activated" : "deactivated"));
                    break;
                }
            }
        };

        disableAllButton.addActionListener(e -> {
            // Disable all checkboxes
            Component[] components = checkboxPanel.getComponents();
            for (Component component : components) {
                if (component instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) component;
                    checkBox.setSelected(false);
                    engines.toggle(checkBox.getActionCommand()); // Optionally, you can also toggle the corresponding engine
                }
            }
            api.logging().logToOutput("Disabled all engines");
        });

        // Create checkboxes for each engine and add them to the checkboxPanel
        for (Engine engine : engines.getEngines()) {
            JCheckBox checkBox = new JCheckBox(engine.name);
            checkBox.setSelected(engine.isActivated);
            checkBox.addActionListener(checkBoxListener);
            checkBox.setActionCommand(engine.name); // Set the action command as the engine name
            checkBox.setBorder(new EmptyBorder(5, 20, 0, 0));
            checkboxPanel.add(checkBox);
        }

        // Create a JScrollPane for the checkbox panel
        JScrollPane scrollPane = new JScrollPane(checkboxPanel);

        // Create a container panel for your existingPanel and the scrollPane
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BorderLayout());
        containerPanel.add(existingPanel, BorderLayout.NORTH); // Place your existingPanel at the top
        containerPanel.add(scrollPane, BorderLayout.CENTER);   // Place the checkboxPanel in the center

        return containerPanel;
    }

    public String getCustomPayload() {
        return customPayload;
    }
}