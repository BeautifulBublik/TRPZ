package com.example.emailclient.service.interpreter;

import com.example.emailclient.model.EmailMessage;

public class AndExpression implements Expression {
    private final Expression left;
    private final Expression right;

    public AndExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean interpret(EmailMessage msg) {
        return left.interpret(msg) && right.interpret(msg);
    }
}

