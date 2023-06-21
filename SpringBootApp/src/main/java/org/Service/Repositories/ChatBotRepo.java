package org.Service.Repositories;

import org.Service.Entities.ChatBot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public interface ChatBotRepo extends CrudRepository<ChatBot, Integer> {

    ChatBot findByQuestion(String question);
}
