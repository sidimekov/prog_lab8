package gui.forms;

import entity.Route;
import gui.GuiManager;
import network.Client;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
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
    private JPanel visualizationPanel;
    private JPanel fullVisualPanel;
    private JSlider zoomSlider;
    private JLabel visualuzationLabel;
    private JPanel visualHeaderPanel;

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
        private HashMap<Long, Line2D> interactLines = new HashMap<>();
        private final Timer t;
        private double delta;
        private int offsetX = 0, offsetY = 0;
        private int dragStartX, dragStartY;
        private static final double SCALE_FACTOR = 1.1;
        private static final double MAX_SCALE = 2.0;
        private static final double MIN_SCALE = 0.5;
        private double scale = 1.0;
        private Point focusPoint = new Point();
        private Line2D hoveredLine, clickedLine;
        private JPopupMenu popupMenu;

        private JLabel popupLabel;

        private VisualizationPanel() {
            super(null);
//            setBackground(new Color(190, 190, 190));
            setBounds(0, 0, VisualizationForm.WIDTH, VisualizationForm.HEIGHT);

            zoomSlider = new JSlider(0, 100, 50);

            t = new Timer(5, new Animator());
            t.setCoalesce(false);
            addListeners();
            initPopupMenu();
        }

        private void initPopupMenu() {
            popupMenu = new JPopupMenu();
            popupLabel = new JLabel();
            popupMenu.add(popupLabel);
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

            g2d.translate(offsetX, offsetY);
            g2d.scale(scale, scale);
            g2d.setFont(GuiManager.getInstance().getDefaultFont());

            for (Route route : routes) {

                Color color = new Color(route.getUserHash());
                if (interactLines.get(route.getId()) == hoveredLine) {
                    color = color.brighter();
                }

                int routeSize = (int) Math.min(10 + (route.getDistance() / 100), 40);
                int fromX = route.getFrom().getX();
                int fromY = (int) ((-1.0) * ((double) route.getFrom().getY()));
                int toX = (int) route.getTo().getX();
                int toY = (int) ((-1.0) * (double) route.getTo().getY());

                int transformedFromX = (int) (fromX * scale + offsetX);
                int transformedFromY = (int) (fromY * scale + offsetY);
                int transformedToX = (int) (toX * scale + offsetX);
                int transformedToY = (int) (toY * scale + offsetY);

                interactLines.put(route.getId(), new Line2D.Double(transformedFromX, transformedFromY, transformedToX, transformedToY));

                toX = (int) (fromX + (toX - fromX) * (delta / 50));
                toY = (int) (fromY + (toY - fromY) * (delta / 50));


                drawRoute(g2d, fromX, fromY, toX, toY, routeSize, color);
                g2d.setColor(Color.BLACK);
                g2d.drawString(String.valueOf(route.getName()), fromX + routeSize, fromY);

            }
        }

        private void drawRoute(Graphics2D g2d, int x1, int y1, int x2, int y2, int size, Color color) {
            g2d.setColor(color);

            float strokeWidth = Math.round(size * 0.8f);
            g2d.setStroke(new BasicStroke(strokeWidth));
            g2d.drawLine(x1, y1, x2, y2);

            g2d.setStroke(new BasicStroke(Math.round(size * 0.4f)));
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

        private void addListeners() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    dragStartX = e.getX() - offsetX;
                    dragStartY = e.getY() - offsetY;
                    clickedLine = getLineUnderMouse(e.getPoint());

                    if (clickedLine != null) {
                        Route route = null;
                        for (Map.Entry<Long, Line2D> entry : interactLines.entrySet()) {
                            if (entry.getValue() == clickedLine) {
                                long id = entry.getKey();
                                for (Route curRoute : routes) {
                                    if (curRoute.getId() == id) {
                                        route = curRoute;
                                    }
                                }
                            }
                        }
                        if (route != null) {
                            popupLabel.setText(GuiManager.FONT_HTML_STRING + GuiManager.getInstance().getResourceBundle().getString("routeInfo").replace("$route$", route.toString()).replace("\n", "<br>"));
                            popupMenu.show(VisualizationPanel.this, e.getX(), e.getY());
                        } else {
                            popupLabel.setText(GuiManager.FONT_HTML_STRING + GuiManager.getInstance().getResourceBundle().getString("noRouteInfo"));
                            popupMenu.show(VisualizationPanel.this, e.getX(), e.getY());
                        }
                    } else {
                        popupMenu.setVisible(false);
                    }

                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    clickedLine = null;
                    repaint();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    offsetX = e.getX() - dragStartX;
                    offsetY = e.getY() - dragStartY;
                    focusPoint = e.getPoint();
                    repaint();
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    Line2D prevLine = hoveredLine;
                    hoveredLine = getLineUnderMouse(e.getPoint());
                    if (prevLine != hoveredLine) {
                        repaint();
                    }
                }
            });

            addMouseWheelListener(new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    int notches = e.getWheelRotation();
                    double prevScale = scale;
                    if (notches < 0) {
                        scale *= SCALE_FACTOR;
                    } else {
                        scale /= SCALE_FACTOR;
                    }
                    if (scale > MAX_SCALE) {
                        scale = MAX_SCALE;
                    } else if (scale < MIN_SCALE) {
                        scale = MIN_SCALE;
                    }
                    focusPoint.setLocation(
                            (int) (focusPoint.x * (scale / prevScale)),
                            (int) (focusPoint.y * (scale / prevScale))
                    );
                    zoomSlider.setValue((int) (((scale - 0.5) / 1.5) * 100));
                    visualizationPanel.repaint();
                }
            });

            zoomSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider src = (JSlider) e.getSource();
                    int value = src.getValue();
                    scale = 0.5 + 1.5 * (double) value / 100;
                    repaint();
                }
            });
        }


        private Line2D getLineUnderMouse(Point point) {
            double tolerance = 10.0;
            for (Map.Entry<Long, Line2D> entry : interactLines.entrySet()) {
                Line2D line = entry.getValue();
                double lineWidth = 10;
                if (line.ptSegDist(point) <= tolerance + lineWidth / 2.0) {
                    return line;
                }
            }
            return null;
        }
    }

    private void createUIComponents() {
        visualizationPanel = new VisualizationPanel();
    }
}
