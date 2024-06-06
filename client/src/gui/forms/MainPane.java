package gui.forms;

import commandManagers.CommandInvoker;
import enums.ReadModes;
import gui.GuiManager;
import network.Response;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

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
}
