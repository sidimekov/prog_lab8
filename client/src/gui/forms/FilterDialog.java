package gui.forms;

import gui.GuiManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel filterLabel;
    private JTextArea id;
    private JLabel idLabel;
    private JLabel routeNameLabel;
    private JLabel coordinatesXLabel;
    private JTextArea routeName;
    private JTextArea coordinatesX;
    private JTextArea coordinatesY;
    private JLabel coordinatesYLabel;
    private JTextArea fromX;
    private JLabel fromYLabel;
    private JLabel fromZLabel;
    private JLabel fromXLabel;
    private JTextArea fromY;
    private JTextArea fromZ;
    private JLabel toXLabel;
    private JTextArea toX;
    private JLabel toYLabel;
    private JTextArea toY;
    private JLabel toZLabel;
    private JTextArea toZ;
    private JLabel toNameLabel;
    private JTextArea toName;
    private JLabel distanceLabel;
    private JTextArea distance;

    public FilterDialog(Frame mainFrame) {
        super(mainFrame);

        setContentPane(contentPane);
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

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {

        Map<String, String> filterValues = new HashMap<>();

        filterValues.put("id", id.getText().trim());
        filterValues.put("routeName", routeName.getText().trim());
        filterValues.put("coordinatesX", coordinatesX.getText().trim());
        filterValues.put("coordinatesY", coordinatesY.getText().trim());
        filterValues.put("fromX", fromX.getText().trim());
        filterValues.put("fromY", fromY.getText().trim());
        filterValues.put("fromZ", fromZ.getText().trim());
        filterValues.put("toName", toName.getText().trim());
        filterValues.put("toX", toX.getText().trim());
        filterValues.put("toY", toY.getText().trim());
        filterValues.put("toZ", toZ.getText().trim());
        filterValues.put("distance", distance.getText().trim());

        GuiManager.getInstance().getMainPanel().applyFilter(filterValues);

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
