package com.codingbingoZTT.BanmaReader.manager;

import com.codingbingoZTT.BanmaReader.Constants;
import com.codingbingoZTT.BanmaReader.utils.SharedPreferenceUtils;

/**
 *
 *
 * By 2017/2/14.
 */

public class SettingManager {

    private volatile static SettingManager instance;
    private SharedPreferenceUtils sharedPreferenceUtils;

    public static SettingManager getInstance() {
        if (instance == null) {
            synchronized (SettingManager.class) {
                if (instance == null) {
                    instance = new SettingManager();
                    instance.sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
                }
            }
        }
        return instance;
    }

    public int getReadFontSize(){
        return instance.sharedPreferenceUtils.getInt("fontSize", Constants.STYLE_NORMAL_FONT_SIZE);
    }

    public void setReadFontSize(int fontSize){
        instance.sharedPreferenceUtils.putInt("fontSize", fontSize);
    }

    public boolean getReadMode(){
        return instance.sharedPreferenceUtils.getBoolean("nightMode", false);
    }

    public void setReadMode(boolean value){
        instance.sharedPreferenceUtils.putBoolean("nightMode", value);
    }

    public String getReadBackground(){
        return instance.sharedPreferenceUtils.getString("readBackground", "#FFFFFF");
    }

    public void setReadBackground(String color){
        instance.sharedPreferenceUtils.putString("readBackground", color);
    }
}
