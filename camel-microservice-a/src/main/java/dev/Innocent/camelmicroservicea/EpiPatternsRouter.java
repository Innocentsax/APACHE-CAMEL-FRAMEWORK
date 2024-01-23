package dev.Innocent.camelmicroservicea;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EpiPatternsRouter extends RouteBuilder {
    @Autowired
    SplitterComponent splitterComponent;
    @Override
    public void configure() throws Exception {
        // Pipeline
        // Content based Routing - choice
        // Multicast
//        from("timer:multicast?period=10000")
//                .multicast()
//                .to("log:something1", "log:somethings2");
//
//        from("file:files/csv")
//                .unmarshal().csv()
//                .split(body())
//                .to("activemq:split-queue");
//
//        from("activemq:split-queue")
//                .to("log:received-message-from-active-mq");


        // message, message2, Message3
        from("file:files/csv")
//                .unmarshal().csv()
                .convertBodyTo(String.class)
//                .split(body(),",")
                .split(method(splitterComponent))
                .to("activemq:split-queue");
    }
}

@Component
class SplitterComponent{
    public List<String> splitInput(String body){
        return List.of("ABC", "DEF", "GHI");
    }
}
