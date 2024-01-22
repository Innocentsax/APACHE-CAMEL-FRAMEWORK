package dev.Innocent.camelmicroservicea;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EpiPatternsRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // Pipeline
        // Content based Routing - choice
        // Multicast
        from("timer:multicast?period=10000")
                .multicast()
                .to("log:something1", "log:somethings2");

        from("file:files/csv")
                .unmarshal().csv()
                .split(body())
                .to("activemq:split-queue");

        from("activemq:split-queue")
                .to("log:received-message-from-active-mq");
    }
}
