package com.nminh.websiteinstagram.controller;

import com.nminh.websiteinstagram.entity.Message;
import com.nminh.websiteinstagram.model.request.ChatMessageDTO;
import com.nminh.websiteinstagram.model.response.ApiResponse;
import com.nminh.websiteinstagram.model.response.MessageResponseDTO;
import com.nminh.websiteinstagram.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user/chat")
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void send(ChatMessageDTO chatMessageDTO) {
        MessageResponseDTO savedMessage = chatService.save(chatMessageDTO);
        // send to receiver
        simpMessagingTemplate.convertAndSendToUser(
                chatMessageDTO.getReceiverId().toString(),
                "/queue/messages", savedMessage
        );
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageDTO chatMessageDTO) {
        MessageResponseDTO savedMessage = chatService.save(chatMessageDTO);
        // send message to real time
        simpMessagingTemplate.convertAndSendToUser(
                chatMessageDTO.getReceiverId().toString(),
                "/queue/messages",
                savedMessage
        );
        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getChatHistory(@RequestParam Long  user1Id , @RequestParam Long  user2Id) {
        List<MessageResponseDTO> chatHistory = chatService.getHistory(user1Id,user2Id) ;
        return ResponseEntity.ok(chatHistory);
    }

    // lấy danh sách những người đã từng nhắn tin với mình
    @GetMapping("/history-people")
    public ApiResponse getPeopleMessage(){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(chatService.getHistoryPeople());
        return apiResponse;
    }
}
