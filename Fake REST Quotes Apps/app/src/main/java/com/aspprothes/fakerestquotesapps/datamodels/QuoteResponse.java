package com.aspprothes.fakerestquotesapps.datamodels;

import java.util.ArrayList;

public class QuoteResponse{
    private ArrayList<Quotes> quotes;

    public QuoteResponse(ArrayList<Quotes> quotes){
        this.quotes = quotes;
    }

    public ArrayList<Quotes> getQuotes(){
        return this.quotes;
    }

}
