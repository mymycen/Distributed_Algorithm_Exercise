package com.tub.callcenterorder.src;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.processor.aggregate.AggregationStrategy;


/**
 * Created by
 * Darshan Hingu 380584
 * RaviPrasad Marike Ramesh 387219
 * Seema Narasimha Swamy 384418
 * Yuchun Chen 387275
 */
public class CallCenterOrderSystem {
        //Convert the incoming order to the required format to write to a file(Message transformer/Content Enricher)
        private static Processor callCenterOrderProcessor = new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                String[] parts = exchange.getIn().getBody(String.class).split(",");
                String newOrderFormat;
                if (parts.length == 5) {
                    String firstName = parts[0];
                    String lastName = parts[1];
                    String numSurfBoards = parts[2];
                    String numDivingSuits = parts[3];
                    String customerId = parts[4];
                    newOrderFormat = customerId + "," + firstName + " " + lastName + "," + numSurfBoards
                            + "," + numDivingSuits + "\n";
                }
                else
                {
                    newOrderFormat = exchange.getIn().getBody(String.class);
                }
                exchange.getIn().setBody(newOrderFormat);
            }
        };

    /*Aggregator used to aggregate messages and print every 2 minutes into file*/
    public static class CountingAggregation implements AggregationStrategy {

        @Override
        public Exchange aggregate(Exchange exchange, Exchange exchange1) {
            if (exchange == null) {
                return exchange1;
            }

            Object body = exchange.getIn().getBody();
            Object body1 = exchange1.getIn().getBody();
            exchange1.getIn().setBody(body + "" + body1);

            return exchange1;
        }
    }

    public static void main(String[] args) throws Exception {
        DefaultCamelContext ctxt = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        ctxt.addComponent("activemq", activeMQComponent);

        RouteBuilder route = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                //read from the queue
                from("activemq:queue:orderQueue")
                        .process(callCenterOrderProcessor)
                        //aggregate messages ,wait for 2 minutes//reduce to 10 sec
                        .aggregate(constant(0), new CallCenterOrderSystem.CountingAggregation()).completionInterval(10*1000)
                        //write to a file
                        .to("file:/Users/darshan/Downloads/Task3/src/main/java/com/tub/callCenterOrdersData?fileExist=Append&fileName=orders.txt")
                        //Testcode, output to stream to check the interval of aggregation
                        .to("stream:out");
            }
        };

        ctxt.addRoutes(route);

        ctxt.start();
        System.in.read();
        ctxt.stop();
    }
}
