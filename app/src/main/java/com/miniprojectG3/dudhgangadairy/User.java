package com.miniprojectG3.dudhgangadairy;

//Creating the user class for storing the user data on the database.
public class User {
    public String address;
    public String contactNumber;
    public String emailId ;
    public String nameOfDairy ;
    public   String nameOfOwner;
    public  int uniqueInitialize = 1;
    public  User(String nameOfOwner,String nameOfDairy,String emailId,String address,String contactNumber)
    {
        this.address = address;
        this.contactNumber = contactNumber;
        this.emailId = emailId;
        this.nameOfDairy = nameOfDairy;
        this.nameOfOwner = nameOfOwner;
    }
    public User(){};
}
