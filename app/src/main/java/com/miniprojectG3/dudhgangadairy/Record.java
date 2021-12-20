package com.miniprojectG3.dudhgangadairy;

public class Record {
    public String cName;
    public String time;
    public int id;
    public float fat;
    public float snf;
    public float cost;
    public float rate;
    public float weight;
    public String date;
    public String milkType;

    public Record(String cName,String time,int id,float fat,float snf,float weight,float cost,float rate,String date,String milkType)
    {
        this.cName = cName;
        this.time = time;
        this.id = id;
        this.fat = fat;
        this.snf = snf;
        this.cost = cost;
        this.rate = rate;
        this.date = date;
        this.weight = weight;
        this.milkType = milkType;
    }
    public Record(){};
    public Record(String cName)
    {
        this.cName = cName;
    }
}
