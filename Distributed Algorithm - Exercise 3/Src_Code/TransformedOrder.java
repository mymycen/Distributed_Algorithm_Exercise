package com.tub.common.src;

import java.io.Serializable;

/**
 * Created by seema on 17/01/2017.
 */
public class TransformedOrder implements Serializable {

    public int getCustomerID() {
        return CustomerID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public int getOverallItems() {
        return OverallItems;
    }

    public int getNumberOfDivingSuits() {
        return NumberOfDivingSuits;
    }

    public int getNumberOfSurfboards() {
        return NumberOfSurfboards;
    }

    public int getOrderID() {
        return OrderID;
    }

    public Boolean getValid() {
        return Valid;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setOverallItems(int overallItems) {
        OverallItems = overallItems;
    }

    public void setNumberOfDivingSuits(int numberOfDivingSuits) {
        NumberOfDivingSuits = numberOfDivingSuits;
    }

    public void setNumberOfSurfboards(int numberOfSurfboards) {
        NumberOfSurfboards = numberOfSurfboards;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public void setValid(Boolean valid) {
        Valid = valid;
    }

    private int CustomerID;
    private String FirstName;
    private String LastName;
    private int    OverallItems;
    private int    NumberOfDivingSuits;
    private int    NumberOfSurfboards;
    private int    OrderID;
    private Boolean Valid;

    public TransformedOrder( int CustomerID, String FirstName, String LastName, int OverallItems,
                             int NumberOfDivingSuits, int NumberOfSurfboards, int OrderID, Boolean Valid) {
        this.CustomerID = CustomerID;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.OverallItems = OverallItems;
        this.NumberOfDivingSuits = NumberOfDivingSuits;
        this.NumberOfSurfboards = NumberOfSurfboards;
        this.OrderID = OrderID;
        this.Valid = Valid;
    }


    @Override
    public String toString() {
        return "Order{" +
                "CustomerID='" + CustomerID + '\'' +
                ", FirstName=" + FirstName +
                ", LastName=" + LastName +
                ", OverallItems=" + OverallItems +
                ", NumberOfDivingSuits=" + NumberOfDivingSuits +
                ", NumberOfSurfboards=" + NumberOfSurfboards +
                ", OrderID=" + OrderID +
                ", Valid=" + Valid +
                '}';
    }
}
