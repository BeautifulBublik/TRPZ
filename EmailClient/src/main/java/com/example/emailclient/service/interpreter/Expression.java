package com.example.emailclient.service.interpreter;

import com.example.emailclient.model.EmailMessage;

public interface Expression {
    boolean interpret(EmailMessage message);
}

