package com.example.emailclient.service.interpreter;

import com.example.emailclient.model.EmailMessage;

public class SubjectContainsExpression implements Expression {
    private final String text;

    public SubjectContainsExpression(String text) {
        this.text = text.toLowerCase();
    }

    @Override
    public boolean interpret(EmailMessage msg) {
        return msg.getSubject() != null && msg.getSubject().toLowerCase().contains(text);
    }
}


