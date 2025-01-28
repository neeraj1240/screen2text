package org.example.ocr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class TransparentOverlay extends JWindow {
    private Point startPoint;
    private Rectangle selection;
    private final JPanel overlayPanel;

    public TransparentOverlay() {

        setAlwaysOnTop(true);
        setBackground(new Color(0, 0, 0, 1)); // Nearly transparent
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));


        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Rectangle bounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        setBounds(bounds);


        setLocation(0, 0);
        setSize(bounds.width, bounds.height);

        overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (selection != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(new Color(0, 120, 215, 80));
                    g2d.fillRect(selection.x, selection.y, selection.width, selection.height);
                    g2d.setColor(Color.BLUE);
                    g2d.drawRect(selection.x, selection.y, selection.width, selection.height);
                }
            }
        };

        overlayPanel.setOpaque(false);
        overlayPanel.setLayout(null);
        overlayPanel.setPreferredSize(bounds.getSize());

        MouseAdapter adapter = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                selection = new Rectangle();
            }

            public void mouseDragged(MouseEvent e) {
                if (startPoint != null) {
                    int x = Math.min(startPoint.x, e.getX());
                    int y = Math.min(startPoint.y, e.getY());
                    int width = Math.abs(e.getX() - startPoint.x);
                    int height = Math.abs(e.getY() - startPoint.y);

                    selection.setBounds(x, y, width, height);
                    overlayPanel.repaint();
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (selection != null && selection.width > 10 && selection.height > 10) {
                    captureAndProcess();
                }
                dispose();
            }
        };

        overlayPanel.addMouseListener(adapter);
        overlayPanel.addMouseMotionListener(adapter);
        add(overlayPanel);
    }

    private void captureAndProcess() {
        try {
            Point screenLocation = getLocationOnScreen();
            Rectangle captureArea = new Rectangle(
                    screenLocation.x + selection.x,
                    screenLocation.y + selection.y,
                    selection.width,
                    selection.height
            );

            BufferedImage capture = new Robot().createScreenCapture(captureArea);
            String result = OCRProcessor.extractText(capture);

            SwingUtilities.invokeLater(() -> {
                JTextArea textArea = new JTextArea(result);
                JOptionPane.showMessageDialog(null, new JScrollPane(textArea),
                        "OCR Results", JOptionPane.PLAIN_MESSAGE);
            });
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    public static void startSelection() {
        SwingUtilities.invokeLater(() -> {
            TransparentOverlay overlay = new TransparentOverlay();


            overlay.setFocusableWindowState(false);
            overlay.setFocusable(false);
            overlay.setAutoRequestFocus(false);

            overlay.setVisible(true);


            overlay.toFront();
            overlay.repaint();
        });
    }
}