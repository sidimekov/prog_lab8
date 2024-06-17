package gui.forms;

import util.CommandInvoker;
import enums.ReadModes;
import gui.GuiManager;
import network.Response;
import util.LocalizationManager;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPane  {
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
    private JButton languageButton;

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
                        guiManager.closeVisualization();
                    }
                }
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = mainTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(guiManager.getMainFrame(), GuiManager.FONT_HTML_STRING + LocalizationManager.getString("removeErrorSelectFirst"));
                } else {
                    Long id = (Long) mainTable.getModel().getValueAt(row, 0);
                    String name = (String) mainTable.getModel().getValueAt(row, 1);

                    String confirmMessage = GuiManager.FONT_HTML_STRING + LocalizationManager.getString("removeConfirm").replace("$name$", name);
                    String confirmTitle = LocalizationManager.getString("removeById");

                    int result = JOptionPane.showConfirmDialog(guiManager.getMainFrame(), confirmMessage, confirmTitle, JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION) {

                        Response response = CommandInvoker.getInstance().runCommand(String.format("remove_by_id %s", id), ReadModes.APP);
                        switch (response.getStatus()) {
                            case OK -> {
                                String removeOk = GuiManager.FONT_HTML_STRING + LocalizationManager.getString("removeOk").replace("$name$", name);
                                JOptionPane.showMessageDialog(guiManager.getMainFrame(), removeOk);
                                updateTableData();
                            }
                            case CLIENT_ERROR -> JOptionPane.showMessageDialog(guiManager.getMainFrame(), GuiManager.FONT_HTML_STRING + response.getMessage());
                            case SERVER_ERROR -> JOptionPane.showMessageDialog(guiManager.getMainFrame(), GuiManager.FONT_HTML_STRING + LocalizationManager.getString("serverError"));
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
        visualizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiManager.openVisualization();
            }
        });
        languageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiManager.openLanguageDialog(guiManager.getMainFrame());
            }
        });
        if (languageButton != null) {
            languageButton.setOpaque(false);
            languageButton.setContentAreaFilled(false);
            languageButton.setBorderPainted(false);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
    public void updateTableData() {

        Response response = CommandInvoker.getInstance().runCommand("show table", ReadModes.APP);

        switch (response.getStatus()) {
            case OK -> {
                TableModel tableModel;
                try {
                    tableModel = (TableModel) response.getObject();
                } catch (ClassCastException e) {
                    mainHeader.setForeground(Color.RED);
                    mainHeader.setText(LocalizationManager.getString("serverError"));
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
                mainHeader.setText(LocalizationManager.getString("serverError"));
            }
        }
    }

    public void updateLanguage() {
        mainHeader.setText(LocalizationManager.getString("mainHeader"));
        logoutButton.setText(LocalizationManager.getString("logoutButton"));
        refreshButton.setText(LocalizationManager.getString("refresh"));
        addButton.setText(LocalizationManager.getString("addButton"));
        removeButton.setText(LocalizationManager.getString("removeButton"));
        otherButton.setText(LocalizationManager.getString("otherButton"));
        visualizeButton.setText(LocalizationManager.getString("visualizeButton"));
    }
    public void updateUserLabel(String login) {
        username.setText(login);
    }

}
