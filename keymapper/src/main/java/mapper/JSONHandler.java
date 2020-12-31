package mapper;

import com.google.gson.Gson;
import java.io.*;

public class JSONHandler {

    public static KeyRecorder parseFromJSON(File jsonFile) throws Exception {
        Reader reader = new FileReader(jsonFile);
        Gson gson = new Gson();
        KeyRecorder recorder = gson.fromJson(reader, KeyRecorder.class);
        reader.close();

        return recorder;
    }

    public static void writeToJSON(KeyRecorder recorder, File jsonFile) throws IOException {
        Writer writer = new FileWriter(jsonFile);
        Gson gson = new Gson();
        gson.toJson(recorder, writer);
        writer.flush();
        writer.close();
    }
}
