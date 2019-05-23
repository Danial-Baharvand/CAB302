package com.endgame.vectordesigntool;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class FileStorage {
    static void load (File filename){
    StringBuilder sb = new StringBuilder();
//           FileReader fr = new FileReader(filename);
    Path path = filename.toPath();

    try (BufferedReader br = Files.newBufferedReader(path)) {

        // read line by line
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }

    } catch (IOException e) {
        System.err.format("IOException: %s%n", e);
//        }catch (FileNotFoundException ex) {
//            System.err.format("IOException: %s%n", ex);
    }
    System.out.println(sb);
    }
}
