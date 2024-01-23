package dev.Innocent.camelmicroservicea;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
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
//        from("file:files/csv")
////                .unmarshal().csv()
//                .convertBodyTo(String.class)
////                .split(body(),",")
//                .split(method(splitterComponent))
//                .to("activemq:split-queue");

        // Aggregate
        // Message => Aggregate => Endpoint
        // to, 3
        from("file:files/aggregate-json")
                .unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
                .aggregate(simple("${body.to}"), new ArrayListAggregationStrategy())
                .completionSize(3)
//                .completionTimeout(HIGHEST)
                .to("log:aggregate-json");

        String routingSlip = "direct:endpoint1,direct:endpoint2";
        // String routingSlip = "direct:endpoint1,direct:endpoint2,direct:endpoint3"

        from("timer:routingSlip?period=1000")
                .transform().constant("My message is hardcoded")
                .routingSlip(simple(routingSlip));

        from("direct:endpoint1")
                .to("log:directendpoint1");

        from("direct:endpoint2")
                .to("log:directendpoint2");

        from("direct:endpoint3")
                .to("log:directendpoint3");
    }
}

@Component
class SplitterComponent{
    public List<String> splitInput(String body){
        return List.of("ABC", "DEF", "GHI");
    }
}
