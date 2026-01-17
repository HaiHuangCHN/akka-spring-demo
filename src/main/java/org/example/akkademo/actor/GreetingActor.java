package org.example.akkademo.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// Actor行为类（处理消息逻辑）
public class GreetingActor extends AbstractBehavior<IActorMessage> {

    public GreetingActor(ActorContext<IActorMessage> context) {
        super(context);
    }

    // Actor行为实现
    // 创建 Actor 行为的工厂方法
    public static Behavior<IActorMessage> create() {
        // Behaviors.setup 用于创建 Actor 的初始状态
        return Behaviors.setup(GreetingActor::new);
    }

    // 定义如何处理接收到的消息
    @Override
    public Receive<IActorMessage> createReceive() {
        return newReceiveBuilder()
                .onMessage(Greet.class, this::onGreet)
                .build();
    }

    private Behavior<IActorMessage> onGreet(Greet command) {
        // 记录接收到的消息
        getContext().getLog().info("Hello! I'm {}", command.name);
        // 向发送者回复 Greeted 消息
        try {
            command.replyTo.tell(new Greeted("Hi, I'm Admin!", getContext().getSelf()));
        } catch (Exception e) {
            getContext().getLog().error("Failed to send reply", e);
        }
        // 返回 this 表示保持当前行为不变
        return this;
    }
}

