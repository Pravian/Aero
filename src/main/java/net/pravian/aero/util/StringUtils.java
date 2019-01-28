package net.pravian.aero.util;

/**
 * Represents all String-related utilities.
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {

  private StringUtils() {
  }

  /**
   * Checks if a list of characters contains the character specified
   * <p>
   * <p>
   * Author: bergerkiller</p>
   *
   * @param value to find
   * @param values to search in
   * @return True if it is contained, False if not
   */
  public static boolean containsChar(char value, char... values) {
    for (char v : values) {
      if (v == value) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the first index of a string in an array of characters.
   * <p>
   * <p>
   * Author: bergerkiller</p>
   *
   * @param text The text to match.
   * @param values The characters to parse.
   * @return The first index or -1 if not found.
   */
  public static int firstIndexOf(String text, char... values) {
    for (int i = 0; i < text.length(); i++) {
      if (containsChar(text.charAt(i), values)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Returns the first index of a string in an array of strings.
   * <p>
   * <p>
   * Author: bergerkiller</p>
   *
   * @param text The text to match.
   * @param values The characters to parse.
   * @return The first index or -1 if not found.
   */
  public static int firstIndexOf(String text, String... values) {
    return firstIndexOf(text, 0, values);
  }

  /**
   * Returns the first index after a starting index of a string in an array of StringUtils.
   * <p>
   * <p>
   * Author: bergerkiller</p>
   *
   * @param text The text to match.
   * @param startindex The starting index.
   * @param values The characters to parse.
   * @return The first index or -1 if not found.
   */
  public static int firstIndexOf(String text, int startindex, String... values) {
    int i = -1;
    int index;
    for (String value : values) {
      if ((index = text.indexOf(value, startindex)) != -1 && (i == -1 || index < i)) {
        i = index;
      }
    }
    return i;
  }

  /**
   * Gets the text before the first occurrence of a given separator in a text.
   * <p>
   * <p>
   * Author: bergerkiller</p>
   *
   * @param text The text to use.
   * @param delimiter The delimiter to find.
   * @return The text before the first occurrence of the delimiter, or an empty String if not found.
   */
  public static String getBefore(String text, String delimiter) {
    final int index = text.indexOf(delimiter);
    return index >= 0 ? text.substring(0, index) : "";
  }

  /**
   * Gets the text after the first occurrence of a given separator in a text
   * <p>
   * <p>
   * Author: bergerkiller</p>
   *
   * @param text The text to use.
   * @param delimiter The delimiter to find.
   * @return The text after the first occurrence of the delimiter, or an empty String if not found
   */
  public static String getAfter(String text, String delimiter) {
    final int index = text.indexOf(delimiter);
    return index >= 0 ? text.substring(index + delimiter.length()) : "";
  }
}
