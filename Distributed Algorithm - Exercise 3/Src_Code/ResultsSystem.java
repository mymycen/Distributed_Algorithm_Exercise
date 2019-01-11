package com.tub.result.src;

import com.tub.common.src.ResultAggregator;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Created by
 * Darshan Hingu 380584
 * RaviPrasad Marike Ramesh 387219
 * Seema Narasimha Swamy 384418
 * Yuchun Chen 387275
 */
/* Aggregates the result of two systems(Billing,Inventory and outputs to console as Valid(stream:out)/Invalid(Stream:err
Content based router is used to differentiate outputs
 */
public class ResultsSystem {
    public static void main (String[] args) throws Exception
    {
        DefaultCamelContext ctxt = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true); //Cannot access jms Component
        ctxt.addComponent("activemq", activeMQComponent);

        RouteBuilder route = new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("activemq:queue:aggregatorQueue")
                        //aggregator waits for 2 msgs and send to Result Aggregator
                        .aggregate(header("OrderID"), new ResultAggregator()).completionSize(2)
                        .choice()
                            //based on aggregation result, if its a valid msg, stream:out
                            .when(header("valid"))
                                .to("stream:out")
                            //else stream:err
                            .otherwise()
                                .to("stream:err");

            }
        };

        ctxt.addRoutes(route);

        ctxt.start();
        System.in.read();
        ctxt.stop();
    }
}
