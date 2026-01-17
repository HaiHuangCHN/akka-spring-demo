package org.example.akkademo.actor;

import akka.actor.typed.ActorRef;

// Greeted 消息：用于回复问候请求
public final class Greeted implements IActorMessage {
    // 问候消息
    public final String message;
    // 从哪个 Actor 来的
    public final ActorRef<IActorMessage> from;

    public Greeted(String message, ActorRef<IActorMessage> from) {
        this.message = message;
        this.from = from;
    }
}

