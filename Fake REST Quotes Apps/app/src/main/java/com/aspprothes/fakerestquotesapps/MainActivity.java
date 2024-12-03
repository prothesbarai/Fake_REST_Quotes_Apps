package com.aspprothes.fakerestquotesapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private String json_url = "https://dummyjson.com/quotes";
    private ArrayList<Quotes> quotesArrayList = new ArrayList<>();

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

            Quotes quotes = quotesArrayList.get(position);
            
            itemId.setText("Item No - "+quotes.getId());
            authorName.setText("-"+quotes.getAuthor());

            String getQuoteText = quotes.getQuote();
            quotesText.setText(""+getQuoteText);


            return convertView;
        }
    }
    // ================================================ Create Custom Array Adapter End Here ==================================
}