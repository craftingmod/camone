package com.camone;

import android.content.Context;
import android.provider.Settings;

public class SettingsEditor {
  private static final String key = "csc_pref_camera_forced_shuttersound_key";

  public static final String configStore = "config";
  public static final String userShutter = "user_shutter";
  public static final String startOnBoot = "start_on_boot";
  public static boolean setForcedShutterSound(Context context, int value) {
    if (Settings.System.canWrite(context)) {
      try {
        Settings.System.putInt(context.getContentResolver(), key, value);
        return true;
      } catch (SecurityException e) {
        e.printStackTrace();
      }
    }
    return false;
  }
  public static int getForcedShutterSound(Context context) {
    return Settings.System.getInt(context.getContentResolver(), key, 1);
  }
  public static boolean havePerm(Context context) {
    return Settings.System.canWrite(context);
  }
}
