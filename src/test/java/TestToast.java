import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

public class TestToast {
    public static void main(String[] args) {

        JFrame mainFrame = new JFrame();

        ToastMessage message = new ToastMessage(mainFrame, "Test toast message");
        message.display();
    }

    public static class ToastMessage extends JFrame {

        public ToastMessage(JFrame mainFrame, final String message) {
            setUndecorated(true);
            setLayout(new GridBagLayout());
            setBackground(new Color(240, 240, 240, 250));

            setLocation(mainFrame.getX() + mainFrame.getWidth() - getWidth(), mainFrame.getY());

            setSize(300, 50);
            add(new JLabel(message));

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                }
            });
        }

        public void display() {
            try {
                setOpacity(1);
                setVisible(true);
                Thread.sleep(2000);

                for (double d = 1.0; d > 0.2; d -= 0.1) {
                    Thread.sleep(100);
                    setOpacity((float) d);
                }

                setVisible(false);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
