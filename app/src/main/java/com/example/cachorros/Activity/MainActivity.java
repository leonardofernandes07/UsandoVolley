package com.example.cachorros.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cachorros.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Filterable {

    private final String url = "https://dog.ceo/api/breeds/list";
    private ArrayList<String> racas;
    private ListView lv;
    private RequestQueue queue;
    private String raca;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listaFiltrada = new ArrayList<>();
    private ArrayList<String> favoritos = new ArrayList<>();

    private EditText tvPesquisar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Favoritos.class);
                startActivity(intent);
            }
        });

        tvPesquisar = findViewById(R.id.tvPesquisar);
        tvPesquisar.addTextChangedListener(new TextWatcher() {
            final android.os.Handler handler = new android.os.Handler();
            Runnable runnable;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(runnable);
            }

            @Override
            public void afterTextChanged(Editable s) {

                runnable = new Runnable() {
                    @Override
                    public void run() {
                        adapter.getFilter().filter(tvPesquisar.getText());
                    }
                };
                handler.postDelayed(runnable,500);
            }
        });

        racas = new ArrayList<>();

        lv = findViewById(R.id.listview);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, racas);
        lv.setAdapter(adapter);

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("message");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                racas.add(jsonArray.get(i).toString());
                            }

                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e("erro", "Não deu " + error.getMessage());
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

                        try {

                            String subraca = "";

                            JSONObject message = response.getJSONObject("message");

                            JSONArray jsonArray = message.getJSONArray(raca);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                subraca += jsonArray.get(i).toString() + ",";
                            }

                            if (!subraca.equals("")) {
                                Intent i = new Intent(MainActivity.this, Sub_raca.class);
                                i.putExtra("raca", raca);
                                i.putExtra("subRaca", subraca);
                                startActivity(i);

                            } else {
                                Intent i = new Intent(MainActivity.this, ImagemActivity.class);
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

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long arg3) {
                // TODO Auto-generated method stub
                String racaClicada = racas.get(index);
                if (favoritos.indexOf(racaClicada) > -1){
                    // REMOVO DO FAVORITOS
                    favoritos.remove(racaClicada);
                    // TIRO O BG
                    v.setBackgroundColor(getResources().getColor(R.color.backgroud_app));
                    Log.e("Remove","Removeu do Array");
                }else{
                    // ADD O FAVORITOS
                    favoritos.add(racaClicada);
                    // ADD O BG
                    v.setBackgroundColor(Color.rgb(122,89,102));
                    Log.e("Add","Adiciona Array");
                }
                salvaSharedPreferences();
                Log.e("Test", "Selecionado");
                return true;
            }
        });

    }

    private void salvaSharedPreferences() {
        JSONArray favoritoson = new JSONArray(favoritos);

        String jsonString = favoritoson.toString();

        SharedPreferences Sfavoritos = getSharedPreferences("Foto", MODE_PRIVATE);
        SharedPreferences.Editor SEditorfavoritos = Sfavoritos.edit();
        SEditorfavoritos.putString("Favoritos", jsonString);
        SEditorfavoritos.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences foto = getSharedPreferences("Foto", MODE_PRIVATE);
        String url = foto.getString("ultima", "");
        ImageView ultima = findViewById(R.id.imageView2);

        if (!url.equals("")) {
            Picasso.get().load(url).into(ultima);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String key = constraint.toString();
                if (key.isEmpty()) {
                    listaFiltrada = racas;
                }else {
                    List<String> filtrados = new ArrayList<String>();
                    for (String s: racas){
                        if (s.contains(key)){
                            filtrados.add(s);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listaFiltrada;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listaFiltrada = (ArrayList<String>) results.values;
            }
        };
    }

}
