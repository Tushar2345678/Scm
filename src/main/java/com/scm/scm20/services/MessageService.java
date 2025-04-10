package com.scm.scm20.services;

import java.util.List;

import com.scm.scm20.entities.Message;

public interface MessageService {

    public Message sendMessage(Message message);

    List<Message> getConversation(String senderId, String receiverId);
;

    /* public List<Message> getMessagesBySenderAndReceiver(Long senderId, Long receiverId);

    public void deleteMessage(String messageId);

    public void deleteAllMessagesByUserId(Long userId);

    public void deleteAllMessagesBySenderAndReceiver(Long senderId, Long receiverId);
 */


}
