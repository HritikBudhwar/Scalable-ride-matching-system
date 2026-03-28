package com.platform.model.support;

import com.platform.model.enums.MessageType;
import com.platform.model.user.User;

import java.time.LocalDateTime;

/**
 * Message entity class
 */
public class Message {
    private Long id;
    private User sender;
    private User receiver;
    private MessageType messageType;
    private String subject;
    private String content;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
    private boolean isRead;
}