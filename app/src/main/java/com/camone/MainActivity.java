package com.camone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    SharedPreferences pref = this.getSharedPreferences(SettingsEditor.configStore, Context.MODE_PRIVATE);

    Switch shutterStart = findViewById(R.id.checkbox_start);
    shutterStart.setChecked(pref.getBoolean(SettingsEditor.startOnBoot, false));

    ImageButton githubBtn = findViewById(R.id.btn_github);
    githubBtn.setOnClickListener(view -> {
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.setData(Uri.parse("https://github.com/craftingmod/camone"));
      startActivity(intent);
    });

    RelativeLayout shutterLayout = findViewById(R.id.layout_shutter);
    // Toggle shutter
    shutterLayout.setOnClickListener(view -> {
      if (havePerm()) {
        int modifyValue = Math.abs(this.getShutterSound() + 1) % 2;
        if (SettingsEditor.setForcedShutterSound(this, modifyValue)) {
          pref.edit().putBoolean(SettingsEditor.userShutter, modifyValue == 1).apply();
        } else {
          Toast.makeText(this, R.string.toast_edit_failed, Toast.LENGTH_LONG).show();
        }
        updateState();
      }
    });

    RelativeLayout startLayout = findViewById(R.id.layout_start);
    startLayout.setOnClickListener(view -> {
      boolean startOnBoot = pref.getBoolean(SettingsEditor.startOnBoot, false);
      pref.edit().putBoolean(SettingsEditor.startOnBoot, !startOnBoot).apply();
      shutterStart.setChecked(!startOnBoot);
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (!havePerm()) {
      showPermDialog();
    }
    updateState();
  }

  private void showPermDialog() {
    new AlertDialog.Builder(this)
        .setTitle(R.string.dialog_perm_title)
        .setMessage(R.string.dialog_perm_desc)
        .setPositiveButton(R.string.ok, (dialog, i) -> {
          Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + this.getPackageName()));
          startActivity(intent);
        })
        .setNegativeButton(R.string.cancel, (dialog, i) -> {
          dialog.cancel();
        })
        .show();
  }
  private int getShutterSound() {
    return SettingsEditor.getForcedShutterSound(this);
  }
  private boolean havePerm() {
    return SettingsEditor.havePerm(this);
  }
  private void updateState() {
    Switch shutterSwitch = findViewById(R.id.checkbox_shutter);

    boolean isForced = SettingsEditor.getForcedShutterSound(this) == 1;
    shutterSwitch.setEnabled(havePerm());
    shutterSwitch.setVisibility(View.VISIBLE);
    shutterSwitch.setChecked(isForced);
  }
}