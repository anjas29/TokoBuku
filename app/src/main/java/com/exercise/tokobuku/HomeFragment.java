package com.exercise.tokobuku;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.exercise.tokobuku.adapter.ListBukuAdapter;
import com.exercise.tokobuku.object.Buku;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView listBuku;
    ArrayList<Buku> data;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //Mengambil tampilan RecyclerView
        listBuku = (RecyclerView) view.findViewById(R.id.list_view);

        //Mengambil data dari database internet
        getData();


        return view;
    }

    public void getData(){
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                "http://tokobuku-stembayo.esy.es/list_buku.php", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("DEBUGS", response.toString());

                try {
                    data = new ArrayList<Buku>();
                    for (int i=0; i<response.length();i++){
                        JSONObject object = response.getJSONObject(i);
                        int id = object.getInt("id");
                        String judul_buku = object.getString("judul_buku");
                        String pengarang =object.getString("pengarang");
                        String penerbit = object.getString("penerbit");
                        String deskripsi = object.getString("deskripsi");

                        data.add(new Buku(id, judul_buku, pengarang, penerbit, deskripsi));
                    }
                    //Menambahkan data kedalam RecyclerView
                    LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    listBuku.setHasFixedSize(true);

                    listBuku.setLayoutManager(layoutManager);

                    ListBukuAdapter adapter = new ListBukuAdapter(getActivity().getApplicationContext(), data);

                    listBuku.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("DEBUGS", "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

}
