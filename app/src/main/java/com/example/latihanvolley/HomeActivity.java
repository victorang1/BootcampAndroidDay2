package com.example.latihanvolley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.latihanvolley.databinding.ActivityHomeBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements HomeAdapter.onItemClickCallback {

    private ActivityHomeBinding binding;
    private SessionManager sessionManager;
    private ArrayList<Note> userList = new ArrayList<>();
    private HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        sessionManager = new SessionManager(this);

        if(!sessionManager.isLoggedIn()) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }

        mAdapter = new HomeAdapter(userList, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(mAdapter);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, InsertActivity.class));
            }
        });
    }

    @Override
    public void onIconDeleteClick(final Note note) {

        Toast.makeText(this, "Delete Clicked", Toast.LENGTH_SHORT).show();
        String url = "https://raderking-gg.herokuapp.com/note/" + note.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(HomeActivity.this, "Delete Success", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onIconEditClick(Note note) {

        try {
            JSONObject object = new JSONObject();
            object.put("title", note.getTitle());
            object.put("content", note.getContent());

            Toast.makeText(this, "Edit Clicked", Toast.LENGTH_SHORT).show();
            String url = "https://raderking-gg.herokuapp.com/note/" + note.getId();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(HomeActivity.this, "Update Success", Toast.LENGTH_SHORT).show();
                            loadData();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }
            );

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        userList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://raderking-gg.herokuapp.com/notes";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                       for(int i = 0; i < response.length(); i++) {
                           try {
                               JSONObject jsonObject = response.getJSONObject(i);
                               Note note = new Note(jsonObject.getString("_id"), jsonObject.getString("title"), jsonObject.getString("content"));
                               userList.add(note);
                           } catch (Exception e) {

                           }
                       }
                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("<RESULT>", "onErrorResponse: " + error.getMessage());
                    }
                }
        );
        queue.add(arrayRequest);
    }
}
