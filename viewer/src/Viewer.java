import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Viewer {

    private static final String MAC_IP = "172.30.1.91"; // 나중에 맥북 IP로 바꿈
    private static final int PORT = 5050;

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket(MAC_IP, PORT);
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            System.out.println("맥북에 연결됨! 답변을 기다리는 중...");

            while (true) {
                String type = in.readUTF(); // 메시지가 올 때까지 여기서 멈춰서 기다림
                if (type.equals("TEXT")) {
                    int length = in.readInt();
                    byte[] bytes = new byte[length];
                    in.readFully(bytes);    // 정확히 length 바이트를 다 받을 때까지 읽기
                    String text = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println("=== 새 답변 ===");
                    System.out.println(text);
                }
            }
        }
    }
}