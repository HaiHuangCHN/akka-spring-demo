package org.example.akkademo.actor;

import akka.actor.typed.ActorRef;

// Greet 消息：用于请求问候
public class Greet implements IActorMessage {
    // 要问候的名字
    public final String name;
    // 需要回复的目标 Actor
    public final ActorRef<Greeted> replyTo;

    public Greet(String name, ActorRef<Greeted> replyTo) {
        this.name = name;
        this.replyTo = replyTo;
    }

}

