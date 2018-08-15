
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.io.*;

/**
 * Reads from a file to get the top five high scores
 */
public class HighScores {

    TreeMap<Integer, String> scores = new TreeMap<Integer, String>();
    
  /** A special purpose exception class to indicate errors when reading 
   *  the input.
   */
  public static class FormatException extends Exception {
    public FormatException(String msg) {
      super(msg);
    }
  }


  /**
   * Constructs an instance from the supplied Reader. 
   * @param r The sequence of characters to parse 
   * @throws IOException for an io error while reading
   * @throws FormatException for an invalid line
   * @throws IllegalArgumentException if the provided reader is null
   */
  public HighScores(Reader r) throws IOException, FormatException, NumberFormatException {
      if (r == null) { throw new IllegalArgumentException("reader is null"); }
          BufferedReader br = new BufferedReader(r);
          String line = br.readLine();
          while (line != null && line.length() > 0) {
              if (!checkValidLine(line)) {
                  throw new HighScores.FormatException("invalid line"); 
              }
              String score = "";
              String un = "";
              int commaLoc = getCommaLocation(line);
              for (int i = 0; i < commaLoc; i++) {
                      un += line.charAt(i);
              }
              for (int j = commaLoc + 1; j < line.length(); j++) {
                      score += line.charAt(j);
              }
              un = un.trim();
              score = score.trim();
              int intScore = Integer.parseInt(score);
              scores.put(intScore, un);
              line = br.readLine();
          }
  }
  
  public boolean checkValidLine(String line) {
      if (line.charAt(0) == ',') { return false; }
      if (line.charAt(line.length() - 1) == ',') { return false; }
      boolean containsComma = false;
      for (int i = 0; i < line.length(); i++) {
          if (line.charAt(i) == ',') {
              if (containsComma == true) {
                  return false;
              } else {
                  try {
                      Integer.parseInt((line.substring(i + 1, line.length())).trim());
                  } catch (NumberFormatException n) {
                      return false;
                  }
              }
              containsComma = true;
          }
      }
      return containsComma;
  }
  
  public int getCommaLocation(String line) {
      for (int i = 0; i < line.length(); i++) {
          if (line.charAt(i) == ',') {
              return i;
          }
      }
      return -1;
  }

  /** Construct from a file.
   *
   * @param filename of file to read from
   * @throws IOException if error while reading
   * @throws FormatException for an invalid line
   * @throws FileNotFoundException if file cannot be opened
   */
  public static HighScores make(String filename) throws IOException, FormatException {
      Reader r = new FileReader(filename);
      HighScores hs;
      try {
            hs = new HighScores(r);
      } finally {
          if (r != null) { r.close(); }
      }
      return hs;
  }

   /**
   * Returns a set of the top five scores
   *
   * @return a (potentially empty) set of five top scores
   */
  public TreeMap<Integer, String> getHighScores() {
      TreeMap<Integer, String> topFive = new TreeMap<Integer, String>();
      while (!scores.isEmpty()) {
          Map.Entry<Integer, String> score1 = scores.pollLastEntry();
          topFive.put(score1.getKey(), score1.getValue());
      }
      return topFive;
  }
}
