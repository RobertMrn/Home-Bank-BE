package org.Service.Entities;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "CHAT_BOT")
public class ChatBot {
    @Id
    @Column(name = "chat_bot_id", nullable = false)
    @GeneratedValue
    private String chatBotId;

    @Column
    private String question;

    @Column
    private String answer;

    @Column
    private String type;
}
