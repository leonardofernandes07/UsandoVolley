package com.example.cachorros.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cachorros.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String url = "https://dog.ceo/api/breeds/list";
    private ArrayList<String> racas;
    private ListView lv;
    private RequestQueue queue;
    private String raca;
    private final int SUB_RACA = 2222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        racas = new ArrayList<>();

        lv = findViewById(R.id.listview);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,racas);
        lv.setAdapter(adapter);

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("message");

                            for(int i = 0; i < jsonArray.length(); i++){
                                racas.add(jsonArray.get(i).toString());

                            }

                            adapter.notifyDataSetChanged();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override


                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e("erro","Não deu "+error.getMessage());
                    }
                });

        queue.add(jsonObjectRequest);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                raca = lv.getItemAtPosition(position).toString();

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://dog.ceo/api/breeds/list/all", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{

                            String subraca = "";

                            JSONObject message = response.getJSONObject("message");


                            JSONArray jsonArray = message.getJSONArray(raca);

                            for(int i = 0; i<jsonArray.length(); i++){
                                subraca+= jsonArray.get(i).toString()+",";
                            }
                            
                            if(!subraca.equals("")){
                                Intent i = new Intent(MainActivity.this,Sub_raca.class);
                                i.putExtra("raca",raca);
                                i.putExtra("subRaca",subraca);
                                startActivity(i);
                                finish();
                            }else{
                                Intent i = new Intent(MainActivity.this, ImagemActivity.class);
                                i.putExtra("raca",raca);
                                startActivity(i);
                                finish();
                            }
                        }catch(Exception e ){
                            Log.e("Erro",e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                queue.add(request);
            }
        });

    }

}
