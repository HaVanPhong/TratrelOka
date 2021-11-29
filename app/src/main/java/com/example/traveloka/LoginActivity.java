package com.example.traveloka;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traveloka.Constant.Constant;
import com.example.traveloka.Volley.Account;
import com.example.traveloka.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    RequestQueue requestQueue;
    final String URL= Constant.BASE_API;
    static final String SHARE_PRE_NAME="account";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //hiện sẵn tên đăng nhập và mật khẩu
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PRE_NAME,MODE_PRIVATE);
        String username = sharedPreferences.getString("username","");
        String password = sharedPreferences.getString("password","");
        binding.edtUsername.setText(username);
        binding.edtPassword.setText(password);


        binding.cbSavePassword.setChecked(true);

        requestQueue = Volley.newRequestQueue(LoginActivity.this);
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username= binding.edtUsername.getText().toString().trim();
                String password= binding.edtPassword.getText().toString().trim();

                if (binding.cbSavePassword.isChecked()){
                    SaveAccount(username, password);
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("account", username);
                    jsonObject.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String requestBody = jsonObject.toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL+ "/users/signin", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject1= new JSONObject(response);
                            int statusCode = jsonObject1.getInt("status");
                            if (statusCode==201 || statusCode==200){
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                String data= jsonObject1.getString("data");
                                Log.d("tx", "data: "+ data);
                                JSONObject jsonObject2= new JSONObject(data);
                                int id= jsonObject2.getInt("idUser");
                                Log.d("tx", "id gửi: "+ id);
                                Account account= new Account(id, username, password);
                                Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("account", (Parcelable) account);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Lỗi đường truyền, Hãy thử đn lại!", Toast.LENGTH_SHORT).show();
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
        binding.btnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialogLogin();
            }
        });
    }

    public void OpenDialogLogin(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_registration);
        Window window = dialog.getWindow();
        if (window == null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT); // set chiều rộng với chiều cao chứa dialog
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // set background ngoài dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        // xét tắt khi ấn bên ngoài dialog
        dialog.setCancelable(true);

        final EditText edt_new_username = dialog.findViewById(R.id.edt_new_username);
        final EditText edt_new_password = dialog.findViewById(R.id.edt_new_password);
        final EditText edt_enter_new_password = dialog.findViewById(R.id.edt_enter_new_password);
        Button btn_registration = dialog.findViewById(R.id.btn_registration);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);



        btn_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = edt_new_username.getText().toString();
                final String password = edt_new_password.getText().toString();
                final String rePassword = edt_enter_new_password.getText().toString();
                if (password.compareTo(rePassword)!=0){
                    Toast.makeText(LoginActivity.this, "Hãy nhập mật khẩu trùng khớp", Toast.LENGTH_SHORT).show();
                }else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("account", username);
                        jsonObject.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String requestBody = jsonObject.toString();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL+ "/users/signup", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject1= new JSONObject(response);
                                int statusCode = jsonObject1.getInt("status");
                                String message = jsonObject1.getString("message");
                                if (statusCode==201 || statusCode==200){
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }else {
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this, "Lỗi đường truyền: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
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
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void SaveAccount(String username, String password){
        SharedPreferences sharedPreferences=getSharedPreferences(SHARE_PRE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }

}