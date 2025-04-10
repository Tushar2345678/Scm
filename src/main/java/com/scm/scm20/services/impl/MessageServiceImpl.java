package com.scm.scm20.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import com.scm.scm20.entities.Message;
import com.scm.scm20.repositories.MessageRepository;
import com.scm.scm20.services.MessageService;

@Service
public class MessageServiceImpl implements MessageService{

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Message sendMessage(Message message) {
        message.setTimestamp(new Date());
        return messageRepository.save(message);
    }


    @Override
    public List<Message> getConversation(String senderId, String receiverId) {
    return messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
            senderId, receiverId, senderId, receiverId
    );
}

}
