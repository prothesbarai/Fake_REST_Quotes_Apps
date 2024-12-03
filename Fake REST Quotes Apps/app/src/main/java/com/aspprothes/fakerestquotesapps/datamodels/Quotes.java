package com.aspprothes.fakerestquotesapps.datamodels;

public class Quotes {
    private int id;
    private String quote;
    private String author;

    public Quotes(int id,String quote,String author){
        this.id = id;
        this.quote = quote;
        this.author = author;
    }

    public int getId(){
        return this.id;
    }

    public String getQuote(){
        return this.quote;
    }

    public String getAuthor(){
        return this.author;
    }

}
