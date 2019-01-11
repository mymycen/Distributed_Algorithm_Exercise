package com.tub.billing.src;

import com.tub.common.src.TransformedOrder;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;


/**
 * Created by
 * Darshan Hingu 380584
 * RaviPrasad Marike Ramesh 387219
 * Seema Narasimha Swamy 384418
 * Yuchun Chen 387275
 */
public class BillingSystem {
        //Processor which handles the task of credit check for the customer
        private static Processor BillingProcessor = new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                TransformedOrder order = (TransformedOrder) exchange.getIn().getBody();
                //validation is done using a random function as we dont have customer data
                boolean validated = Math.random() > 0.5;
                order.setValid(validated);

                System.out.println(order);
                //header set for aggregation
                exchange.getIn().setHeader("OrderId", order.getOrderID());
                exchange.getIn().setBody(order);
            }
        };

    public static void main(String[] args) throws Exception{
        DefaultCamelContext ctxt = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        ctxt.addComponent("activemq", activeMQComponent);

        RouteBuilder route = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                //read from pub-sub channel and write to aggregator queue
                from("activemq:queue:billing")
                        .process(BillingProcessor)
                        //point to point channel for aggregation and result system
                        .to("activemq:queue:aggregatorQueue");
            }
        };

        ctxt.addRoutes(route);

        ctxt.start();
        System.in.read();
        ctxt.stop();
    }
}
