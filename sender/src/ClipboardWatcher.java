import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

public class ClipboardWatcher {
    public static void main(String[] args) throws InterruptedException {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        String lastText = readText(clipboard);

        System.out.println("클립보드 감시 시작");

        while(true) {
            String currentText = readText(clipboard);

            if (currentText != null && !currentText.equals(lastText)) {
                lastText = currentText;
                System.out.println("=== 새 내용 (" + currentText.length() + "자) ===");
                System.out.println(currentText);
            }
            Thread.sleep(500);

            }
        }

        private static String readText(Clipboard clipboard) {
            try {
                if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                    return (String) clipboard.getData(DataFlavor.stringFlavor);
                }
            }         catch (Exception e){

                }
                return null;

            }
        }
