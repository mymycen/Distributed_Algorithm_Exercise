package com.tub.inventory.src;

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
public class InventorySystem {
        //Check the availability of the requested items
        private static Processor InventoryProcessor = new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                TransformedOrder order = (TransformedOrder) exchange.getIn().getBody();
                boolean validated = Math.random() > 0.5;
                order.setValid(validated);

                System.out.println(order);
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
                //read from pub-sub channel
                from("activemq:queue:inventory")
                        .process(InventoryProcessor)
                        //write to point to point channel for aggregation
                        .to("activemq:queue:aggregatorQueue");
            }
        };

        ctxt.addRoutes(route);

        ctxt.start();
        System.in.read();
        ctxt.stop();
    }
}
