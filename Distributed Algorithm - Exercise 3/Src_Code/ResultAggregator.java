package com.tub.common.src;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * Created by seema on 17/01/2017.
 */
public class ResultAggregator implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange exchange, Exchange exchange1) {
        if (exchange == null) {
            System.out.println("Returning new exchange");
            return exchange1;
        }

        TransformedOrder order1 = (TransformedOrder)exchange.getIn().getBody();
        TransformedOrder order2 = (TransformedOrder)exchange1.getIn().getBody();
        boolean result = order1.getValid() && order2.getValid();
        order1.setValid(result);
        exchange.getIn().setHeader("valid", result);
        exchange.getIn().setBody(order1);
        return exchange;
    }
}
