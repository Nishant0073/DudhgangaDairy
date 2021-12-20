package com.miniprojectG3.dudhgangadairy;


//Created customer class to store the information about the customer on the firestore
public class Customer {
    public String name;
    public int uniqueId;
    public String phoneNumber;
    public Customer(String name,int uniqueId,String phoneNumber)
    {
        this.name = name;
        this.uniqueId = uniqueId;
        this.phoneNumber = phoneNumber;
    }

    public Customer(){};
}
