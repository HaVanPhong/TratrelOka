package com.example.traveloka.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traveloka.Constant.Constant;
import com.example.traveloka.R;
import com.example.traveloka.Volley.Account;
import com.example.traveloka.Volley.ChuyenBay;
import com.example.traveloka.Volley.ChuyenXe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ChuyenXeAdapter extends RecyclerView.Adapter<ChuyenXeAdapter.ViewHolder> {
    private static final String URL = Constant.BASE_API;
    List<ChuyenXe> list;
    Context context;
    RequestQueue requestQueue;
    Account account;

    public ChuyenXeAdapter(List<ChuyenXe> list, Context context, Account account) {
        this.list = list;
        this.context = context;
        this.account= account;
    }

    @NonNull
    @Override
    public ChuyenXeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_item_chuyen_xe, parent, false);
        ChuyenXeAdapter.ViewHolder viewHolder=new ChuyenXeAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChuyenXeAdapter.ViewHolder holder, int position) {
        ChuyenXe chuyenXe= list.get(position);
        holder.tvNoiDen_Di.setText(chuyenXe.getNoiDi() + " - "+ chuyenXe.getNoiDen());
        holder.tvTienXe.setText(chuyenXe.getTienXe()+" VNĐ");
        holder.tvSoGhe.setText(chuyenXe.getSoGheNgoi()+"");

        holder.btnDatXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestQueue= Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL+ "/carschedule/register/" +account.getId()+"/" + chuyenXe.getId(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Item: "+response +" -- "+ account.getId()+ account.getUsername()+"  / "+ chuyenXe.getId(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Đặt xe không thành công", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNoiDen_Di, tvTienXe, tvSoGhe;
        Button btnDatXe;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoiDen_Di= itemView.findViewById(R.id.tvNoiXe_Di_Den_item_tk);
            tvTienXe= itemView.findViewById(R.id.tvTienXe_tk);
            tvSoGhe= itemView.findViewById(R.id.tvSoGhe_tk);
            btnDatXe= itemView.findViewById(R.id.btnDatXe);
        }
    }
    public void setupData(List<ChuyenXe> l){
        list= l;
        notifyDataSetChanged();
    }
}