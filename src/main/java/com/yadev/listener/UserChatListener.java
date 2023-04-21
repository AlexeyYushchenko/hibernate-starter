package com.yadev.listener;

import com.yadev.entity.UserChat;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
public class UserChatListener {
    @PostPersist
    public void postPersist(UserChat userChat) {
        var chat = userChat.getChat();
        chat.setUserCount(chat.getUserCount() + 1);
    }

    @PostRemove
    public void postRemove(UserChat userChat) {
        var chat = userChat.getChat();
        chat.setUserCount(chat.getUserCount() - 1);
    }
}
