import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

/**
 * Chat client.
 *  - Main window thread: form chính, nút "New connection" mở 1 cửa sổ chat mới
 *  - Chat window: kết nối socket tới server, tạo luồng đọc InputStream
 *  - Grand-child thread: đọc InputStream của socket và hiển thị tin nhắn
 */
public class ChatClient extends JFrame {
    private final JTextField hostField = new JTextField("localhost", 10);
    private final JTextField portField = new JTextField("9999", 5);
    private final JTextField nameField = new JTextField("user", 8);

    public ChatClient() {
        super("Chat Client - Main");
        setLayout(new FlowLayout());
        add(new JLabel("Host:")); add(hostField);
        add(new JLabel("Port:")); add(portField);
        add(new JLabel("Name:")); add(nameField);
        JButton btn = new JButton("New connection");
        add(btn);
        btn.addActionListener(e -> new ChatWindow(
                hostField.getText().trim(),
                Integer.parseInt(portField.getText().trim()),
                nameField.getText().trim()).setVisible(true));
        setSize(650, 90);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /** Mỗi kết nối = 1 cửa sổ chat + 1 socket + 1 luồng đọc */
    static class ChatWindow extends JFrame {
        private final JTextArea area = new JTextArea();
        private final JTextField input = new JTextField();
        private Socket socket;
        private PrintWriter out;

        ChatWindow(String host, int port, String name) {
            super("Chat - " + name);
            area.setEditable(false);
            add(new JScrollPane(area), BorderLayout.CENTER);
            add(input, BorderLayout.SOUTH);
            setSize(400, 300);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            input.addActionListener(e -> {
                if (out != null && !input.getText().isEmpty()) {
                    out.println(input.getText());
                    input.setText("");
                }
            });
            addWindowListener(new java.awt.event.WindowAdapter() {
                @Override public void windowClosed(java.awt.event.WindowEvent e) {
                    try { if (socket != null) socket.close(); } catch (IOException ignored) {}
                }
            });

            try {
                socket = new Socket(host, port);
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
                out.println(name);  // gửi tên đầu tiên
            } catch (IOException ex) {
                append("Cannot connect: " + ex.getMessage());
                return;
            }

            // Grand-child thread: xử lý InputStream của socket
            Thread reader = new Thread(() -> {
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), "UTF-8"))) {
                    String line;
                    while ((line = in.readLine()) != null) append(line);
                } catch (IOException ignored) {
                }
                append("--- disconnected ---");
            }, "reader-" + name);
            reader.start();
        }

        private void append(String s) {
            SwingUtilities.invokeLater(() -> area.append(s + "\n"));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatClient().setVisible(true));
    }
}