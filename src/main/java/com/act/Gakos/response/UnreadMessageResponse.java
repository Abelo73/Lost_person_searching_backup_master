package com.act.Gakos.response;

public class UnreadMessageResponse {

    private int unreadMessage;

    public UnreadMessageResponse(Integer unreadMessage) {
        this.unreadMessage = unreadMessage;
    }

    public int getUnreadMessage() {
        return unreadMessage;
    }

    public void setUnreadMessage(Integer unreadMessage) {
        this.unreadMessage = unreadMessage;
    }
}
