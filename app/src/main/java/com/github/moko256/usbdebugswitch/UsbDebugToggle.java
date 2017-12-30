package com.github.moko256.usbdebugswitch;

import android.app.KeyguardManager;
import android.graphics.drawable.Icon;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

/**
 * Created by moko256 on 2017/12/08.
 *
 * @author moko256
 */

public class UsbDebugToggle extends TileService {
    @Override
    public void onClick() {
        super.onClick();

        Tile tile = getQsTile();

        if (((KeyguardManager) getSystemService(KEYGUARD_SERVICE)).inKeyguardRestrictedInputMode()){
            locked(tile);
        } else {
            try {
                int flag = Settings.Global.getInt(getContentResolver(), Settings.Global.ADB_ENABLED);

                if (flag == 0) {
                    Settings.Global.putInt(getContentResolver(), Settings.Global.ADB_ENABLED, 1);
                    on(tile);
                } else {
                    Settings.Global.putInt(getContentResolver(), Settings.Global.ADB_ENABLED, 0);
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
            if (((KeyguardManager) getSystemService(KEYGUARD_SERVICE)).inKeyguardRestrictedInputMode()){
                locked(tile);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            unavailable(tile);
        }

        tile.updateTile();
    }

    private void on(Tile tile){
        tile.setIcon(Icon.createWithResource(this, R.drawable.ic_flash_on_black_24dp));
        tile.setState(Tile.STATE_ACTIVE);
        tile.setLabel("Enable");
    }

    private void off(Tile tile){
        tile.setIcon(Icon.createWithResource(this, R.drawable.ic_flash_off_black_24dp));
        tile.setState(Tile.STATE_INACTIVE);
        tile.setLabel("Disable");
    }

    private void locked(Tile tile){
        tile.setState(Tile.STATE_UNAVAILABLE);
        tile.setLabel("Unavailable in lock screen");
    }

    private void unavailable(Tile tile){
        tile.setIcon(Icon.createWithResource(this, R.drawable.ic_usb_black_24dp));
        tile.setState(Tile.STATE_UNAVAILABLE);
        tile.setLabel("Unavailable");
    }

}
