package gui.forms;

import entity.Coordinates;
import entity.LocationFrom;
import entity.LocationTo;
import entity.Route;
import gui.GuiManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SpecifyRouteDialog extends JDialog {
    GuiManager guiManager = GuiManager.getInstance();

    private JPanel specifyRoutePane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel lowerPanel;
    private JPanel buttonLabel;
    private JLabel messageLabel;
    private JPanel upperPanel;
    private JLabel headerLabel;
    private JPanel midPanel;
    private JTextField routeNameField;
    private JLabel routeNameLabel;
    private JTextField routeDistanceField;
    private JLabel routeDistanceLabel;
    private JPanel coodinatesPanel;
    private JTextField coordinatesXField;
    private JTextField coordinatesYField;
    private JLabel coordinatesXLabel;
    private JLabel coordinatesYLabel;
    private JLabel coordinatesLabel;
    private JLabel locationFromLabel;
    private JPanel locationFromPanel;
    private JTextField fromXField;
    private JTextField fromYField;
    private JTextField fromZField;
    private JTextField toXField;
    private JTextField toYField;
    private JLabel toXLabel;
    private JLabel toYLabel;
    private JTextField toZField;
    private JLabel toZLabel;
    private JTextField toNameField;
    private JLabel toNameLabel;

    private Route specifiedRoute;

    public SpecifyRouteDialog(Frame frame) {
        new SpecifyRouteDialog(frame, null);
    }

    public SpecifyRouteDialog(Frame frame, Route route) {
        super(frame);

        setContentPane(specifyRoutePane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setPreferredSize(new Dimension(800, 300));
        pack();
        setLocationRelativeTo(null);

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
        specifyRoutePane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // если specifiedRoute уже указан, то для удобства восстанавливаем поля
        if (route != null) {
            routeNameField.setText(route.getName());
            routeDistanceField.setText(String.valueOf(route.getDistance()));
            coordinatesXField.setText(String.valueOf(route.getCoordinates().getX()));
            coordinatesYField.setText(String.valueOf(route.getCoordinates().getY()));
            fromXField.setText(String.valueOf(route.getFrom().getX()));
            fromYField.setText(String.valueOf(route.getFrom().getY()));
            fromZField.setText(String.valueOf(route.getFrom().getZ()));
            toNameField.setText(route.getTo().getName());
            toXField.setText(String.valueOf(route.getTo().getX()));
            toYField.setText(String.valueOf(route.getTo().getY()));
            toZField.setText(String.valueOf(route.getTo().getZ()));
        }
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

        specifiedRoute = new Route(routeName, new Coordinates(coordinatesX, coordinatesY), new LocationFrom(fromX, fromY, fromZ), new LocationTo(toName, toX, toY, toZ), distance);

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        SpecifyRouteDialog dialog = new SpecifyRouteDialog(GuiManager.getInstance().getFrame());
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public Route getSpecifiedRoute() {
        return specifiedRoute;
    }

    public void setSpecifiedRoute(Route specifiedRoute) {
        this.specifiedRoute = specifiedRoute;
    }
}
