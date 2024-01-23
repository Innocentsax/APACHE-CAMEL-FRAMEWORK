package dev.Innocent.camelmicroservicea;

import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class EpiPatternsRouter extends RouteBuilder {
    @Autowired
    SplitterComponent splitterComponent;

    @Autowired
    DynamicRouterBean dynamicRouterBean;

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
                .to("{{endpoint-for-logging}}");

        from("direct:endpoint2")
                .to("log:directendpoint2");

        from("direct:endpoint3")
                .to("log:directendpoint3");

        /**
         * Dynamic Routing pattern(Step 1, Step2, Step 3)
         * Endpoint 1
         * Endpoint 2
         * Endpoint 3
         */

        // Set tracing to see all information
        getContext().setTracing(true);

        from("timer:dynamicRouting?period={{timePeriod}}")
                .transform().constant("My message is hardcoded")
                .dynamicRouter(method(dynamicRouterBean));
    }
}

@Component
class SplitterComponent{
    public List<String> splitInput(String body){
        return List.of("ABC", "DEF", "GHI");
    }
}

//class DynamicRouterBean implements AggregationStrategy {
//    @Override
//    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
//        Object newBody = newExchange.getIn().getBody();
//        ArrayList<Object> list = null;
//        if(oldExchange == null){
//            list = new ArrayList<Object>();
//            list.add(newBody);
//            newExchange.getIn().setBody(list);
//            return newExchange;
//        }
//        else{
//            list = oldExchange.getIn().getBody(ArrayList.class);
//            list.add(newBody);
//            return oldExchange;
//        }
//    }
//}

@Component
class DynamicRouterBean{
    Logger logger = LoggerFactory.getLogger(DynamicRouterBean.class);
    int invocations;
    public String decideTheNextEndpoint(@ExchangeProperties Map<String, String> properties,
                                        @Headers Map<String, String> headers, @Body String body) {
        logger.info("{} {} {}", properties, headers, body);
        invocations++;

        if(invocations%3==0)
            return "direct:endpoint1";
        if(invocations%3==1)
            return "direct:endpoint2,direct:endpoint3";

        return null;
    }
}

