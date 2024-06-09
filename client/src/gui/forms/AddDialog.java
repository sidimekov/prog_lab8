package gui.forms;

import commandManagers.CommandInvoker;
import entity.Coordinates;
import entity.LocationFrom;
import entity.LocationTo;
import entity.Route;
import enums.ReadModes;
import gui.GuiManager;
import network.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddDialog extends JDialog {
    private GuiManager guiManager = GuiManager.getInstance();
    private JPanel addObjectPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField routeNameField;
    private JPanel coodinatesPanel;
    private JTextField coordinatesXField;
    private JTextField coordinatesYField;
    private JTextField routeDistanceField;
    private JScrollPane midScroll;
    private JPanel midPanel;
    private JPanel buttonLabel;
    private JPanel lowerPanel;
    private JLabel routeNameLabel;
    private JLabel routeDistanceLabel;
    private JLabel coordinatesLabel;
    private JLabel coordinatesXLabel;
    private JLabel coordinatesYLabel;
    private JLabel locationFromLabel;
    private JPanel locationFromPanel;
    private JTextField fromXField;
    private JTextField fromYField;
    private JTextField fromZField;
    private JPanel upperPanel;
    private JLabel headerLabel;
    private JTextField toXField;
    private JTextField toYField;
    private JLabel toXLabel;
    private JLabel toYLabel;
    private JTextField toZField;
    private JLabel toZLabel;
    private JTextField toNameField;
    private JLabel toNameLabel;
    private JLabel messageLabel;
    private JCheckBox addIfMinCheckbox;

    public AddDialog(Frame frame) {
        super(frame);

        setContentPane(addObjectPane);
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
        addObjectPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {

        String routeName = routeNameField.getText();
        String toName = toNameField.getText();
        double distance;
        double coordinatesX;
        Integer coordinatesY;
        int fromX;
        Integer fromY;
        float fromZ;
        float toX;
        Integer toY;
        long toZ;
        try {
            distance = Double.parseDouble(routeDistanceField.getText());
            coordinatesX = Double.parseDouble(coordinatesXField.getText());
            coordinatesY = Integer.parseInt(coordinatesYField.getText());
            fromX = Integer.parseInt(fromXField.getText());
            fromY = Integer.parseInt(fromYField.getText());
            fromZ = Float.parseFloat(fromZField.getText());
            toX = Float.parseFloat(toXField.getText());
            toY = Integer.parseInt(toYField.getText());
            toZ = Long.parseLong(toZField.getText());
        } catch (ClassCastException | NumberFormatException e) {
            messageLabel.setText(guiManager.getResourceBundle().getString("invalidValues"));
            return;
        }

        // сначала чек что норм объект
        if (!Route.checkName(routeName) || !Coordinates.checkX(coordinatesX) || !Coordinates.checkY(coordinatesY) || !LocationFrom.checkX(fromX) || !LocationFrom.checkY(fromY) || !LocationFrom.checkZ(fromZ) || !LocationTo.checkName(toName) || !LocationTo.checkX(toX) || !LocationTo.checkY(toY) || !LocationTo.checkZ(toZ) || !Route.checkDistance(distance)) {
            messageLabel.setText(guiManager.getResourceBundle().getString("invalidValues"));
            return;
        }

        Route route = new Route(routeName, new Coordinates(coordinatesX, coordinatesY), new LocationFrom(fromX, fromY, fromZ), new LocationTo(toName, toX, toY, toZ), distance);

        // на серв
        Response response;
        if (addIfMinCheckbox.isSelected()) {
            response = CommandInvoker.getInstance().runCommand("add_if_min", ReadModes.APP, route);
        } else {
            response = CommandInvoker.getInstance().runCommand("add", ReadModes.APP, route);
        }

        switch (response.getStatus()) {
            case OK -> {
                JOptionPane.showMessageDialog(null, guiManager.getResourceBundle().getString("addSuccess"));
                guiManager.getMainPanel().updateTableData();
                dispose();
            }
            case CLIENT_ERROR -> {
                messageLabel.setText("<html>" + response.getMessage());
                this.pack();
            }
            case SERVER_ERROR -> {
                messageLabel.setText(guiManager.getResourceBundle().getString("serverError"));
                this.pack();
            }
        }
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        AddDialog dialog = new AddDialog(GuiManager.getInstance().getFrame());
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
