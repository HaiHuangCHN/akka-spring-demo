package org.example.akkademo.config;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AkkaConfig {

    @Bean
    public ActorSystem<Void> actorSystem() {
        // 创建根Actor，不处理任何消息（仅作为系统入口）
        ActorSystem<Void> system = ActorSystem.create(Behaviors.empty(), "spring-akka-system");
        return system;
    }

}
