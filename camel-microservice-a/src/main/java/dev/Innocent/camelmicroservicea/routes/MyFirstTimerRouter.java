package dev.Innocent.camelmicroservicea.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyFirstTimerRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        /**
         * Timer
         * transformation
         * log
         */
        from("timer:first-timer")
                //.transform().constant("my Constant Message")
                //.transform().constant("Time now is " + LocalDateTime.now())
                .bean("getCurrentTimeBean")
                .to("log:first-timer");
    }
}

@Component
class GetCurrentTimeBean{
    public String getCurrentTime(){
        return "Time now is " + LocalDateTime.now();
    }
}
