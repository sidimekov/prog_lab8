package gui.forms;

import entity.Route;
import gui.GuiManager;
import network.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.PriorityQueue;

public class VisualizationForm extends JFrame {

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 720;

    private static final int PAINT_TIME = 1500;

    private Client client = Client.getInstance();

    private JPanel mainPanel;
    private JPanel errorMessagePanel;
    private JLabel errorLabel;
    private JScrollPane visualizationPanel;

    public VisualizationForm() {
//        setSize(WIDTH, HEIGHT);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void open() {
        Dimension dim = new Dimension(WIDTH, HEIGHT);
//        setPreferredSize(dim);
//        setSize(dim);
        setLocationRelativeTo(null);
        setResizable(false);

//        mainPanel.setBounds(0, 0, WIDTH, HEIGHT);
        mainPanel.setSize(new Dimension(WIDTH, HEIGHT));
        mainPanel.setBackground(new Color(190, 190, 190));

        PriorityQueue<Route> collection = client.getCollection();
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        if (collection == null) {
            errorMessagePanel.setSize(new Dimension(WIDTH - 100, HEIGHT));
            cl.show(mainPanel, "error");
        } else {
            cl.show(mainPanel, "visual");
            repaint();
        }

        setContentPane(mainPanel);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Route route : client.getCollection()) {

//        Color color = GuiManager.getInstance().randomColor();
            System.out.println(route);
            Color color = new Color(route.getUserHash());
            g2d.setColor(color);

            int routeSize = (int) Math.min(10 + (route.getDistance() / 100), 40);
            int fromX = Math.min(WIDTH - routeSize*2, route.getFrom().getX());
//            int fromY = HEIGHT - Math.min(HEIGHT - routeSize*2, route.getFrom().getY());
            int fromY = (int) ((HEIGHT - routeSize*2) * ((double) route.getFrom().getY() / 1000));
            int toX = (int) Math.min(WIDTH - routeSize*2, route.getTo().getX());
//            int toY = HEIGHT - Math.min(HEIGHT - routeSize*2, route.getTo().getY());
            int toY = (int) ((HEIGHT - routeSize*2) * ((double) route.getTo().getY() / 1000));
            System.out.println(fromY + " " + toY);

            g2d.setStroke(new BasicStroke(Math.round(routeSize * 0.8)));
            g2d.drawLine(fromX, fromY, toX, toY);
            g2d.setStroke(new BasicStroke(Math.round(routeSize * 0.4)));
            g2d.setColor(color.darker());
            g2d.drawLine(fromX, fromY, toX, toY);
            g2d.setColor(color);
            g2d.fillOval(fromX - routeSize, fromY - routeSize, routeSize * 2, routeSize * 2);
            g2d.fillOval(toX - routeSize, toY - routeSize, routeSize * 2, routeSize * 2);
            g2d.setColor(color.darker());
            g2d.fillOval((int) (fromX - Math.round(routeSize * 0.75)), (int) (fromY - Math.round(routeSize * 0.75)), (int) (1.5 * routeSize), (int) (1.5 * routeSize));
            g2d.fillOval((int) (toX - Math.round(routeSize * 0.75)), (int) (toY - Math.round(routeSize * 0.75)), (int) (1.5 * routeSize), (int) (1.5 * routeSize));


        }
    }
}
