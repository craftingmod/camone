package com.camone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    SharedPreferences pref = context.getSharedPreferences(SettingsEditor.configStore, Context.MODE_PRIVATE);
    boolean startOnBoot = pref.getBoolean(SettingsEditor.startOnBoot, false);
    if (!startOnBoot) {
      Log.i("CamOne-BootReceiver", "startOnBoot is false!");
      return;
    }
    if (!SettingsEditor.havePerm(context)) {
      Log.i("CamOne-BootReceiver", "Settings permission is not exist!");
      return;
    }
    if (!pref.contains(SettingsEditor.userShutter)) {
      Log.i("CamOne-BootReceiver", "User shutter value is not exist!");
      return;
    }
    boolean shutterSound = pref.getBoolean(SettingsEditor.userShutter, false);
    if (!SettingsEditor.setForcedShutterSound(context, shutterSound ? 1 : 0)) {
      Log.i("CamOne-BootReceiver", "Modification failed!");
      return;
    }
    Log.i("CamOne-BootReceiver", "ShutterSound successfully changed to " + shutterSound + "!");
  }
}