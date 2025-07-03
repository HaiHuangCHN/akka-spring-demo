package org.example.akkademo.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

// Actor消息协议
public class GreetingActor {

    // 消息类型
    public static final class Greet {
        public final String name;
        public final ActorRef<Greeted> replyTo;

        public Greet(String name, ActorRef<Greeted> replyTo) {
            this.name = name;
            this.replyTo = replyTo;
        }

    }

    public static final class Greeted {
        public final String message;
        public final ActorRef<Greet> from;

        public Greeted(String message, ActorRef<Greet> from) {
            this.message = message;
            this.from = from;
        }
    }

    // Actor行为实现
    public static Behavior<Greet> create() {
        return Behaviors.setup(GreetingActorBehavior::new);
    }

    // Actor行为类（处理消息逻辑）
    private static class GreetingActorBehavior extends AbstractBehavior<Greet> {
        private final ActorContext<Greet> context;

        public GreetingActorBehavior(ActorContext<Greet> context) {
            super(context);
            this.context = context;
        }

        @Override
        public Receive<Greet> createReceive() {
            return newReceiveBuilder()
                    .onMessage(Greet.class, this::onGreet)
                    .build();
        }

        private Behavior<Greet> onGreet(Greet command) {
            context.getLog().info("Hello {}!", command.name);
            command.replyTo.tell(new Greeted("Hello " + command.name, getContext().getSelf()));
            return this;
        }
    }

}