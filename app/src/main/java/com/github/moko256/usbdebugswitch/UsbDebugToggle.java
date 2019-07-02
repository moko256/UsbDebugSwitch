package com.github.moko256.usbdebugswitch;

import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.graphics.drawable.Icon;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

/**
 * Created by moko256 on 2017/12/08.
 *
 * @author moko256
 */

public class UsbDebugToggle extends TileService {

    private Icon onIcon;
    private Icon offIcon;
    private Icon unavailableIcon;

    @Override
    public void onCreate() {
        super.onCreate();
        onIcon = Icon.createWithResource(this, R.drawable.margined_on);
        offIcon = Icon.createWithResource(this, R.drawable.cellphone_link_off);
        unavailableIcon = Icon.createWithResource(this, R.drawable.ic_usb_black_24dp);
    }

    @Override
    public void onClick() {
        super.onClick();

        Tile tile = getQsTile();

        if (((KeyguardManager) getSystemService(KEYGUARD_SERVICE)).inKeyguardRestrictedInputMode()) {
            locked();
        } else {
            try {
                ContentResolver contentResolver = getContentResolver();
                int flag = Settings.Global.getInt(contentResolver, Settings.Global.ADB_ENABLED);

                if (flag == 0) {
                    Settings.Global.putInt(contentResolver, Settings.Global.ADB_ENABLED, 1);
                    on(tile);
                } else {
                    Settings.Global.putInt(contentResolver, Settings.Global.ADB_ENABLED, 0);
                    off(tile);
                }
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                unavailable(tile);
            }
        }

        tile.updateTile();

    }

    @Override
    public void onStartListening() {
        super.onStartListening();

        Tile tile = getQsTile();
        try {
            int flag = Settings.Global.getInt(getContentResolver(), Settings.Global.ADB_ENABLED);

            if (flag != 0) {
                on(tile);
            } else {
                off(tile);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            unavailable(tile);
        }

        tile.updateTile();
    }

    private void on(Tile tile) {
        tile.setIcon(onIcon);
        tile.setState(Tile.STATE_ACTIVE);
        tile.setLabel("Enable");
    }

    private void off(Tile tile) {
        tile.setIcon(offIcon);
        tile.setState(Tile.STATE_INACTIVE);
        tile.setLabel("Disable");
    }

    private void locked() {
        Toast.makeText(this, "Unavailable in lock screen", Toast.LENGTH_SHORT).show();
    }

    private void unavailable(Tile tile) {
        tile.setIcon(unavailableIcon);
        tile.setState(Tile.STATE_UNAVAILABLE);
        tile.setLabel("Unavailable");
    }

}
