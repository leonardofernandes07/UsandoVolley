package com.example.cachorros.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cachorros.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Favoritos extends AppCompatActivity {

    private ArrayList<String> favoritos = new ArrayList<>();
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private Intent i;
    private String favoritoss;
    private String raca;
    private  String url;
    private RequestQueue queue;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        listView = findViewById(R.id.lisfavoritos);

        i = getIntent();
        raca = i.getStringExtra("raca");

        queue = Volley.newRequestQueue(this);

        imageView = findViewById(R.id.imageView3);


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favoritos);
        Log.e("Adapter","ListView");

        listView.setAdapter(adapter);

        SharedPreferences SFavoritos = getSharedPreferences("Foto", MODE_PRIVATE);
        favoritoss = SFavoritos.getString("Favoritos", "[]");

        try {
            JSONArray jsonArray = new JSONArray(favoritoss);
            for (int i = 0; i < jsonArray.length(); i++) {
                favoritos.add(jsonArray.get(i).toString());
                Log.e("try","Entrou");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                raca = listView.getItemAtPosition(position).toString();

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://dog.ceo/api/breeds/list/all", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String subraca = "";

                            JSONObject message = response.getJSONObject("message");

                            JSONArray jsonArray = message.getJSONArray(raca);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                subraca += jsonArray.get(i).toString() + ",";
                            }

                            if (!subraca.equals("")) {
                                Intent i = new Intent(Favoritos.this, Sub_raca.class);
                                i.putExtra("raca", raca);
                                i.putExtra("subRaca", subraca);
                                startActivity(i);

                            } else {
                                Intent i = new Intent(Favoritos.this, ImagemActivity.class);
                                i.putExtra("raca", raca);
                                startActivity(i);

                            }
                        } catch (Exception e) {
                            Log.e("Erro", e.getMessage());
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
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences foto = getSharedPreferences("Foto", MODE_PRIVATE);
        String url = foto.getString("ultima", "");
        ImageView ultima = findViewById(R.id.imageView3);

        if (!url.equals("")) {
            Picasso.get().load(url).into(ultima);
        }
    }
}
