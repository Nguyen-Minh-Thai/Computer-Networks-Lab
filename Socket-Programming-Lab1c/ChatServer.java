import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer extends JFrame {
    private final JTextArea log = new JTextArea();
    private final JTextField portField = new JTextField("9999", 6);
    private final JButton btn = new JButton("Start");

    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private volatile boolean running = false;
    private ServerSocket serverSocket;

    public ChatServer() {
        super("Chat Server");
        log.setEditable(false);
        JPanel top = new JPanel();
        top.add(new JLabel("Port:"));
        top.add(portField);
        top.add(btn);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(log), BorderLayout.CENTER);
        btn.addActionListener(e -> { if (running) stopServer(); else startServer(); });
        setSize(450, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void log(String s) {
        SwingUtilities.invokeLater(() -> log.append(s + "\n"));
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(Integer.parseInt(portField.getText().trim()));
        } catch (Exception ex) {
            log("Cannot start: " + ex.getMessage());
            return;
        }
        running = true;
        btn.setText("Stop");
        log("Server started on port " + serverSocket.getLocalPort());

        Thread listener = new Thread(() -> {
            while (running) {
                try {
                    Socket s = serverSocket.accept();
                    ClientHandler h = new ClientHandler(s);
                    clients.add(h);
                    new Thread(h, "client-handler").start();
                } catch (IOException ex) {
                    if (running) log("Accept error: " + ex.getMessage());
                }
            }
            log("Listener thread ended.");
        }, "listener");
        listener.start();
    }

    private void stopServer() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException ignored) {}
        for (ClientHandler h : clients) h.close();
        clients.clear();
        btn.setText("Start");
        log("Server stopped.");
    }

    private void broadcast(String msg) {
        for (ClientHandler h : clients) h.send(msg);
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;
        private final BufferedReader in;
        private final PrintWriter out;
        private String name = "?";

        ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        }

        void send(String msg) { out.println(msg); }

        void close() { try { socket.close(); } catch (IOException ignored) {} }

        @Override
        public void run() {
            try {
                name = in.readLine();
                if (name == null) return;
                log(name + " joined from " + socket.getRemoteSocketAddress());
                broadcast("*** " + name + " joined ***");
                String line;
                while ((line = in.readLine()) != null) {
                    log(name + ": " + line);
                    broadcast(name + ": " + line);
                }
            } catch (IOException ignored) {
            } finally {
                clients.remove(this);
                close();
                log(name + " left.");
                broadcast("*** " + name + " left ***");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatServer().setVisible(true));
    }
}