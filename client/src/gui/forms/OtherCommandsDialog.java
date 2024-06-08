package gui.forms;

import commandManagers.CommandInvoker;
import entity.Route;
import enums.ReadModes;
import gui.GuiManager;
import network.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OtherCommandsDialog extends JDialog {
    GuiManager guiManager = GuiManager.getInstance();
    Route specifiedRoute = null;

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
    private JComboBox commandCombo;
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

    public OtherCommandsDialog() {
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
                    cl.show(commandPanel, (String) e.getItem());
                }
            }
        });
        removeGreaterSpecifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                specifiedRoute = guiManager.specifyRouteDialog(specifiedRoute);

                if (specifiedRoute != null) {
                    messageLabel.setForeground(Color.BLACK);
                    messageLabel.setText("Route specified");
                }
            }
        });
    }

    private void onOK() {
        // на серв

        if (specifiedRoute != null) {

            Response response = CommandInvoker.getInstance().runCommand("remove_greater", ReadModes.APP, specifiedRoute);
            specifiedRoute = null;

            switch (response.getStatus()) {
                case OK -> {
                    JOptionPane.showMessageDialog(null, guiManager.getResourceBundle().getString("removeGreaterSuccess"));
                    guiManager.getMainPanel().updateTableData();
                    dispose();
                }
                case CLIENT_ERROR -> {
                    messageLabel.setText(GuiManager.FONT_HTML_STRING + response.getMessage());
                }
                case SERVER_ERROR -> {
                    messageLabel.setText(GuiManager.FONT_HTML_STRING + guiManager.getResourceBundle().getString("serverError"));
                }
            }
            dispose();
        } else {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(guiManager.getResourceBundle().getString("errorSpecifyRoute"));
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        OtherCommandsDialog dialog = new OtherCommandsDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
