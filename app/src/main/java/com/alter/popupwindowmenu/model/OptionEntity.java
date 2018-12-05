package com.alter.popupwindowmenu.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by lzsheng on 2018/1/24.
 */

public class OptionEntity implements Serializable {

    private int index;
    private String iconUrl;
    private Drawable drawable;
    private String optionName;

    public OptionEntity(int index, Drawable drawable, String optionName) {
        this.index = index;
        this.drawable = drawable;
        this.optionName = optionName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    @Override
    public String toString() {
        return "OptionEntity{" +
                "index=" + index +
                ", iconUrl='" + iconUrl + '\'' +
                ", drawable=" + drawable +
                ", optionName='" + optionName + '\'' +
                '}';
    }
}
