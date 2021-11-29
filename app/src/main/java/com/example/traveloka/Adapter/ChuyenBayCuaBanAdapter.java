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
import com.example.traveloka.R;
import com.example.traveloka.Volley.ChuyenBay;

import java.util.List;

public class ChuyenBayCuaBanAdapter  extends RecyclerView.Adapter<ChuyenBayCuaBanAdapter.ViewHolder> {
    List<ChuyenBay> list;
    Context context;
    RequestQueue requestQueue;

    public ChuyenBayCuaBanAdapter(List<ChuyenBay> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ChuyenBayCuaBanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_item_chuyen_bay_cua_ban, parent, false);
        ChuyenBayCuaBanAdapter.ViewHolder viewHolder=new ChuyenBayCuaBanAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChuyenBayCuaBanAdapter.ViewHolder holder, int position) {
        ChuyenBay chuyenBay=list.get(position);
        holder.tvGioDi.setText(chuyenBay.getGioDi());
        holder.tvGioDen.setText(chuyenBay.getGioDen());
        String[] hGioDi= chuyenBay.getGioDi().split(":");
        String[] hGioDen= chuyenBay.getGioDen().split(":");
        int hBay= Integer.parseInt(hGioDen[0])-Integer.parseInt(hGioDi[0]);
        int mBay= Integer.parseInt(hGioDen[1])+ 60 -Integer.parseInt(hGioDi[0]);
        holder.tvThoiGianBay.setText(hBay + "h "+ mBay+"m");
        holder.tvGiaVe.setText("VNĐ "+ chuyenBay.getGiaVe());

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
            tvGiaVe= itemView.findViewById(R.id.tvGiaVe_lb);
            tvThoiGianBay= itemView.findViewById(R.id.tvThoiGianBay_lb);
            tvGioDen= itemView.findViewById(R.id.tvGioDen_lb);
            tvGioDi= itemView.findViewById(R.id.tvGioDi_lb);
        }
    }
    public void setupData(List<ChuyenBay> l){
        list= l;
        notifyDataSetChanged();
    }
}