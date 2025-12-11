package com.example.grupo_04_tarea_16_ejercicio_01.ui.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grupo_04_tarea_16_ejercicio_01.MainActivity;
import com.example.grupo_04_tarea_16_ejercicio_01.databinding.ActivityLoginBinding;
import com.example.grupo_04_tarea_16_ejercicio_01.ui.Register.RegisterActivity;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_email, et_password;
    MaterialButton btn_login;
    TextView tv_register;

    private ActivityLoginBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtener SharedPreferences
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        InitialValues();
        InitialEvents();

        // Verificar si hay email registrado para autocompletar
        checkForRegisteredEmail();
    }

    private void checkForRegisteredEmail() {
        // Si viene de registro, autocompletar email
        String registeredEmail = getIntent().getStringExtra("registered_email");
        if (registeredEmail != null && !registeredEmail.isEmpty()) {
            et_email.setText(registeredEmail);
            et_password.requestFocus();
        }
    }

    private void InitialEvents() {
        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    private void InitialValues() {
        et_email = binding.etEmail;
        et_password = binding.etPassword;
        btn_login = binding.btnLogin;
        tv_register = binding.tvRegister;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.btnLogin.getId()) {
            String email = et_email.getText().toString().trim();
            String password = et_password.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor llene todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Primero verificar si hay credenciales registradas
            if (checkRegisteredCredentials(email, password)) {
                // Login exitoso con credenciales registradas
                loginSuccess(email);
            }
            // También mantener el usuario demo por si acaso
            else if (email.equals("usuario@correo.com") && password.equals("123456")) {
                // Login exitoso con usuario demo
                loginSuccess(email);
            } else {
                Toast.makeText(this, "Email o contraseña incorrectos, inténtelo de nuevo", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == binding.tvRegister.getId()) {
            // Navegar a la pantalla de registro
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    private boolean checkRegisteredCredentials(String email, String password) {
        String savedEmail = sharedPreferences.getString("registered_email", "");
        String savedPassword = sharedPreferences.getString("registered_password", "");

        // Verificar si el usuario está registrado
        boolean isRegistered = sharedPreferences.getBoolean("is_registered", false);

        if (isRegistered && email.equals(savedEmail) && password.equals(savedPassword)) {
            return true;
        }
        return false;
    }

    private void loginSuccess(String email) {
        Toast.makeText(this, "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show();

        // Guardar sesión actual si lo necesitas
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("current_user_email", email);
        editor.putBoolean("is_logged_in", true);
        editor.apply();

        // Ir a MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }
}