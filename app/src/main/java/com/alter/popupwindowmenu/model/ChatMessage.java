package com.alter.popupwindowmenu.model;

/**
 * @CreateDate: 2018/3/9
 * @Author: lzsheng
 * @Description:
 * @Version:
 */
public class ChatMessage {

    private UserInfo userInfo;
    private int from;
    private String msgContent;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "userInfo=" + userInfo +
                ", from=" + from +
                ", msgContent='" + msgContent + '\'' +
                '}';
    }
}
