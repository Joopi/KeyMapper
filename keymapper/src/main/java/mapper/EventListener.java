package mapper;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import java.io.*;
import java.util.*;

public class EventListener implements NativeKeyListener, NativeMouseInputListener {

    private KeyRecorder recorder;
    private Map<Integer, Long> heldKeys;
    private Map<Integer, Long> heldMouseButtons;
    private int previousKey;
    private long previousTime;
    private File file;

    public EventListener(KeyRecorder recorder, File file) {
        this.recorder = recorder;
        this.file = file;
        heldKeys = new LinkedHashMap<Integer, Long>();
        heldMouseButtons = new HashMap<Integer, Long>();
        previousKey = -1;
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        //System.out.println("pressed: " + e.getKeyCode());

        if (e.getKeyCode() == NativeKeyEvent.VC_END) {
            try {
                JSONHandler.writeToJSON(recorder, file);
                GlobalScreen.unregisterNativeHook();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {

            //Ensure no values already exist so that the keyboard's key repeater doesn't reset any values.
            if (!heldKeys.containsKey(e.getKeyCode())) {
                heldKeys.put(e.getKeyCode(), e.getWhen());
                if (previousKey != -1) {
                    recorder.addKey(previousKey, e.getKeyCode(), e.getWhen() - previousTime);
                    previousKey = -1;
                }
            }
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        //System.out.println("rel: " + e.getKeyCode());
        Iterator<Integer> iterator = heldKeys.keySet().iterator();
        while (iterator.hasNext()) {
            Integer key = iterator.next();
            if (key == e.getKeyCode()) {
                long time = e.getWhen();

                while (iterator.hasNext()) {
                    Integer next = iterator.next();
                    recorder.addKey(e.getKeyCode(), next, (heldKeys.get(next)-time));
                }
            }
        }

        heldKeys.remove(e.getKeyCode());
        if (heldKeys.isEmpty()) {
            previousTime = e.getWhen();
            previousKey = e.getKeyCode();
        }
    }

    public void nativeMousePressed(NativeMouseEvent nativeEvent) {
        if (!heldMouseButtons.containsKey(nativeEvent.getButton())) {
            heldMouseButtons.put(nativeEvent.getButton(), nativeEvent.getWhen());
        }
    }

    public void nativeMouseReleased(NativeMouseEvent nativeEvent) {
        Long time = heldMouseButtons.get(nativeEvent.getButton());
        if (time != null) {
            recorder.addClick(nativeEvent.getButton(), nativeEvent.getWhen()-time);
            heldMouseButtons.remove(nativeEvent.getButton());
        }
    }

    public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    public void nativeMouseMoved(NativeMouseEvent nativeEvent) {
    }

    public void nativeMouseClicked(NativeMouseEvent nativeEvent) {
    }
}
