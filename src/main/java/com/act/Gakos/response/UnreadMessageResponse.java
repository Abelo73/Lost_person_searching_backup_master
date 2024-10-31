package com.act.Gakos.response;

public class UnreadMessageResponse {

    private int unreadMessage;

    public UnreadMessageResponse(int unreadMessage) {
        this.unreadMessage = unreadMessage;
    }

    public int getUnreadMessage() {
        return unreadMessage;
    }

    public void setUnreadMessage(int unreadMessage) {
        this.unreadMessage = unreadMessage;
    }
}
