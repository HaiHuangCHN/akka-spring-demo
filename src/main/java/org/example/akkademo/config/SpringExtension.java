package org.example.akkademo.config;

import akka.actor.AbstractExtensionId;
import akka.actor.ExtendedActorSystem;
import akka.actor.Extension;
import akka.actor.Props;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringExtension extends AbstractExtensionId<SpringExtension.SpringExt> {


    public static final SpringExtension SPRING_EXTENSION_PROVIDER = new SpringExtension();

    @Override
    public SpringExt createExtension(ExtendedActorSystem system) {
        return new SpringExt();
    }

    /**
     * Extension 只是一个标记类
     * 实现类定义的方法，都会被调用
     */
    public static class SpringExt implements Extension {
        private volatile ApplicationContext applicationContext;

        public void initialize(ApplicationContext applicationContext) {
            log.info("Spring extension has been initialized, init ApplicationContext");
            this.applicationContext = applicationContext;
        }

        public Props props(String actorBeanName) {
            log.info("Spring extension has been initialized, init Props");
            return Props.create(SpringActorProducer.class, applicationContext, actorBeanName);
        }
    }


}
