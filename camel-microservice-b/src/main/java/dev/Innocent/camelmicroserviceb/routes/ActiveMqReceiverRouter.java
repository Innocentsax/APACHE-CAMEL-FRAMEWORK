package dev.Innocent.camelmicroserviceb.routes;

import dev.Innocent.camelmicroserviceb.CurrencyExchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ActiveMqReceiverRouter extends RouteBuilder {
    @Autowired
    private MyCurrencyExchangeProcessor myCurrencyExchangeProcessor;
    @Autowired
    private MyCurrencyExchangeTransformer myCurrencyExchangeTransformer;
    @Override
    public void configure() throws Exception {
        from("activemq:my-activemq-queue")
                .unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
                .bean(myCurrencyExchangeProcessor)
                .bean(myCurrencyExchangeTransformer)
                .to("log:received-message-from-active-mq");
    }
}
@Component
class MyCurrencyExchangeProcessor{
    Logger logger = LoggerFactory.getLogger(MyCurrencyExchangeProcessor.class);
    public void processMessage(CurrencyExchange currencyExchange){
        logger.info("Do some processing with currencyExchange.getConversionMultiple() value which is {}",
                currencyExchange.getConversionMultiple());
    }
}

@Component
class MyCurrencyExchangeTransformer{
    Logger logger = LoggerFactory.getLogger(MyCurrencyExchangeTransformer.class);
    public CurrencyExchange processMessage(CurrencyExchange currencyExchange){
        currencyExchange.setConversionMultiple(currencyExchange.getConversionMultiple().multiply(BigDecimal.TEN));
        return currencyExchange;
    }
}
