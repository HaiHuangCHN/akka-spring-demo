package org.example.akkademo.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// Actor消息协议
public class GreetingActor {

    // Greet 消息：用于请求问候
    public static final class Greet implements IActorMessage {
        // 要问候的名字
        public final String name;
        // 回复目标 Actor
        public final ActorRef<Greeted> replyTo;

        public Greet(String name, ActorRef<Greeted> replyTo) {
            this.name = name;
            this.replyTo = replyTo;
        }

    }

    // Greeted 消息：用于回复问候请求
    public static final class Greeted implements IActorMessage {
        // 问候消息
        public final String message;
        public final ActorRef<Greet> from;

        public Greeted(String message, ActorRef<Greet> from) {
            this.message = message;
            this.from = from;
        }
    }

    // Actor行为实现
    // 创建 Actor 行为的工厂方法
    public static Behavior<IActorMessage> create() {
        // Behaviors.setup 用于创建 Actor 的初始状态
        return Behaviors.setup(GreetingActorBehavior::new);
    }

    // Actor行为类（处理消息逻辑）
    private static class GreetingActorBehavior extends AbstractBehavior<IActorMessage> {
        private final ActorContext<IActorMessage> context;

        public GreetingActorBehavior(ActorContext<IActorMessage> context) {
            super(context);
            this.context = context;
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
            context.getLog().info("Hello! I'm {}", command.name);
            // 向发送者回复 Greeted 消息
            command.replyTo.tell(new Greeted("Hi, I'm Admin!", context.getSelf().unsafeUpcast()));
            // 返回 this 表示保持当前行为不变
            return this;
        }
    }

}