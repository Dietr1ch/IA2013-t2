package ai.utils;

import java.io.PrintWriter;

public class Utils {

  
  /**
   * Autoflushing std out stream writer.
   */
  public static PrintWriter out = new PrintWriter(System.out, true);
  /**
   * Autoflushing std err stream writer.
   */
  public static PrintWriter err = new PrintWriter(System.err, true);
}
