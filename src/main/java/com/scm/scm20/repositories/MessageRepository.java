package com.scm.scm20.repositories;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.scm.scm20.entities.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
    
    List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
        String senderId1, String receiverId1, String senderId2, String receiverId2
);

}
