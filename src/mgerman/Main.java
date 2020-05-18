package mgerman;

import static java.lang.System.lineSeparator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final int LINE_SEPARATOR_LENGTH = lineSeparator().getBytes().length;

    public static void main(String[] args) throws IOException {
        findIntersections(args[0], args[1], args[2]);
    }

    private static void findIntersections(String firstSourceFileName, String secondSourceFileName,
                                          String resultFileName) throws IOException {
        Map<String, Long> idToCursor = createIndexFromFile(firstSourceFileName);
        try (FileReader reader = new FileReader(secondSourceFileName);
            BufferedReader br = new BufferedReader(reader);
            RandomAccessFile source1Reader = new RandomAccessFile(firstSourceFileName, "r");
            BufferedWriter writer = new BufferedWriter(new FileWriter(resultFileName, true))) {
            String line;
            long linesCounter = 0;
            while ((line = br.readLine()) != null) {
                linesCounter++;
                if (linesCounter % 2 != 0 && idToCursor.containsKey(line)) {
                    source1Reader.seek(idToCursor.get(line));
                    String dataFromSource1 = source1Reader.readLine();
                    String dataFromSource2 = br.readLine();
                    linesCounter++;
                    writer.append(line).append(lineSeparator()).append(dataFromSource1)
                        .append(lineSeparator()).append(dataFromSource2)
                        .append(lineSeparator());
                }
            }
        }
    }

    private static Map<String, Long> createIndexFromFile(String fileName) throws IOException {
        Map<String, Long> idToCursor = new HashMap<>();
        try (FileReader reader = new FileReader(fileName);
            BufferedReader br = new BufferedReader(reader)) {
            String line;
            long cursorPosition = 0;
            long linesCounter = 0;
            while ((line = br.readLine()) != null) {
                linesCounter++;
                cursorPosition += line.getBytes().length + LINE_SEPARATOR_LENGTH;
                if (linesCounter % 2 != 0) {
                    idToCursor.put(line, cursorPosition);
                }
            }
        }
        return idToCursor;
    }
}
