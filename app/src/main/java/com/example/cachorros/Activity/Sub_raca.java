package com.example.cachorros.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cachorros.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class Sub_raca extends AppCompatActivity {


    private TextView lblRaca;
    private ListView listView;
    private String subRaca;
    private String raca;
    private ImageView imageView;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_raca);
        getSupportActionBar().setTitle("Escolha a sub-raça");

        imageView = findViewById(R.id.imageView3);
        Intent intent = getIntent();

        raca = intent.getStringExtra("raca");
        subRaca = intent.getStringExtra("subRaca");

        queue = Volley.newRequestQueue(this);

        //TextView
        lblRaca = findViewById(R.id.lblRaca);
        lblRaca.setText(raca);

        listView = findViewById(R.id.listaSubRaca);

        Log.e("asdads",subRaca);

        ArrayList<String> subRacas = new ArrayList<>();

        String[] racas = subRaca.split(",");

        for(int i = 0; i<racas.length;i++){
            subRacas.add(racas[i]);
            Log.e("RAÇAS",racas[i]);
        }

        imagem(raca);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,subRacas);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String subRaca = listView.getItemAtPosition(position).toString();
                Intent resultIntent = new Intent(Sub_raca.this,ImagemActivity.class);
                resultIntent.putExtra("subRaca",subRaca);
                resultIntent.putExtra("raca",raca);
                resultIntent.putExtra("verif",true);
                startActivity(resultIntent);

            }
        });

    }

    private void imagem (String raca){
        JsonObjectRequest request = new JsonObjectRequest(0, "https://dog.ceo/api/breed/"+raca+"/images/random", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String img = response.get("message").toString();
                    Picasso.get().load(img).into(imageView);
                }catch (Exception e ){
                    Log.e("AAAA",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("AAAA",error.getMessage());
            }
        });

        queue.add(request);

    }
}