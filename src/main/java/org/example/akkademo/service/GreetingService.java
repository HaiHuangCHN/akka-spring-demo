package org.example.akkademo.service;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Props;
import akka.actor.typed.javadsl.AskPattern;
import lombok.extern.slf4j.Slf4j;
import org.example.akkademo.actor.Greet;
import org.example.akkademo.actor.Greeted;
import org.example.akkademo.actor.GreetingActor;
import org.example.akkademo.actor.IActorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

@Slf4j
@Service
public class GreetingService {

    private final ActorSystem<Void> actorSystem;
    private final ActorRef<IActorMessage> greetingActor;

    @Autowired
    public GreetingService(ActorSystem<Void> actorSystem) {
        this.actorSystem = actorSystem;
        // 创建GreetingActor实例
        this.greetingActor = actorSystem.systemActorOf(GreetingActor.create(), "greeting-actor", Props.empty());
    }

    // 异步发送消息并获取回复
    public CompletionStage<String> greet(String name) {
        return AskPattern.ask(
                greetingActor,
                (ActorRef<IActorMessage> replyTo) ->
                        new Greet(name, replyTo),
                Duration.ofSeconds(3),
                actorSystem.scheduler()
        ).thenApply(response -> {
            if (response instanceof Greeted greeted) {
                log.info("Received response: {}", greeted.message);
                return greeted.message;
            }

            return response.toString();
        }).exceptionally(ex -> {
            log.error("Failed to get response", ex);
            return "Error: " + ex.getMessage();
        });
    }



}