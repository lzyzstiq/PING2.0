package com.ping.sleep;

import android.content.Context;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class DreamRecorder {

    private static final String DREAM_FILE = "/sdcard/ping_dreams.txt";
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    public static void saveDream(Context context, String dream) {
        String timestamp = DATE_FORMAT.format(new Date());
        String entry = "[" + timestamp + "] " + dream + "\n";

        try (FileOutputStream fos = new FileOutputStream(DREAM_FILE, true)) {
            fos.write(entry.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getRecentDreams(Context context, int limit) {
        List<String> dreams = new ArrayList<>();
        File file = new File(DREAM_FILE);

        if (!file.exists()) {
            return dreams;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            List<String> allLines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
            }
            int start = Math.max(0, allLines.size() - limit);
            for (int i = start; i < allLines.size(); i++) {
                dreams.add(allLines.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dreams;
    }
}
