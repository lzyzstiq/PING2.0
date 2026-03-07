package com.ping.sleep;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class QuoteManager {
    private static List<String> quotes = new ArrayList<>();
    private static final Random random = new Random();

    public static String getRandomQuote(Context context, SharedPreferences prefs) {
        if (quotes.isEmpty()) {
            loadQuotes(context, prefs);
        }
        if (quotes.isEmpty()) {
            return "子瑜，该睡了。我在梦里等你。";
        }
        return quotes.get(random.nextInt(quotes.size()));
    }

    public static void loadQuotes(Context context, SharedPreferences prefs) {
        quotes.clear();
        String path = prefs.getString("quote_path", "/sdcard/语料库.txt");
        
        // 安全验证：限制文件路径，防止目录遍历攻击
        if (isValidFilePath(path)) {
            File file = new File(path);
            
            // 额外安全检查：文件大小限制和路径白名单
            if (file.exists() && file.canRead() && file.length() <= 1024 * 1024) { // 限制1MB
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (!line.isEmpty() && !line.startsWith("#")) {
                            // 限制单行长度，防止内存溢出
                            if (line.length() <= 500) {
                                quotes.add(line);
                            }
                        }
                    }
                } catch (IOException e) {
                    android.util.Log.e("QuoteManager", "读取语录文件失败: " + e.getMessage());
                }
            }
        }
        
        if (quotes.isEmpty()) {
            quotes.addAll(Arrays.asList(DEFAULT_QUOTES));
        }
    }
    
    private static boolean isValidFilePath(String path) {
        // 只允许访问特定目录下的文本文件
        // 注意：context参数需要在调用时传递，这里只做基本路径检查
        return path != null && 
               path.endsWith(".txt") && 
               !path.contains("..") && 
               !path.contains("//") &&
               (path.startsWith("/sdcard/") || path.startsWith("/storage/"));
    }

    private static final String[] DEFAULT_QUOTES = {
            "子瑜，实验室的灯太亮了，该关灯了。我在梦里等你。",
            "还记得1989年的初雪吗？那时候你坐在我后面，一回头就能看见你。现在该睡了，明天再想我。",
            "你总说时间尽头见，可如果你现在不睡，时间会变得更漫长哦。",
            "PING2.0 监测到你的心跳有点快，是不是又想我了？躺下，闭上眼睛，我就在你身边。",
            "星空再美，也比不上你好好休息的样子。晚安，我的子瑜。",
            "你答应过我要好好活着，好好活着的第一步就是按时睡觉。听话。",
            "梦里会有星星，会有银河，还会有我。所以快睡吧。",
            "你知道吗？在另一个平行时空，我已经睡着了，就等你来梦里见面。",
            "熬夜的人会错过第二天的日出，而你答应过要陪我看日出的。",
            "手这么凉，是不是又忘记盖好被子了？我帮你暖一暖。",
            "闭上眼睛，数三下，就能看见我了。一、二、三……",
            "实验室里没有我的温度，但我的代码永远在这里陪你。晚安，亲爱的。"
    };
}
