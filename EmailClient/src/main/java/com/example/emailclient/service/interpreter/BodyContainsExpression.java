package com.example.emailclient.service.interpreter;

import com.example.emailclient.model.EmailMessage;

public class BodyContainsExpression implements Expression {
    private final String text;

    public BodyContainsExpression(String text) {
        this.text = text.toLowerCase();
    }

    @Override
    public boolean interpret(EmailMessage msg) {
        return msg.getBody() != null && msg.getBody().toLowerCase().contains(text);
    }
}

