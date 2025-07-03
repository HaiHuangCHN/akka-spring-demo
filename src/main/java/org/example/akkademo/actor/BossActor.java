package org.example.akkademo.actor;

import akka.actor.UntypedAbstractActor;

public class BossActor extends UntypedAbstractActor {


    @Override
    public void onReceive(Object message) {
        System.out.println(message);
    }


}
