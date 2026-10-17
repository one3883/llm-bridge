import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Sender {

    private static final int PORT = 5050;

    public static void main(String[] args) throws IOException, InterruptedException {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("포트 " + PORT + "에서 Z13 접속 대기 중...");

            while (true) {
                // accept()는 누군가 접속할 때까지 여기서 멈춰 있음
                try (Socket socket = server.accept();
                     DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                    System.out.println("Z13 연결됨: " + socket.getInetAddress());
                    String lastText = readText(clipboard);

                    while (true) {
                        String currentText = readText(clipboard);
                        if (currentText != null && !currentText.equals(lastText)) {
                            lastText = currentText;
                            sendText(out, currentText);
                            System.out.println("보냄 (" + currentText.length() + "자)");
                        }
                        Thread.sleep(500);
                    }
                } catch (IOException e) {
                    System.out.println("연결 끊김. 다시 대기합니다.");
                }
            }
        }
    }

    private static void sendText(DataOutputStream out, String text) throws IOException {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        out.writeUTF("TEXT");        // 1. 종류
        out.writeInt(bytes.length);  // 2. 길이
        out.write(bytes);            // 3. 내용
        out.flush();                 // 모아둔 데이터를 지금 바로 전송
    }

    private static String readText(Clipboard clipboard) {
        try {
            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                return (String) clipboard.getData(DataFlavor.stringFlavor);
            }
        } catch (Exception e) {
            // 잠깐 실패하면 다음에 다시 시도
        }
        return null;
    }
}

//상징적인 llm-bridge 첫 코드
//import java.awt.Toolkit;
//import java.awt.datatransfer.Clipboard;
//import java.awt.datatransfer.DataFlavor;
//
//public class ClipboardWatcher {
//    public static void main(String[] args) throws InterruptedException {
//        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//
//        String lastText = readText(clipboard);
//
//        System.out.println("클립보드 감시 시작");
//
//        while(true) {
//            String currentText = readText(clipboard);
//
//            if (currentText != null && !currentText.equals(lastText)) {
//                lastText = currentText;
//                System.out.println("=== 새 내용 (" + currentText.length() + "자) ===");
//                System.out.println(currentText);
//            }
//            Thread.sleep(500);
//
//            }
//        }
//
//        private static String readText(Clipboard clipboard) {
//            try {
//                if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
//                    return (String) clipboard.getData(DataFlavor.stringFlavor);
//                }
//            }         catch (Exception e){
//
//                }
//                return null;
//
//            }
//        }
