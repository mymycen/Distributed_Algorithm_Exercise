package com.tub.weborder.src;

import com.tub.common.src.TransformedOrder;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.io.File;

/**
 * Created by
 * Darshan Hingu 380584
 * RaviPrasad Marike Ramesh 387219
 * Seema Narasimha Swamy 384418
 * Yuchun Chen 387275
 */
public class WebOrderSystem {
    private static int orderID = 1;
        /* Converts the incoming new order to the format expected by Billing and Inventory System*/
        private static Processor orderProcessor = new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                String[] parts = exchange.getIn().getBody(String.class).split(",");
                String FirstName = parts[0];
                String LastName = parts[1];
                int numOrderedSurfBoards = Integer.parseInt(parts[2]);
                int numOrderedDivingSuits = Integer.parseInt(parts[3]);
                int customerId = Integer.parseInt(parts[4]);

                System.out.println("CamelMain: process(): customerId: " + customerId);
                /* Create a transformedOrder object and set as body of exchange*/
                exchange.getIn().setBody(new TransformedOrder(customerId, FirstName, LastName,
                        numOrderedDivingSuits + numOrderedSurfBoards,
                        numOrderedDivingSuits,numOrderedSurfBoards,orderID, false));
                orderID++;
            }
        };


    public static void main(String[] args) throws Exception {
        DefaultCamelContext ctxt = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        ctxt.addComponent("activemq", activeMQComponent);

        RouteBuilder route = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:/Users/darshan/Downloads/Task3/src/main/java/com/tub/webOrders?noop=true")
                        .split(body().tokenize("\n"))
                        //convert to string class as expected by CallcenterOrderSystem
                        .convertBodyTo(String.class)
                        //add to callCenter activemq - Channel adapter endpoint
                        .to("activemq:queue:orderQueue")
                        .process(orderProcessor)
                        //using pub-sub pattern , send from weborder to billing,inventory
                        .to("activemq:queue:billing", "activemq:queue:inventory");

            }
        };

        ctxt.addRoutes(route);

        ctxt.start();
        System.in.read();
        ctxt.stop();
    }
}