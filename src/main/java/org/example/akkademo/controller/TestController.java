package org.example.akkademo.controller;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.akkademo.actor.BossActor;
import org.example.akkademo.actor.GreetingActor;
import org.example.akkademo.config.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;

@Tag(name = "test")
@RestController
@RequestMapping("/test")
@Validated
public class TestController {

    @Autowired
    private ActorSystem actorSystem;

    @PostMapping("/testBossActor")
    @Operation(summary = "testBossActor")
    public ResponseEntity<Void> testBossActor() {
        ActorRef pcm = actorSystem.actorOf(Props.create(BossActor.class));
        pcm.tell("I AM MASTER. TELLING BOSS", ActorRef.noSender());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/testGreetingActor")
    @Operation(summary = "testGreetingActor")
    public ResponseEntity<Object> testGreetingActor() throws Exception {
        ActorRef greeter = actorSystem.actorOf(SpringExtension.SPRING_EXTENSION_PROVIDER.get(actorSystem).props("greetingActor"), "greeter");

        FiniteDuration duration = FiniteDuration.create(1, TimeUnit.SECONDS);
        Timeout timeout = Timeout.durationToTimeout(duration);

        Future<Object> result = ask(greeter, new GreetingActor.Greet("John"), timeout);

        // 使用 Scala 中 Await.result 等待 Future 结果
        Await.result(result, Duration.create(3, TimeUnit.SECONDS));

        return ResponseEntity.ok(result.value().get().get());
    }

}
