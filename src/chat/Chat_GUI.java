package chat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Chat_GUI extends JFrame {

    // Program window
    protected static JFrame frame;
    // Font used
    protected static final Font font = new Font("Ubuntu Mono", Font.PLAIN, 18);
    // Scroll Pane for Thread access
    protected static final JScrollPane scrollPane= new JScrollPane();
    // Messages Panel for Thread access
    protected static final JPanel messages = new JPanel();

    public Chat_GUI(String title) {

        super(title);
        frame = this;

        // Add input main panel for the frame
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        frame.setContentPane(mainPanel);

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();



        // Add scroll pane for messages
        // JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane);


        // Panel for putting messages as labels
        // messages = new JPanel();
        messages.setLayout(new BoxLayout(messages, BoxLayout.Y_AXIS));
        messages.setBorder(new EmptyBorder(10, 10, 30, 10));
        scrollPane.add(messages);
        scrollPane.setViewportView(messages);

        // Bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setMaximumSize(new Dimension(1080, 100));
        bottomPanel.setMinimumSize(new Dimension(640, 100));
        mainPanel.add(bottomPanel);

        // Text component for input
        JTextField input = new JTextField();
        input.setPreferredSize(new Dimension(540, 40));
        input.setFont(font);
        input.setMargin(new Insets(6, 6, 6, 6));
        bottomPanel.add(input);


        // Button for sending messages
        JButton send = new JButton();
        send.setFocusable(false);
        // Add icon to the button
        ImageIcon icon = new ImageIcon("C:\\Users\\Saba Dzlieri\\Downloads\\7253151_preview.png");
        Image img =  icon.getImage();
        Image newimg = img.getScaledInstance(30, 30,  Image.SCALE_SMOOTH) ;
        icon = new ImageIcon( newimg );
        send.setIcon(icon);
        send.setMaximumSize(new Dimension(40, 40));
        send.setFont(new Font("monospace", Font.PLAIN, 24));
        // Add key bind for Enter
        Action sendMsg = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send.doClick();
            }
        };
        InputMap iMap = bottomPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap aMap = bottomPanel.getActionMap();
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Send");
        aMap.put("Send", sendMsg);
        bottomPanel.add(send);

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = input.getText();
                if (!text.isBlank()) {
                    if (text.length() > 64) {
                        text = getNearestWord(text, 63);
                    }
                    JLabel newMessage = new JLabel("<html> > " + text + "<html/>");
                    newMessage.setFont(font);
                    Chat.out.println(" < " + text);
                    Chat.out.flush();
                    messages.add(newMessage);
                    input.setText("");
                    messages.revalidate();
                    messages.repaint();
                    JScrollBar sb = scrollPane.getVerticalScrollBar();
                    sb.setValue(sb.getMaximum());
                }
            }
        });

    }

    protected static String getNearestWord(String text, int size) {
        for (int i = size; i >= 0; i--)
            if (text.charAt(i) == ' ') {
                text = text.substring(0, i) + "<br/>" + text.substring(i);
                break;
            }
            else if (i == 0) {
                text = text.substring(0, size) + "<br/>" + text.substring(size);
                break;
            }
        return text;
    }

    public static void main(String[] args) {
        JFrame chat = new Chat_GUI("Java Chat");
        chat.setMinimumSize(new Dimension(640, 640));
        chat.setResizable(false);
        chat.setVisible(true);
    }

}
