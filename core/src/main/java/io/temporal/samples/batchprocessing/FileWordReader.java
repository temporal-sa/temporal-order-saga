package io.temporal.samples.batchprocessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileWordReader {

  /**
   * Reads lines from a text file starting from a given line offset and ending at a specified line.
   *
   * @param filePath The path to the text file.
   * @param readUntilLine The line number until which to read (inclusive).
   * @param offset The line number to start reading from (1-based index).
   * @return A list of strings, each representing a line from the file.
   */
  public List<String> readWordsFromFile(String filePath, int readUntilLine, int offset) {
    List<String> words = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      // Skip lines until the offset is reached
      for (int i = 1; i < offset; i++) {
        if (reader.readLine() == null) {
          return words; // Return empty list if offset is beyond file length
        }
      }

      String line;
      int currentLine = offset;
      while ((line = reader.readLine()) != null && currentLine <= readUntilLine) {
        words.add(line);
        currentLine++;
      }
    } catch (IOException e) {
      e.printStackTrace(); // Handle exceptions appropriately in your application
    }
    return words;
  }

  // main
  public static void main(String[] args) {
    String currentWorkingDirectory = System.getProperty("user.dir");
    System.out.println("Current working directory: " + currentWorkingDirectory);
    FileWordReader fileWordReader = new FileWordReader();
    List<String> words = fileWordReader.readWordsFromFile("words_alpha.txt", 10, 0);
    System.out.println(words);
  }
}
