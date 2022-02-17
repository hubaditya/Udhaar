package com.example.infinia.udhaar;

import java.sql.Date;

public class Details
{
    String name;
    String date;
    String location;
    int debit;
    int credit;

    public Details(String name,String date,String location,int debit,int credit)
    {
        this.name=name;
        this.date=date;
        this.location=location;
        this.debit=debit;
        this.credit=credit;
    }

    public String getName()
    { return this.name; }

    public String getDate()
    { return this.date; }

    public String getLocation()
    { return this.location; }

    public int getDebit()
    { return this.debit; }

    public int getCredit()
    { return this.credit; }
}
