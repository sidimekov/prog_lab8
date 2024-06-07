package gui.forms;

import javax.swing.*;
import java.awt.event.*;

public class AddDialog extends JDialog {
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

    public AddDialog() {
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

        // на серв если круто то диспос, если нет то красные иконки

        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        AddDialog dialog = new AddDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
