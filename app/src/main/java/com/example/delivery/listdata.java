package com.example.delivery;

public class listdata {
    private int poster;
    private String name;
    private String price;

    public listdata( int poster,String name, String price){

        this.poster=poster;
        this.name=name;
        this.price=price;

    }

    public  String getPrice(){
        return this.price;
    }
    public String getName(){
        return this.name;
    }
    public  int getPoster(){
        return this.poster;
    }
}
