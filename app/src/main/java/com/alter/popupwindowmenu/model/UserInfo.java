package com.alter.popupwindowmenu.model;

/**
 * @CreateDate: 2018/3/9
 * @Author: lzsheng
 * @Description:
 * @Version:
 */
public class UserInfo {

    private long userId;
    private String nickName;
    private String picUrl;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", picUrl='" + picUrl + '\'' +
                '}';
    }
}
