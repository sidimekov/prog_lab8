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
import java.util.ArrayList;
import java.util.Map;

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
    private JButton languageButton;
    private JButton filterButton;
    private TableRowSorter<TableModel> rowSorter;

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
                            case CLIENT_ERROR ->
                                    JOptionPane.showMessageDialog(guiManager.getMainFrame(), GuiManager.FONT_HTML_STRING + response.getMessage());
                            case SERVER_ERROR ->
                                    JOptionPane.showMessageDialog(guiManager.getMainFrame(), GuiManager.FONT_HTML_STRING + LocalizationManager.getString("serverError"));
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
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiManager.openFilterDialog();
            }
        });
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

                rowSorter = new TableRowSorter<>(tableModel);
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

    public void applyFilter(Map<String, String> filterValues) {
        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>(12);
        try {
            if (!filterValues.get("id").isEmpty()) {
                filters.add(createNumberFilter(filterValues.get("id"), 0));
            }
            if (!filterValues.get("routeName").isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + filterValues.get("routeName"), 1));
            }
            if (!filterValues.get("coordinatesX").isEmpty()) {
                filters.add(createNumberFilter(filterValues.get("coordinatesX"), 3));
            }
            if (!filterValues.get("coordinatesY").isEmpty()) {
                filters.add(createNumberFilter(filterValues.get("coordinatesY"), 4));
            }
            if (!filterValues.get("fromX").isEmpty()) {
                filters.add(createNumberFilter(filterValues.get("fromX"), 5));
            }
            if (!filterValues.get("fromY").isEmpty()) {
                filters.add(createNumberFilter(filterValues.get("fromY"), 6));
            }
            if (!filterValues.get("fromZ").isEmpty()) {
                filters.add(createNumberFilter(filterValues.get("fromZ"), 7));
            }
            if (!filterValues.get("toName").isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + filterValues.get("toName"), 8));
            }
            if (!filterValues.get("toX").isEmpty()) {
                filters.add(createNumberFilter(filterValues.get("toX"), 9));
            }
            if (!filterValues.get("toY").isEmpty()) {
                filters.add(createNumberFilter(filterValues.get("toY"), 10));
            }
            if (!filterValues.get("toZ").isEmpty()) {
                filters.add(createNumberFilter(filterValues.get("toZ"), 11));
            }
            if (!filterValues.get("distance").isEmpty()) {
                filters.add(createNumberFilter(filterValues.get("distance"), 12));
            }
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        rowSorter.setRowFilter(RowFilter.andFilter(filters));

    }

    private RowFilter<Object, Object> createNumberFilter(String filterText, int columnIndex) {
        String operator = filterText.substring(0, 1);
        double value;
        try {
            value = Double.parseDouble(filterText.substring(1));
        } catch (NumberFormatException e) {
            return null;
        }

        switch (operator) {
            case "<":
                return RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, value, columnIndex);
            case ">":
                return RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, value, columnIndex);
            default:
                return RowFilter.regexFilter("(?i)" + filterText, columnIndex);
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
        filterButton.setText(LocalizationManager.getString("filter"));
    }

    public void updateUserLabel(String login) {
        username.setText(login);
    }

}
