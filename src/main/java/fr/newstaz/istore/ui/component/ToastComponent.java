package fr.newstaz.istore.ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

public class ToastComponent {

    private static ToastMessage toastMessage;

    public enum ToastPosition {
        TOP_RIGHT,
    }

    public static void showSuccessToast(JPanel mainFrame, String message) {
        showToast(mainFrame, message, new Color(0, 200, 0), ToastPosition.TOP_RIGHT, 500);
    }

    public static void showFailedToast(JPanel mainFrame, String message) {
        showToast(mainFrame, message, new Color(200, 0, 0), ToastPosition.TOP_RIGHT, 500);
    }

    public static void showToast(JPanel parentPanel, String message, Color backgroundColor,
                                 ToastPosition position, int duration) {
        if (toastMessage != null && toastMessage.isVisible()) {
            toastMessage.updateMessage(message);
        } else {
            toastMessage = new ToastMessage(parentPanel, message, backgroundColor, position);
            SwingUtilities.invokeLater(() -> {
                toastMessage.display(duration);
            });
        }
    }

    public static class ToastMessage extends JWindow {

        private final JLabel messageLabel;

        public ToastMessage(JPanel parentPanel, final String message, Color backgroundColor,
                            ToastPosition position) {
            setLayout(new GridBagLayout());
            getContentPane().setBackground(backgroundColor);
            setSize(300, 50);

            Point parentLocation = parentPanel.getLocationOnScreen();
            setLocation(calculatePosition(parentLocation, parentPanel.getWidth(), position));

            messageLabel = new JLabel(message);
            messageLabel.setForeground(Color.WHITE);
            add(messageLabel);

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                }
            });
        }

        public void updateMessage(String newMessage) {
            messageLabel.setText(newMessage);
            setOpacity(1);
        }

        private volatile boolean shouldCancel = false;

        public void display(int duration) {
            SwingWorker<Void, Float> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    try {
                        setOpacity(1);
                        setVisible(true);

                        for (float opacity = 1.0f; opacity > 0.2f; opacity -= 0.1f) {
                            if (shouldCancel) {
                                break;
                            }

                            Thread.sleep(100);
                            publish(opacity);
                        }

                        setVisible(false);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void process(java.util.List<Float> chunks) {
                    Float opacity = chunks.get(0);
                    setOpacity(opacity);
                }
            };

            worker.execute();

            Timer timer = new Timer(duration, (e) -> {
                shouldCancel = true;
            });
            timer.setRepeats(false);
            timer.start();
        }

        private Point calculatePosition(Point parentLocation, int parentWidth, ToastPosition position) {
            int x = 0;
            int y = parentLocation.y;

            switch (position) {
                case TOP_RIGHT:
                    x = parentLocation.x + parentWidth - getWidth() - 10;
                    y += 10;
                    break;
            }

            return new Point(x, y);
        }
    }
}
