package gui.forms;

import commandManagers.CommandInvoker;
import enums.ReadModes;
import gui.GuiManager;
import network.Response;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPane {
    private GuiManager guiManager = GuiManager.getInstance();
    private JPanel mainPanel;
    private JTable mainTable;
    private JButton visualizeButton;
    private JButton addButton;
    private JPanel upperPanel;
    private JPanel lowerPanel;
    private JLabel username;
    private JLabel mainHeader;
    private JButton removeButton;
    private JButton otherButton;
    private JScrollPane tablePanel;
    private JButton logoutButton;
    private JButton refreshButton;

    public MainPane() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiManager.openAddDialog();
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response response = CommandInvoker.getInstance().runCommand("logout", ReadModes.APP);
                switch (response.getStatus()) {
                    case OK -> {
                        guiManager.openSignInPanel();
                    }
                }
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = mainTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(guiManager.getFrame(), GuiManager.FONT_HTML_STRING + guiManager.getResourceBundle().getString("removeErrorSelectFirst"));
                } else {
                    Long id = (Long) mainTable.getModel().getValueAt(row, 0);
                    String name = (String) mainTable.getModel().getValueAt(row, 1);

                    String confirmMessage = GuiManager.FONT_HTML_STRING + guiManager.getResourceBundle().getString("removeConfirm").replace("$name$", name);
                    String confirmTitle = guiManager.getResourceBundle().getString("removeById");

                    int result = JOptionPane.showConfirmDialog(guiManager.getFrame(), confirmMessage, confirmTitle, JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION) {

                        Response response = CommandInvoker.getInstance().runCommand(String.format("remove_by_id %s", id), ReadModes.APP);
                        switch (response.getStatus()) {
                            case OK -> {
                                String removeOk = GuiManager.FONT_HTML_STRING + guiManager.getResourceBundle().getString("removeOk").replace("$name$", name);
                                JOptionPane.showMessageDialog(guiManager.getFrame(), removeOk);
                                updateTableData();
                            }
                            case CLIENT_ERROR -> JOptionPane.showMessageDialog(guiManager.getFrame(), GuiManager.FONT_HTML_STRING + response.getMessage());
                            case SERVER_ERROR -> JOptionPane.showMessageDialog(guiManager.getFrame(), GuiManager.FONT_HTML_STRING + guiManager.getResourceBundle().getString("serverError"));
                        }

                    } else {

                    }
                }
            }
        });
        otherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiManager.openOtherCommandsDialog();
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTableData();
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
    public void updateTableData() {

        Response response = CommandInvoker.getInstance().runCommand("show", ReadModes.APP);

        switch (response.getStatus()) {
            case OK -> {
                TableModel tableModel;
                try {
                    tableModel = (TableModel) response.getObject();
                } catch (ClassCastException e) {
                    mainHeader.setForeground(Color.RED);
                    mainHeader.setText(guiManager.getResourceBundle().getString("serverError"));
                    break;
                }

                TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(tableModel);
                mainTable.setRowSorter(rowSorter);
                mainTable.setModel(tableModel);
            }
            case CLIENT_ERROR -> {
                mainHeader.setForeground(Color.RED);
                mainHeader.setText(response.getMessage());
            }
            case SERVER_ERROR -> {
                mainHeader.setForeground(Color.RED);
                mainHeader.setText(guiManager.getResourceBundle().getString("serverError"));
            }
        }
    }

    public void updateUserLabel(String login) {
        username.setText(login);
    }
}
