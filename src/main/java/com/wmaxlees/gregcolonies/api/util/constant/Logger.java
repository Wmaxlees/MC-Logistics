package com.wmaxlees.gregcolonies.api.util.constant;

import com.mojang.logging.LogUtils;

public final class Logger {
  private static final org.slf4j.Logger LOGGER = LogUtils.getLogger();

  public static void DebugLog(String msg, Object... args) {
    // Append an easy to see string at the front of messages
    String fmtMsg = String.format("@@@@@@@@@@@@ %s: %s", Constants.MOD_ID, msg);
    LOGGER.info(fmtMsg, args);
  }

  public static void InfoLog(String msg, Object... args) {
    String fmtMsg = String.format("%s: %s", Constants.MOD_ID, msg);
    LOGGER.info(fmtMsg, args);
  }

  public static void WarnLog(String msg, Object... args) {
    String fmtMsg = String.format("%s: %s", Constants.MOD_ID, msg);
    LOGGER.warn(fmtMsg, args);
  }
}
