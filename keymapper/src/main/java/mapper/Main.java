package mapper;

import org.jnativehook.GlobalScreen;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final int clickCutoff = 25;
    private static final int negativeEntries = 10;
    private static final int positiveEntries = 50;
    private static final String jsonFilename = "./output.json";

    public static void main(String[] args) throws Exception {

        File file = new File(jsonFilename);
        KeyRecorder recorder;

        if (file.exists() && !file.isDirectory()) {
            recorder = JSONHandler.parseFromJSON(file);

            if (!recorder.validate(clickCutoff, negativeEntries, positiveEntries)) {
                System.out.println("JSON-Configuration mismatch\nCorrect configuration in main() or enter new JSON filename");
                System.exit(1);
            }
        } else {
            recorder = new KeyRecorder(clickCutoff, negativeEntries, positiveEntries);
        }

        GlobalScreen.registerNativeHook();
        EventListener listener = new EventListener(recorder, file);
        GlobalScreen.addNativeKeyListener(listener);
        GlobalScreen.addNativeMouseListener(listener);

        //move logging code up to suppress extra info.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);
    }
}
