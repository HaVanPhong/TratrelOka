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
import com.example.traveloka.R;
import com.example.traveloka.Volley.ChuyenXe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ChuyenXeCuaBanAdapter extends RecyclerView.Adapter<ChuyenXeCuaBanAdapter.ViewHolder> {
    List<ChuyenXe> list;
    Context context;

    public ChuyenXeCuaBanAdapter(List<ChuyenXe> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ChuyenXeCuaBanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_item_chuyen_xe_cua_ban, parent, false);
        ChuyenXeCuaBanAdapter.ViewHolder viewHolder=new ChuyenXeCuaBanAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChuyenXeCuaBanAdapter.ViewHolder holder, int position) {
        ChuyenXe chuyenXe= list.get(position);
        holder.tvNoiDen_Di.setText(chuyenXe.getNoiDi() + " - "+ chuyenXe.getNoiDen());
        holder.tvTienXe.setText(chuyenXe.getTienXe()+" VNĐ");
        holder.tvSoGhe.setText(chuyenXe.getSoGheNgoi()+"");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNoiDen_Di, tvTienXe, tvSoGhe;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoiDen_Di= itemView.findViewById(R.id.tvNoiXe_Di_Den);
            tvTienXe= itemView.findViewById(R.id.tvTienXe);
            tvSoGhe= itemView.findViewById(R.id.tvSoGhe);
        }
    }
    public void setupData(List<ChuyenXe> l){
        list= l;
        notifyDataSetChanged();
    }
}