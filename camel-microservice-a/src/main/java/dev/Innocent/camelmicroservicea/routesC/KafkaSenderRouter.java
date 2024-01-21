package dev.Innocent.camelmicroservicea.routesC;

import org.apache.camel.builder.RouteBuilder;

//@Component
public class KafkaSenderRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:files/json")
                .log("${body}")
                .to("kafka:myKafkaTopic");
    }
}
