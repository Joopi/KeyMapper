package mapper;

import java.util.HashMap;
import java.util.Map;

public class KeyRecorder {

    private int negative, positive, total, click;
    private Map<Integer, int[]> clickMap;
    private Map<Integer, Map<Integer, int[]>> characterMap;

    public KeyRecorder(int clickCutoff, int negativeEntries, int positiveEntries) {
        characterMap = new HashMap<Integer, Map<Integer, int[]>>();
        clickMap = new HashMap<Integer, int[]>();
        click = clickCutoff;
        negative = negativeEntries;
        positive = positiveEntries;
        total = negative + positive;
    }

    public void addKey(int from, int to, long deltaTime) {
        //split time into intervals of 16 milliseconds (clock frequency)
        //subtracting by half helps with minor discrepancies in frequency (15ms versus 16ms).
        deltaTime -= 8;
        deltaTime /= 16;
        //index by 0
        deltaTime += negative;

        if (deltaTime >= 0 && deltaTime < total) {
            Map<Integer, int[]> entry = characterMap.get(from);
            if (entry == null) {
                entry = new HashMap<Integer, int[]>();
                characterMap.put(from, entry);
            }

            int[] values = entry.get(to);
            if (values == null) {
                values = new int[total];
                entry.put(to, values);
            }

            values[(int) deltaTime] += 1;
        }
    }

    public void addClick(int clickType, long time) {
        time -= 8;
        time /= 16;

        if (time >= 0 && time < click) {
            int[] values = clickMap.get(clickType);
            if (values == null) {
                values = new int[click];
                clickMap.put(clickType, values);
            }

            values[(int)time] += 1;
        }
    }

    public boolean validate(int clickCutoff, int negativeEntries, int positiveEntries) {
        return (click == clickCutoff && negative == negativeEntries && positive == positiveEntries);
    }
}
