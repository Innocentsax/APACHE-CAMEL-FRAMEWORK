package dev.Innocent.camelmicroservicea.routes;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyFirstTimerRouter extends RouteBuilder {
    @Autowired
    private GetCurrentTimeBean getCurrentTimeBean;
    @Override
    public void configure() throws Exception {
        /**
         * Timer
         * transformation
         * log
         * Processing
         */
        from("timer:first-timer")
                .log("${body}")
                //.transform().constant("my Constant Message")
                //.transform().constant("Time now is " + LocalDateTime.now())
                //.bean("getCurrentTimeBean")
                .bean(getCurrentTimeBean,"getCurrentTime")
                .log("${body}")
                .to("log:first-timer");
    }
}
@Component
class GetCurrentTimeBean{
    public String getCurrentTime(){
        return "Time now is " + LocalDateTime.now();
    }
}

@Component
class SimpleLoggingProcessingComponent{
    private final Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);
    public void process(String message){
        logger.info("SimpleLoggingProcessingComponent {}", message);
    }
}

