package gui.forms;

import util.CommandInvoker;
import entity.Route;
import enums.Commands;
import enums.ReadModes;
import gui.GuiManager;
import network.Response;
import util.LocalizationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ResourceBundle;

public class OtherCommandsDialog extends JDialog {
    GuiManager guiManager = GuiManager.getInstance();
    ResourceBundle resourceBundle = LocalizationManager.getBundle();
    Route specifiedRoute = null;
    File chosenFile = null;

    private JPanel commandsPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel lowerPanel;
    private JPanel buttonLabel;
    private JLabel messageLabel;
    private JPanel midPanel;
    private JPanel commandPanel;
    private JPanel removeByDistanceCard;
    private JPanel removeFirstCard;
    private JPanel removeGreaterCard;
    private JComboBox<Commands> commandCombo;
    private JPanel upperPanel;
    private JLabel headerLabel;
    private JPanel defaultCard;
    private JLabel chooseCommandLabel;
    private JLabel removeByDistanceLabel;
    private JLabel removeFirstLabel;
    private JLabel removeGreaterLabel;
    private JPanel removeByDistancePanel;
    private JTextField removeByDistanceField;
    private JLabel removeByDistanceFieldLabel;
    private JPanel updateCard;
    private JLabel updateLabel;
    private JButton removeGreaterSpecifyButton;
    private JButton updateSpecifyButton;
    private JLabel updateIdLabel;
    private JTextField updateIdField;
    private JPanel updateIdPanel;
    private JPanel clearCard;
    private JLabel clearLabel;
    private JPanel countGreaterThanDistanceCard;
    private JPanel executeScriptCard;
    private JLabel countGreaterThanDistanceLabel;
    private JTextField countGreaterThanDistanceField;
    private JLabel countGreaterThanDistanceFieldLabel;
    private JLabel executeScriptLabel;
    private JButton executeScriptSpecifyButton;

    public OtherCommandsDialog(Frame frame) {
        super(frame);

        commandCombo.setFont(guiManager.getDefaultFont());
        commandCombo.addItem(null);

        Commands update = Commands.UPDATE;
        update.setDisplayText(resourceBundle.getString("update"));
        commandCombo.addItem(update);

        Commands clear = Commands.CLEAR;
        clear.setDisplayText(resourceBundle.getString("clear"));
        commandCombo.addItem(clear);

        Commands countGreaterThanDistance = Commands.COUNT_GREATER_THAN_DISTANCE;
        countGreaterThanDistance.setDisplayText(resourceBundle.getString("countGreaterThanDistance"));
        commandCombo.addItem(countGreaterThanDistance);

        Commands executeScript = Commands.EXECUTE_SCRIPT;
        executeScript.setDisplayText(resourceBundle.getString("executeScript"));
        commandCombo.addItem(executeScript);

        Commands removeAllByDistance = Commands.REMOVE_ALL_BY_DISTANCE;
        removeAllByDistance.setDisplayText(resourceBundle.getString("removeAllByDistance"));
        commandCombo.addItem(removeAllByDistance);

        Commands removeFirst = Commands.REMOVE_FIRST;
        removeFirst.setDisplayText(resourceBundle.getString("removeFirst"));
        commandCombo.addItem(removeFirst);

        Commands removeGreater = Commands.REMOVE_GREATER;
        removeGreater.setDisplayText(resourceBundle.getString("removeGreater"));
        commandCombo.addItem(removeGreater);

        setContentPane(commandsPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        commandsPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        commandCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    CardLayout cl = (CardLayout) commandPanel.getLayout();
                    cl.show(commandPanel, ((Commands) e.getItem()).name());
                }
            }
        });

        ActionListener specifyRouteListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                specifiedRoute = guiManager.specifyRouteDialog(specifiedRoute);

                if (specifiedRoute != null) {
                    messageLabel.setForeground(Color.BLACK);
                    messageLabel.setText("Route specified");
                }
            }
        };

        removeGreaterSpecifyButton.addActionListener(specifyRouteListener);
        updateSpecifyButton.addActionListener(specifyRouteListener);
        executeScriptSpecifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chosenFile = guiManager.chooseFile(chosenFile);
                messageLabel.setForeground(Color.BLACK);
                messageLabel.setText(resourceBundle.getString("fileChosen"));
            }
        });
    }

    private void onOK() {

        if (commandCombo.getSelectedItem() != null) {
            switch ((Commands) commandCombo.getSelectedItem()) {
                case REMOVE_GREATER -> {
                    if (specifiedRoute != null) {

                        Response response = CommandInvoker.getInstance().runCommand("remove_greater", ReadModes.APP, specifiedRoute);

                        switch (response.getStatus()) {
                            case OK -> {
                                JOptionPane.showMessageDialog(null, LocalizationManager.getString("removeGreaterSuccess"));
                                guiManager.getMainPanel().updateTableData();
                                specifiedRoute = null;
                                dispose();
                            }
                            case CLIENT_ERROR -> {
                                messageLabel.setForeground(Color.RED);
                                messageLabel.setText(GuiManager.FONT_HTML_STRING + response.getMessage());
                            }
                            case SERVER_ERROR -> {
                                messageLabel.setForeground(Color.RED);
                                messageLabel.setText(GuiManager.FONT_HTML_STRING + LocalizationManager.getString("serverError"));
                            }
                        }
                    } else {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText(LocalizationManager.getString("errorSpecifyRoute"));
                    }
                }
                case REMOVE_ALL_BY_DISTANCE -> {
                    try {
                        double distance = Double.parseDouble(removeByDistanceField.getText());


                        Response response = CommandInvoker.getInstance().runCommand(String.format("remove_all_by_distance %s", distance), ReadModes.APP);

                        switch (response.getStatus()) {
                            case OK -> {
                                JOptionPane.showMessageDialog(null, LocalizationManager.getString("removeByDistanceSuccess"));
                                guiManager.getMainPanel().updateTableData();
                                dispose();
                            }
                            case CLIENT_ERROR -> {
                                messageLabel.setForeground(Color.RED);
                                messageLabel.setText(GuiManager.FONT_HTML_STRING + response.getMessage());
                            }
                            case SERVER_ERROR -> {
                                messageLabel.setForeground(Color.RED);
                                messageLabel.setText(GuiManager.FONT_HTML_STRING + LocalizationManager.getString("serverError"));
                            }
                        }

                    } catch (NumberFormatException e) {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText(LocalizationManager.getString("invalidDistance"));
                    }
                }
                case REMOVE_FIRST -> {
                    Response response = CommandInvoker.getInstance().runCommand("remove_first", ReadModes.APP);

                    switch (response.getStatus()) {
                        case OK -> {
                            JOptionPane.showMessageDialog(null, LocalizationManager.getString("removeFirstSuccess"));
                            guiManager.getMainPanel().updateTableData();
                            dispose();
                        }
                        case CLIENT_ERROR -> {
                            messageLabel.setForeground(Color.RED);
                            messageLabel.setText(GuiManager.FONT_HTML_STRING + response.getMessage());
                        }
                        case SERVER_ERROR -> {
                            messageLabel.setForeground(Color.RED);
                            messageLabel.setText(GuiManager.FONT_HTML_STRING + LocalizationManager.getString("serverError"));
                        }
                    }
                }
                case UPDATE -> {
                    try {
                        long id = Long.parseLong(updateIdField.getText());
                        if (specifiedRoute != null) {

                            specifiedRoute.setId(id);
                            Response response = CommandInvoker.getInstance().runCommand("update", ReadModes.APP, specifiedRoute);

                            switch (response.getStatus()) {
                                case OK -> {
                                    String displayMessage = LocalizationManager.getString("updateSuccess");
                                    displayMessage = displayMessage.replace("$id$", String.valueOf(id));
                                    displayMessage = displayMessage.replace("$name$", specifiedRoute.getName());
                                    JOptionPane.showMessageDialog(null, displayMessage);
                                    guiManager.getMainPanel().updateTableData();
                                    specifiedRoute = null;
                                    dispose();
                                }
                                case CLIENT_ERROR -> {
                                    messageLabel.setForeground(Color.RED);
                                    messageLabel.setText(GuiManager.FONT_HTML_STRING + response.getMessage());
                                }
                                case SERVER_ERROR -> {
                                    messageLabel.setForeground(Color.RED);
                                    messageLabel.setText(GuiManager.FONT_HTML_STRING + LocalizationManager.getString("serverError"));
                                }
                            }
                        } else {
                            messageLabel.setForeground(Color.RED);
                            messageLabel.setText(LocalizationManager.getString("errorSpecifyRoute"));
                        }
                    } catch (NumberFormatException e) {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText(LocalizationManager.getString("invalidId"));
                    }
                }
                case CLEAR -> {
                    Response response = CommandInvoker.getInstance().runCommand("clear", ReadModes.APP);

                    switch (response.getStatus()) {
                        case OK -> {
                            String displayMessage = LocalizationManager.getString("clearSuccess");
                            JOptionPane.showMessageDialog(null, displayMessage);
                            guiManager.getMainPanel().updateTableData();
                            dispose();
                        }
                        case CLIENT_ERROR -> {
                            messageLabel.setForeground(Color.RED);
                            messageLabel.setText(GuiManager.FONT_HTML_STRING + response.getMessage());
                        }
                        case SERVER_ERROR -> {
                            messageLabel.setForeground(Color.RED);
                            messageLabel.setText(GuiManager.FONT_HTML_STRING + LocalizationManager.getString("serverError"));
                        }
                    }
                }
                case COUNT_GREATER_THAN_DISTANCE -> {
                    try {
                        double id = Double.parseDouble(countGreaterThanDistanceField.getText());

                        Response response = CommandInvoker.getInstance().runCommand(String.format("count_greater_than_distance %s", id), ReadModes.APP);

                        switch (response.getStatus()) {
                            case OK -> {
                                JOptionPane.showMessageDialog(null, response.getMessage());
                                guiManager.getMainPanel().updateTableData();
                            }
                            case CLIENT_ERROR -> {
                                messageLabel.setForeground(Color.RED);
                                messageLabel.setText(GuiManager.FONT_HTML_STRING + response.getMessage());
                            }
                            case SERVER_ERROR -> {
                                messageLabel.setForeground(Color.RED);
                                messageLabel.setText(GuiManager.FONT_HTML_STRING + LocalizationManager.getString("serverError"));
                            }
                        }
                    } catch (NumberFormatException e) {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText(resourceBundle.getString("invalidDistance"));
                    }
                }
                case EXECUTE_SCRIPT -> {
                    File script = chosenFile;
                    if (script != null) {

                        Response response = CommandInvoker.getInstance().runCommand(String.format("execute_script %s", script.getPath()), ReadModes.APP);

                        switch (response.getStatus()) {
                            case OK -> {
                                JOptionPane.showMessageDialog(null, response.getMessage());
                                guiManager.getMainPanel().updateTableData();
                                chosenFile = null;
                                dispose();
                            }
                            case CLIENT_ERROR -> {
                                messageLabel.setForeground(Color.RED);
                                messageLabel.setText(GuiManager.FONT_HTML_STRING + response.getMessage());
                            }
                            case SERVER_ERROR -> {
                                messageLabel.setForeground(Color.RED);
                                messageLabel.setText(GuiManager.FONT_HTML_STRING + LocalizationManager.getString("serverError"));
                            }
                        }
                    } else {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText(GuiManager.FONT_HTML_STRING + LocalizationManager.getString("chooseFileFirst"));
                    }
                }
            }
        } else {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(LocalizationManager.getString("specifyCommand"));
        }


    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        OtherCommandsDialog dialog = new OtherCommandsDialog(GuiManager.getInstance().getMainFrame());
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }


    private void createUIComponents() {
        commandCombo = new JComboBox<>();
    }
}
