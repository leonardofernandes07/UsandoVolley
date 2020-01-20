package com.example.cachorros.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
    String favoritoss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        listView = findViewById(R.id.lisfavoritos);

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

    }
}
