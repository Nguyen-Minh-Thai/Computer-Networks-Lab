import java.io.*;
import java.net.*;

public class DownloadHomepage {
    public static void main(String[] args) throws IOException {
        // Mặc định tải www.google.com, có thể truyền tên web khác: java DownloadHomepage example.com
        String host = args.length > 0 ? args[0] : "www.google.com";

        // 1. Tạo socket kết nối tới web server, port 80 (HTTP)
        try (Socket socket = new Socket(host, 80)) {
            System.out.println("Connected to " + host + " (" + socket.getInetAddress() + ")");

            // 2. Gửi yêu cầu HTTP lấy trang chủ
            OutputStream out = socket.getOutputStream();
            String request = "GET / HTTP/1.0\r\n"
                           + "Host: " + host + "\r\n"
                           + "User-Agent: Mozilla/5.0\r\n"
                           + "\r\n";
            out.write(request.getBytes("ISO-8859-1"));
            out.flush();

            // 3. Đọc toàn bộ phản hồi cho tới khi server đóng kết nối
            InputStream in = socket.getInputStream();
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            byte[] chunk = new byte[4096];
            int n;
            while ((n = in.read(chunk)) != -1) {
                buf.write(chunk, 0, n);
            }
            byte[] data = buf.toByteArray();

            // 4. Tách header và nội dung (ngăn cách bởi dòng trống \r\n\r\n)
            int bodyStart = -1;
            for (int i = 0; i + 3 < data.length; i++) {
                if (data[i] == '\r' && data[i + 1] == '\n'
                        && data[i + 2] == '\r' && data[i + 3] == '\n') {
                    bodyStart = i + 4;
                    break;
                }
            }
            int headerLen = (bodyStart < 0) ? data.length : bodyStart;
            System.out.println("----- HTTP response header -----");
            System.out.println(new String(data, 0, headerLen, "ISO-8859-1"));

            // 5. Lưu nội dung trang chủ ra file
            if (bodyStart >= 0) {
                try (FileOutputStream file = new FileOutputStream("homepage.html")) {
                    file.write(data, bodyStart, data.length - bodyStart);
                }
                System.out.println("Saved " + (data.length - bodyStart) + " bytes to homepage.html");
            }
        } // 6. Socket tự đóng ở đây
    }
}