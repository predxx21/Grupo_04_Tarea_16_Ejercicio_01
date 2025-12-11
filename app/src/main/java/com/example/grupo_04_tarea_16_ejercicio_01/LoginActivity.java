package com.example.grupo_04_tarea_16_ejercicio_01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.grupo_04_tarea_16_ejercicio_01.databinding.ActivityLoginBinding;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity implements  View.OnClickListener {
EditText et_name_company,et_password;
MaterialButton btn_register_login;

 private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
     InitialValus();
     InitialEvents();
    }

    private void InitialEvents() {
        btn_register_login.setOnClickListener(this);
    }

    private void InitialValus() {
      et_name_company=binding.etNameCompany;
      et_password=binding.etPassword;
      btn_register_login=binding.btnRegisterLogin;
    }

    @Override
    public void onClick(View v) {
    if(v.getId()==binding.btnRegisterLogin.getId()){
    String nombre=et_name_company.getText().toString();
    String password=et_password.getText().toString();

        if(nombre.trim().isEmpty() || password.trim().isEmpty()){
            Toast.makeText(this,"Por favor llene todos los campos",Toast.LENGTH_SHORT).show();
        }else if(nombre.trim().equals("Empresa Predxx") && password.trim().equals("12345")){
            Intent intent=new Intent(this, MainActivity.class);
            intent.putExtra("nombre",nombre);
            intent.putExtra("password",password);
     startActivity(intent);

        }else{
            Toast.makeText(this,"Nombre de usuario o contraseña incorrectos, intentelo de nuevo",Toast.LENGTH_SHORT).show();
        }
    }
    }
}