package dev.Innocent.camelmicroservicea.routesB;

import org.apache.camel.builder.RouteBuilder;

//@Component
public class MyFileRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:files/input")
                .log("${body}")
                .to("file:files/output");
    }
}
