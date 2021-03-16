package datadog.trace.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DDTraceApiInfo {
  private static final Logger log = LoggerFactory.getLogger(DDTraceApiInfo.class);
  public static final String VERSION;

  static {
    String v;
    try (final BufferedReader br =
        new BufferedReader(
            new InputStreamReader(
                DDTraceApiInfo.class.getResourceAsStream("/dd-trace-api.version"), "UTF-8"))) {
      final StringBuilder sb = new StringBuilder();

      for (int c = br.read(); c != -1; c = br.read()) sb.append((char) c);

      v = sb.toString().trim();
    } catch (final Exception e) {
      v = "unknown";
    }
    VERSION = v;
    log.debug("dd-trace-api - version: {}", v);
  }

  public static void main(final String... args) {
    System.out.println(VERSION);
  }
}
