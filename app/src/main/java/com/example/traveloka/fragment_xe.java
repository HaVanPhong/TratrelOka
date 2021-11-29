package com.example.traveloka;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
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
import com.example.traveloka.Adapter.ChuyenXeAdapter;
import com.example.traveloka.Constant.Constant;
import com.example.traveloka.R;
import com.example.traveloka.Volley.Account;
import com.example.traveloka.Volley.ChuyenBay;
import com.example.traveloka.Volley.ChuyenXe;
import com.example.traveloka.databinding.FragmentTrangchuBinding;
import com.example.traveloka.databinding.FragmentXeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_xe extends Fragment {
    FragmentXeBinding binding;
    final String URL= Constant.BASE_API;
    final String[] noiXeDi = {"Hà Nội", "Nam Định", "Quảng Ninh", "Bắc Giang"};
    final String[] noiXeDen = {"SB Nội Bài", "SB Tân Sơn Nhất", "Quảng Ninh", "Bắc Giang"};
    final String[] gioDonXe = {"00","01","02","03","04","05","06","07","08","09","10","11","12"};
    final String[] phutDonXe = {"00","15","30","45","60"};
    String[] ngayDonXe = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"};
    RequestQueue requestQueue;
    Account account;

    MainActivity mainActivity;

    String rNoiDi="Hà Nội", rNoiDen="SB Nội Bài";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_xe, container, false);
        mainActivity = (MainActivity) getActivity();
        account = mainActivity.getAccount();
        requestQueue = Volley.newRequestQueue(getContext());
        binding.btnTimXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_chuyen_xe);

                Window window = dialog.getWindow();
                if (window == null) {
                    return;
                }
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                WindowManager.LayoutParams windowAtributes = window.getAttributes();
                windowAtributes.gravity = Gravity.CENTER_VERTICAL;
                window.setAttributes(windowAtributes);

                dialog.setCancelable(true);
                //ánh xạ
                TextView tvBack= dialog.findViewById(R.id.tvDialogXeBack);
                TextView tvKhongCoXe= dialog.findViewById(R.id.tvKhongCoXe);
                TextView tvLoading= dialog.findViewById(R.id.tvLoading);
                RecyclerView revChuyenXe= dialog.findViewById(R.id.revChuyenXe);
                //
                tvLoading.setVisibility(View.VISIBLE);
                tvBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                //call api chuyeens xe


                JSONObject jsonObjTimXe= new JSONObject();
                try {
                    jsonObjTimXe.put("fromWhere", rNoiDi);
                    jsonObjTimXe.put("toWhere", rNoiDen);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String requestBody = jsonObjTimXe.toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL+ "/toairport/findtoairport", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArrayChuyenXe= new JSONArray(response);
                                tvKhongCoXe.setVisibility(View.GONE);
                                tvLoading.setVisibility(View.GONE);
                                List<ChuyenXe> chuyenXes= new ArrayList<>();

                                //tạo list chuyến bay sau đó render ra danh sách
                                int size= jsonArrayChuyenXe.length();
                                if (size==0) {
                                    tvKhongCoXe.setVisibility(View.VISIBLE);
                                }else
                                for (int i=0; i<size; i++){
                                    JSONObject objChuyenXe= jsonArrayChuyenXe.getJSONObject(i);
                                    int id = objChuyenXe.getInt("idAirport");
                                    Log.d("tx", "id: "+ id);
                                    String noiDi= objChuyenXe.getString("fromWhere");
                                    String noiDen= objChuyenXe.getString("toWhere");
                                    int soGheNgoi= objChuyenXe.getInt("numberOfSeat");
//                                    long tienXe= objChuyenXe.getLong("price");
                                    ChuyenXe chuyenXe= new ChuyenXe(id, noiDi, noiDen, soGheNgoi, 10000);
                                    chuyenXes.add(chuyenXe);
                                    ChuyenXeAdapter xeAdapter= new ChuyenXeAdapter(chuyenXes, getContext(), account);
                                    RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                                    revChuyenXe.setAdapter(xeAdapter);
                                    revChuyenXe.setLayoutManager(layoutManager);
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Đường truyền Internet không ổn định. Hãy thử tìm xe lại", Toast.LENGTH_SHORT).show();
                        tvKhongCoXe.setVisibility(View.VISIBLE);
                    }
                }){
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset = utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        if(requestBody==null) return null;
                        else {
                            try {
                                return requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    }
                };
                requestQueue.add(stringRequest);


                dialog.show();
            }
        });
        //nơi xe ddi
        binding.spnNoiXeDi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rNoiDi= noiXeDi[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapterNoiXeDi = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, noiXeDi);
        adapterNoiXeDi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnNoiXeDi.setAdapter(adapterNoiXeDi);

        //nơi xe đến
        binding.spnNoiXeDen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rNoiDen= noiXeDen[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapterNoiXeDen = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, noiXeDen);
        adapterNoiXeDen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnNoiXeDen.setAdapter(adapterNoiXeDen);


        //ngày đón xe
        binding.spnNgayDonXe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapterNgay = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, ngayDonXe);
        adapterNgay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnNgayDonXe.setAdapter(adapterNgay);

        //giờ đón xe
        binding.spnGio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapterGio = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, gioDonXe);
        adapterGio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnGio.setAdapter(adapterGio);

        //phút đón xe
        binding.spnPhut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapterPhut = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, phutDonXe);
        adapterPhut.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnPhut.setAdapter(adapterPhut);

        return binding.getRoot();
    }

}
