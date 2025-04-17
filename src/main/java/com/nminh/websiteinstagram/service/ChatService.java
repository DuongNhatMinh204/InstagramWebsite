package com.nminh.websiteinstagram.service;

import com.nminh.websiteinstagram.entity.Message;
import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.enums.ErrorCode;
import com.nminh.websiteinstagram.exception.AppException;
import com.nminh.websiteinstagram.model.request.ChatMessageDTO;
import com.nminh.websiteinstagram.model.response.MessageResponseDTO;
import com.nminh.websiteinstagram.repository.MessageRepository;
import com.nminh.websiteinstagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageResponseDTO save(ChatMessageDTO chatMessageDTO) {
        User sender = userRepository.findById(chatMessageDTO.getSenderId()).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTS));
        User receiver = userRepository.findById(chatMessageDTO.getReceiverId()).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTS));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(chatMessageDTO.getContent());
        messageRepository.save(message);

        MessageResponseDTO messageResponseDTO = new MessageResponseDTO(
                                                            message.getId() ,
                                                            message.getSender().getId() ,
                                                            message.getReceiver().getId() ,
                                                            message.getContent() ,
                                                            message.getTimestamp() ,
                                                            message.isSeen()
                                                    ) ;

        return messageResponseDTO ;
    }

    public List<MessageResponseDTO> getHistory(Long userId1 , Long userId2) {
        User user1 = userRepository.findById(userId1).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTS));
        User user2 = userRepository.findById(userId2).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTS));

        List <Message> messages = messageRepository.findChatBeetweenUser(user1, user2);

        return  messages.stream().map(msg -> new MessageResponseDTO(
                                                            msg.getId(),
                                                            msg.getSender().getId() ,
                                                            msg.getReceiver().getId(),
                                                            msg.getContent(),
                                                            msg.getTimestamp(),
                                                            msg.isSeen()
                                                        )).toList() ;
    }
}
