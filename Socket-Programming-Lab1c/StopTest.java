import java.net.*;

public class StopTest {
    static volatile boolean running;

    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(9998);

        Thread t = new Thread(() -> {
            running = true;
            while (running) {
                try {
                    ss.accept();   // blocked here waiting for client
                } catch (Exception e) {
                    System.out.println("accept() exited with: " + e);
                }
            }
            System.out.println("Thread has stopped.");
        });
        t.start();
        Thread.sleep(1000);

        // Approach 1 (as in the lab prompt): running = false + interrupt()
        System.out.println("Testing running = false and t.interrupt()...");
        running = false;
        t.interrupt();
        Thread.sleep(2000);
        System.out.println("After interrupt, is thread alive? " + t.isAlive());

        // Approach 2 (correct approach): close ServerSocket
        System.out.println("Testing serverSocket.close()...");
        ss.close();
        t.join();
        System.out.println("After close, is thread alive? " + t.isAlive());
    }
}