package com.github.moko256.usbdebugswitch;

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

        try {
            int flag = Settings.Global.getInt(getContentResolver(), Settings.Global.ADB_ENABLED);

            if (flag == 0) {
                Settings.Global.putInt(getContentResolver(), Settings.Global.ADB_ENABLED, 1);
                tile.setIcon(Icon.createWithResource(this, R.drawable.ic_flash_on_black_24dp));
                tile.setState(Tile.STATE_ACTIVE);

            } else {
                Settings.Global.putInt(getContentResolver(), Settings.Global.ADB_ENABLED, 0);
                tile.setIcon(Icon.createWithResource(this, R.drawable.ic_flash_off_black_24dp));
                tile.setState(Tile.STATE_INACTIVE);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            tile.setIcon(Icon.createWithResource(this, R.drawable.ic_usb_black_24dp));
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
                tile.setIcon(Icon.createWithResource(this, R.drawable.ic_flash_on_black_24dp));
                tile.setState(Tile.STATE_ACTIVE);
            } else {
                tile.setIcon(Icon.createWithResource(this, R.drawable.ic_flash_off_black_24dp));
                tile.setState(Tile.STATE_INACTIVE);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            tile.setIcon(Icon.createWithResource(this, R.drawable.ic_usb_black_24dp));
        }

        tile.updateTile();
    }

}
