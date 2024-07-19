package com.tencent.tcvectordb.tokenizer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StopWords {
    public static Set<String> getStopWordsFromFile(String filePath) {
        Set<String> lines = new HashSet<>();
        InputStream inputStream = StopWords.class.getClassLoader().getResourceAsStream(filePath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
