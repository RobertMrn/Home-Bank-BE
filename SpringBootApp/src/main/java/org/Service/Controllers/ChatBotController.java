package org.Service.Controllers;

import org.DTOs.ChatBotResponse;
import org.Service.Services.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class ChatBotController {
    @Autowired
    private ChatBotService chatBotService;

    @GetMapping("/getResponseChatBot")
    public ChatBotResponse getResponseChatBot(@RequestParam String question, @RequestParam int userId){
        ChatBotResponse chatBotResponse = chatBotService.getChatBotResponse(question, userId);
        return chatBotResponse;
    }
}
