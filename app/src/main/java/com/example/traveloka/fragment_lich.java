package com.example.traveloka;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traveloka.Adapter.ChuyenBayAdapter;
import com.example.traveloka.Adapter.ChuyenBayCuaBanAdapter;
import com.example.traveloka.Adapter.ChuyenXeCuaBanAdapter;
import com.example.traveloka.Constant.Constant;
import com.example.traveloka.Volley.Account;
import com.example.traveloka.Volley.ChuyenBay;
import com.example.traveloka.Volley.ChuyenXe;
import com.example.traveloka.databinding.FragmentLichBinding;
import com.example.traveloka.databinding.FragmentTrangchuBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class fragment_lich extends Fragment {
    final String URL= Constant.BASE_API;
    FragmentLichBinding binding;
    MainActivity mainActivity;
    RequestQueue requestQueue;
    RequestQueue requestQueue2;
    Account account;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lich, container, false);
        mainActivity= (MainActivity) getActivity();
        account= mainActivity.getAccount();
        requestQueue= Volley.newRequestQueue(getContext());
        requestQueue2= Volley.newRequestQueue(getContext());

        //call api lịch trình
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL+ "/flightschedule/"+ account.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject1= new JSONObject(response);
                    int statusCode = jsonObject1.getInt("status");
                    String message = jsonObject1.getString("message");
                    JSONArray jsonArrayChuyenBay= jsonObject1.getJSONArray("data");
                    if (statusCode==201 || statusCode==200){
                        binding.tvErr.setVisibility(View.GONE);
                        List<ChuyenBay> chuyenBays= new ArrayList<>();

                        //tạo list chuyến bay sau đó render ra danh sách
                        int size= jsonArrayChuyenBay.length();
                        if (size==0){
                            binding.tvErr.setVisibility(View.VISIBLE);
                        }else
                        for (int i=0; i<size; i++){
                            JSONObject objChuyenBay= jsonArrayChuyenBay.getJSONObject(i);
                            int id = objChuyenBay.getInt("idFlight");
                            String noiDi= objChuyenBay.getString("fromWhere");
                            String noiDen= objChuyenBay.getString("toWhere");
                            String gioDi= objChuyenBay.getString("fromWhen");
                            String gioDen= objChuyenBay.getString("toWhen");
                            String hangGhe= objChuyenBay.getString("classSeat");
                            long giaVe= objChuyenBay.getLong("price");
                            ChuyenBay chuyenBay= new ChuyenBay(id, noiDi, noiDen, gioDi, gioDen, hangGhe, giaVe);
                            chuyenBays.add(chuyenBay);
                            ChuyenBayCuaBanAdapter bayAdapter= new ChuyenBayCuaBanAdapter(chuyenBays, getContext());
                            RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                            binding.revChuyenBayCuaBan.setAdapter(bayAdapter);
                            binding.revChuyenBayCuaBan.setLayoutManager(layoutManager);
                        }
                    }else {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        binding.tvErr.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Lỗi đường truyền, hãy thử lại", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);


        //call api lịch trình xe
        StringRequest stringRequestXe = new StringRequest(Request.Method.GET, URL+ "/carschedule/"+ account.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject1= new JSONObject(response);
                    int statusCode = jsonObject1.getInt("status");
                    String message = jsonObject1.getString("message");
                    JSONArray jsonArrayChuyenXe= jsonObject1.getJSONArray("data");
                    if (statusCode==201 || statusCode==200){
                        binding.tvErr2.setVisibility(View.GONE);
                        List<ChuyenXe> chuyenXes= new ArrayList<>();

                        //tạo list chuyến bay sau đó render ra danh sách
                        int size= jsonArrayChuyenXe.length();
                        if (size==0){
                            binding.tvErr2.setVisibility(View.VISIBLE);
                        }else
                        for (int i=0; i<size; i++){
                            JSONObject objChuyenXe= jsonArrayChuyenXe.getJSONObject(i);
                            int id = objChuyenXe.getInt("idFlight");
                            String noiDi= objChuyenXe.getString("fromWhere");
                            String noiDen= objChuyenXe.getString("toWhere");
                            int soGhe= objChuyenXe.getInt("slghe");
                            long giaXe= objChuyenXe.getLong("price");
                            ChuyenXe chuyenXe= new ChuyenXe(id, noiDi, noiDen, soGhe, giaXe);
                            chuyenXes.add(chuyenXe);
                            ChuyenXeCuaBanAdapter bayAdapter= new ChuyenXeCuaBanAdapter(chuyenXes, getContext());
                            RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                            binding.revChuyenBayCuaBan.setAdapter(bayAdapter);
                            binding.revChuyenBayCuaBan.setLayoutManager(layoutManager);
                        }
                    }else {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        binding.tvErr2.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Lỗi đường truyền, hãy thử lại", Toast.LENGTH_SHORT).show();
            }
        });


        requestQueue2.add(stringRequestXe);


        return binding.getRoot();
    }
}
