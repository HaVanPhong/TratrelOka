package com.example.traveloka.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

import java.net.URL;
import java.util.List;

public class ChuyenBayAdapter extends RecyclerView.Adapter<ChuyenBayAdapter.ViewHolder> {
    private static final String URL = Constant.BASE_API;
    List<ChuyenBay> list;
    Context context;
    RequestQueue requestQueue;
    Account account;

    public ChuyenBayAdapter(List<ChuyenBay> list, Context context, Account account) {
        this.list = list;
        this.context = context;
        this.account= account;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_item_chuyen_bay, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChuyenBay chuyenBay=list.get(position);
        holder.tvGioDi.setText(chuyenBay.getGioDi());
        holder.tvGioDen.setText(chuyenBay.getGioDen());
        String[] hGioDi= chuyenBay.getGioDi().split(":");
        String[] hGioDen= chuyenBay.getGioDen().split(":");
        int hBay= Integer.parseInt(hGioDen[0])-Integer.parseInt(hGioDi[0]);
        int mBay= Integer.parseInt(hGioDen[1])+ 60 -Integer.parseInt(hGioDi[0]);
        holder.tvThoiGianBay.setText(hBay + "h "+ mBay+"m");
        holder.tvGiaVe.setText("VNĐ "+ chuyenBay.getGiaVe());

        holder.btnMuaVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestQueue= Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL + "/flightschedule/register/"+ account.getId()+"/"+chuyenBay.getId() , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Lỗi: Không call đươc api, hãy thử lại", Toast.LENGTH_SHORT).show();
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
        TextView tvGioDi, tvGioDen, tvThoiGianBay, tvGiaVe;
        Button btnMuaVe;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGiaVe= itemView.findViewById(R.id.tvGiaVe);
            tvThoiGianBay= itemView.findViewById(R.id.tvThoiGianBay);
            tvGioDen= itemView.findViewById(R.id.tvGioDen);
            tvGioDi= itemView.findViewById(R.id.tvGioDi);
            btnMuaVe= itemView.findViewById(R.id.btnMuaVe);
        }
    }
    public void setupData(List<ChuyenBay> l){
        list= l;
        notifyDataSetChanged();
    }
}