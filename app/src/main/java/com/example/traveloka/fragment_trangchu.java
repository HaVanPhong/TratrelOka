package com.example.traveloka;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traveloka.Adapter.ChuyenBayAdapter;
import com.example.traveloka.Constant.Constant;
import com.example.traveloka.Volley.Account;
import com.example.traveloka.Volley.ChuyenBay;
import com.example.traveloka.databinding.FragmentTrangchuBinding;

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
public class fragment_trangchu extends Fragment {
    final String URL= Constant.BASE_API;
    FragmentTrangchuBinding binding;
    String[] country = {"Hà Nội", "USA", "China", "Japan", "Other"};
    String[] country2 = {"Hồ Chí Minh", "China", "India", "Japan", "Other"};
    String[] hangGhe = {"Business", "Economic", "First Class", "Primium Economy"};
    String[] ngay = {"1", "2", "3", "4", "5", "6", "7"} ;
    MainActivity mainActivity;
    Account account;

    String rNoiDi = "Hà Nội";
    String rNoiDen = "Hồ Chí Minh";
    String rHangGhe = "Business";

    RequestQueue requestQueue;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trangchu, container, false);
        generateDate();
        mainActivity= (MainActivity) getActivity();
        account = mainActivity.getAccount();
        requestQueue= Volley.newRequestQueue(getContext());

        binding.LnTimChuyenBay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_tim_chuyen_bay);

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


                TextView tvBack= dialog.findViewById(R.id.tvDialogBack);
                Spinner spnNoiDi = dialog.findViewById(R.id.spnNoiDi);
                Spinner spnNoiDen = dialog.findViewById(R.id.spnNoiDen);
                Spinner spnNgayDi = dialog.findViewById(R.id.spnNgayDi);
                Spinner spnNgayDen = dialog.findViewById(R.id.spnNgayDen);
                Spinner spnHangGhe = dialog.findViewById(R.id.spnHangGhe);
                Switch  swKhuHoi= dialog.findViewById(R.id.swKhuHoi);
                LinearLayout lnNgayDen= dialog.findViewById(R.id.lnNgayDen);
                Button btnTimKiem= dialog.findViewById(R.id.btnTimKiem);
                TextView tvErr= dialog.findViewById(R.id.tvKhongCoChuyenBay);
                RecyclerView revChuyenBay= dialog.findViewById(R.id.revChuyenBay);

                //tìm kiếm

                tvBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                btnTimKiem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jsonObjectTimKiem= new JSONObject();
                        try {
                            jsonObjectTimKiem.put("fromWhere", rNoiDi);
                            jsonObjectTimKiem.put("toWhere", rNoiDen);
                            jsonObjectTimKiem.put("classSeat", rHangGhe);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String requestBody = jsonObjectTimKiem.toString();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL+ "/flights/findtravel", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject1= new JSONObject(response);
                                    int statusCode = jsonObject1.getInt("status");
                                    String message = jsonObject1.getString("message");

                                    if (statusCode==201 || statusCode==200){
                                        JSONArray jsonArrayChuyenBay= jsonObject1.getJSONArray("data");
                                        tvErr.setVisibility(View.GONE);
                                        revChuyenBay.setVisibility(View.VISIBLE);
                                        List<ChuyenBay> chuyenBays= new ArrayList<>();
                                        ChuyenBayAdapter bayAdapter= new ChuyenBayAdapter(chuyenBays, getContext(), account);
                                        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                                        //tạo list chuyến bay sau đó render ra danh sách
                                        int size= jsonArrayChuyenBay.length();
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
                                            bayAdapter.setupData(chuyenBays);
                                            revChuyenBay.setAdapter(bayAdapter);
                                            revChuyenBay.setLayoutManager(layoutManager);
                                        }
                                    }else {
                                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                        tvErr.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "Lỗi, Hãy thử lại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("r", "onErrorResponse: "+ error);
                                tvErr.setVisibility(View.VISIBLE);
                                Toast.makeText(getContext(), "Lỗi đường truyền, hãy thử tìm cbay lại", Toast.LENGTH_SHORT).show();
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
                    }
                });

                //switch khứ hồi
                swKhuHoi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (swKhuHoi.isChecked()){
                            lnNgayDen.setVisibility(View.VISIBLE);
                        }else {
                            lnNgayDen.setVisibility(View.GONE);
                        }
                    }
                });

                //nơi đi
                spnNoiDi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        rNoiDi = country[position].toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                ArrayAdapter adapterNoiDi = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, country);
                adapterNoiDi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnNoiDi.setAdapter(adapterNoiDi);

                //nơi đến
                spnNoiDen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        rNoiDen = country2[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                ArrayAdapter adapterNoiDen = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, country2);
                adapterNoiDen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnNoiDen.setAdapter(adapterNoiDen);

                //ngày đi
                spnNgayDi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //...
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                ArrayAdapter adapterNgayDi = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, ngay);
                adapterNgayDi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnNgayDi.setAdapter(adapterNgayDi);

                //ngày  đến
                spnNgayDen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //...
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                ArrayAdapter adapterNgayDen = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, ngay);
                adapterNgayDen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnNgayDen.setAdapter(adapterNgayDen);

                //Hạng ghế
                spnHangGhe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        rHangGhe= hangGhe[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                ArrayAdapter adapterHangGhe = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, hangGhe);
                adapterHangGhe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnHangGhe.setAdapter(adapterHangGhe);

                //

                dialog.show();
            }


        });

        return binding.getRoot();
    }

    private void generateDate() {
        Date date = new Date();
        LocalDate localDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year  = localDate.getYear();
            int month = localDate.getMonthValue();
            int day   = localDate.getDayOfMonth();
            for (int i=0; i<7; i++){
                ngay[i]= "Ngày "+ (day+i) + " Th "+ month+ " " + year;
            }
        }
    }
}
