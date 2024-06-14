package gui.forms;

import entity.Route;
import network.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class VisualizationForm extends JFrame {

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 720;

    private Client client = Client.getInstance();

    private JPanel mainPanel;
    private JPanel errorMessagePanel;
    private JLabel errorLabel;
    private JScrollPane visualizationScroll;
    private JPanel visualizationPanel;

    public VisualizationForm() {
//        setSize(WIDTH, HEIGHT);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        pack();
        setLocationRelativeTo(null);
        setResizable(true);
    }

    public void open() {
        Dimension dim = new Dimension(WIDTH, HEIGHT);
//        setPreferredSize(dim);
//        setSize(dim);
        setLocationRelativeTo(null);
        setResizable(true);

//        mainPanel.setBounds(0, 0, WIDTH, HEIGHT);
        mainPanel.setSize(new Dimension(WIDTH, HEIGHT));

        PriorityQueue<Route> collection = client.getCollection();
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        if (collection == null) {
            errorMessagePanel.setSize(new Dimension(WIDTH - 100, HEIGHT));
            cl.show(mainPanel, "error");

            setContentPane(mainPanel);
            pack();
            setVisible(true);
        } else {
            cl.show(mainPanel, "visual");
            repaint();

            setContentPane(mainPanel);
            pack();
            ((VisualizationPanel) visualizationPanel).animate();
            setVisible(true);
        }
    }

    public class VisualizationPanel extends JPanel {

        private int HEIGHT = VisualizationForm.HEIGHT;
        private int WIDTH = VisualizationForm.WIDTH;
        private PriorityQueue<Route> routes;
        private Timer t;
        private double delta;
        private Animator animator = new Animator();

        private VisualizationPanel() {
            super(new GridLayout());
//            setBackground(new Color(190, 190, 190));
            setBounds(0, 0, VisualizationForm.WIDTH, VisualizationForm.HEIGHT);
            t = new Timer(5, animator);
            t.setCoalesce(false);
        }

        private void animate() {
            routes = client.getCollection();

            delta = 0;
            t.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            for (Route route : routes) {

                Color color = new Color(route.getUserHash());

                int routeSize = (int) Math.min(10 + (route.getDistance() / 100), 40);
                int fromX = Math.min(WIDTH - routeSize * 2, route.getFrom().getX());
                int fromY = (int) ((HEIGHT - routeSize * 2) * ((double) route.getFrom().getY() / 1000));
                int toX = (int) Math.min(WIDTH - routeSize * 2, route.getTo().getX());
                int toY = (int) ((HEIGHT - routeSize * 2) * ((double) route.getTo().getY() / 1000));
//
                toX = (int) (fromX + Math.abs(toX-fromX) * ((double) delta / 50));
                toY = (int) (fromY + Math.abs(toY-fromY) * ((double) delta / 50));

                drawRoute(g2d, fromX, fromY, toX, toY, routeSize, color);

            }
        }

        private void drawRoute(Graphics2D g2d, int x1, int y1, int x2, int y2, int size, Color color) {

            g2d.setColor(color);


            g2d.setStroke(new BasicStroke(Math.round(size * 0.8)));
            g2d.drawLine(x1, y1, x2, y2);
            g2d.setStroke(new BasicStroke(Math.round(size * 0.4)));
            g2d.setColor(color.darker());
            g2d.drawLine(x1, y1, x2, y2);
            g2d.setColor(color);
            g2d.fillOval(x1 - size, y1 - size, size * 2, size * 2);
            g2d.fillOval(x2 - size, y2 - size, size * 2, size * 2);
            g2d.setColor(color.darker());
            g2d.fillOval((int) (x1 - Math.round(size * 0.75)), (int) (y1 - Math.round(size * 0.75)), (int) (1.5 * size), (int) (1.5 * size));
            g2d.fillOval((int) (x2 - Math.round(size * 0.75)), (int) (y2 - Math.round(size * 0.75)), (int) (1.5 * size), (int) (1.5 * size));

        }

        private class Animator implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (delta < 50) {
                    delta++;
                    visualizationPanel.repaint();
                } else {
                    t.stop();
                }
            }
        }
    }

    private void createUIComponents() {
        visualizationPanel = new VisualizationPanel();
    }
}
