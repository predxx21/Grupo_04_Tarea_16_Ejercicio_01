package com.example.grupo_04_tarea_16_ejercicio_01.ui.Register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grupo_04_tarea_16_ejercicio_01.R;
import com.example.grupo_04_tarea_16_ejercicio_01.databinding.ActivityRegisterBinding;
import com.example.grupo_04_tarea_16_ejercicio_01.ui.Login.LoginActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityRegisterBinding binding;
    private EditText etFullName, etEmail, etPhone, etPassword, etConfirmPassword;
    private Spinner spinnerCountry;
    private ImageButton btnBack;
    private TextView tvGoLogin;
    private com.google.android.material.button.MaterialButton btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        initEvents();
        setupCountrySpinner();
    }

    private void initViews() {
        etFullName = binding.etFullname;
        etEmail = binding.etEmail;
        etPhone = binding.etPhone;
        etPassword = binding.etPassword;
        etConfirmPassword = binding.etConfirmPassword;
        spinnerCountry = binding.spinnerCountry;
        btnBack = binding.btnBack;
        tvGoLogin = binding.tvGoLogin;
        btnRegister = binding.btnRegister;
    }

    private void initEvents() {
        btnBack.setOnClickListener(this);
        tvGoLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    private void setupCountrySpinner() {
        String[] countries = {"Seleccione país", "Perú", "Argentina", "Chile", "Colombia", "México", "España", "Estados Unidos"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_back) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.tv_go_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.btn_register) {
            registerUser();
        }
    }

    private void registerUser() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String country = spinnerCountry.getSelectedItem().toString();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Por favor ingrese un email válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        if (country.equals("Seleccione país")) {
            Toast.makeText(this, "Por favor seleccione un país", Toast.LENGTH_SHORT).show();
            return;
        }

        saveUserData(email, password, fullName, phone, country);

        Toast.makeText(this, "¡Registro exitoso! Ahora puedes iniciar sesión", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("registered_email", email);
        startActivity(intent);
        finish();
    }

    private void saveUserData(String email, String password, String fullName, String phone, String country) {
        getSharedPreferences("user_prefs", MODE_PRIVATE)
                .edit()
                .putString("registered_email", email)
                .putString("registered_password", password)
                .putString("registered_fullname", fullName)
                .putString("registered_phone", phone)
                .putString("registered_country", country)
                .putBoolean("is_registered", true)
                .apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}