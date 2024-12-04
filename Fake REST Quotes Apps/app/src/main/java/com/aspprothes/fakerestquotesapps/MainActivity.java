package com.aspprothes.fakerestquotesapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aspprothes.fakerestquotesapps.datamodels.Quotes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private long backPressedDefaultTime = 0;
    private static final long backPressedInTime = 2000;
    private ListView listView;
    private String json_url = "https://dummyjson.com/quotes";
    private ArrayList<Quotes> quotesArrayList = new ArrayList<>();
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setStatusBarColor(getResources().getColor(R.color.sky));
        this.getWindow().setNavigationBarColor(getResources().getColor(R.color.sky));
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, json_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("quotes");

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Integer id = jsonObject.getInt("id");
                        String quote = jsonObject.getString("quote");
                        String author = jsonObject.getString("author");

                        Quotes quotes = new Quotes(id,quote,author);
                        quotesArrayList.add(quotes);
                    }

                    CustomArrayAdapter customArrayAdapter = new CustomArrayAdapter();
                    listView.setAdapter(customArrayAdapter);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something Error", Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjectRequest);


    }
    // ========================================== On Create Method End Here ===============================================




    // ================================================ Create Custom Array Adapter Start Here ==================================
    public class CustomArrayAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return quotesArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return quotesArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.listviews_items_design,parent,false);
            }
            TextView itemId = convertView.findViewById(R.id.itemId);
            TextView quotesText = convertView.findViewById(R.id.quotesText);
            TextView authorName = convertView.findViewById(R.id.authorName);
            ImageView copyItems = convertView.findViewById(R.id.copyItems);
            ImageView soundItems = convertView.findViewById(R.id.soundItems);
            ImageView shareItems = convertView.findViewById(R.id.shareItems);
            ImageView whatsAppShareItems = convertView.findViewById(R.id.whatsAppShareItems);


            Quotes quotes = quotesArrayList.get(position);
            
            itemId.setText("Item No - "+quotes.getId());
            authorName.setText("-"+quotes.getAuthor());

            String getQuoteText = quotes.getQuote();
            quotesText.setText(""+getQuoteText);

            textToSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR){
                        textToSpeech.setLanguage(new Locale("bn_IN"));
                    }
                }
            });

            soundItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textToSpeech.speak(getQuoteText,TextToSpeech.QUEUE_FLUSH,null);
                }
            });

            copyItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("text",getQuoteText);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(MainActivity.this, "Copied", Toast.LENGTH_SHORT).show();
                }
            });

            shareItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT,getQuoteText);
                    intent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(intent,null);
                    startActivity(shareIntent);

                    textToSpeech.stop();
                }
            });

            whatsAppShareItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT,getQuoteText);
                    intent.setType("text/plain");
                    intent.setPackage("com.whatsapp");

                    Intent share = Intent.createChooser(intent,null);
                    try {
                        startActivity(share);
                        textToSpeech.stop();
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "Whatsapp Not Install", Toast.LENGTH_SHORT).show();
                    }

                }
            });



            return convertView;
        }
    }
    // ================================================ Create Custom Array Adapter End Here ==================================





    @Override
    public void onBackPressed() {
        if (isTaskRoot()){
            long currentTime = System.currentTimeMillis();
            if (currentTime-backPressedDefaultTime<backPressedInTime){
                finish();
            }else{
                backPressedDefaultTime = currentTime;
                Toast.makeText(this, "Again tap to exit", Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onBackPressed();
        }
    }



}