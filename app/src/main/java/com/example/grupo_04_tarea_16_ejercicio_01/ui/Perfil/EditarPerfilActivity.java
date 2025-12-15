package com.example.grupo_04_tarea_16_ejercicio_01.ui.Perfil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grupo_04_tarea_16_ejercicio_01.R;

import com.example.grupo_04_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_04_tarea_16_ejercicio_01.model.Usuario;
import com.google.android.material.button.MaterialButton;

public class EditarPerfilActivity extends AppCompatActivity {

    private EditText etNombre, etCorreo, etCurrentPassword, etNewPassword, etConfirmPassword;
    private Spinner spinnerGrupo;
    private MaterialButton btnSave, btnCancel;
    private ImageButton btnBack;

    private DBHelper dbHelper;
    private Usuario currentUsuario;
    private int userId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        initViews();
        initDatabase();
        loadUsuarioData();
        setupGrupoSpinner();
        setupEventListeners();
    }

    private void initViews() {
        etNombre = findViewById(R.id.et_nombrescompletos);
        etCorreo = findViewById(R.id.et_correo);
        etCurrentPassword = findViewById(R.id.et_current_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        spinnerGrupo = findViewById(R.id.spinner_country_edit);
        btnSave = findViewById(R.id.btn_save_changes);
        btnCancel = findViewById(R.id.btn_cancel_edit);
        btnBack = findViewById(R.id.btn_perfil);

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
    }

    private void initDatabase() {
        dbHelper = new DBHelper(this);
        userId = getIntent().getIntExtra("user_id", -1);

        // Cargar datos del usuario desde SharedPreferences (ya que no tenemos método en DBHelper)
        if (userId != -1) {
            currentUsuario = new Usuario();
            currentUsuario.setId(userId);
            currentUsuario.setNombre(sharedPreferences.getString("user_name", ""));
            currentUsuario.setCorreo(sharedPreferences.getString("user_email", ""));
            currentUsuario.setGrupo(sharedPreferences.getString("user_group", "Grupo 04"));
            currentUsuario.setPassword(""); // La contraseña no está en SharedPreferences
        }
    }

    private void loadUsuarioData() {
        if (currentUsuario != null) {
            etNombre.setText(currentUsuario.getNombre());
            etCorreo.setText(currentUsuario.getCorreo());

            // El correo no debería ser editable normalmente
            etCorreo.setEnabled(false);
        }
    }

    private void setupGrupoSpinner() {
        String[] grupos = {"Grupo 01", "Grupo 02", "Grupo 03", "Grupo 04", "Grupo 05", "Grupo 06"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, grupos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupo.setAdapter(adapter);

        // Seleccionar el grupo actual del usuario
        if (currentUsuario != null && currentUsuario.getGrupo() != null) {
            for (int i = 0; i < grupos.length; i++) {
                if (grupos[i].equals(currentUsuario.getGrupo())) {
                    spinnerGrupo.setSelection(i);
                    break;
                }
            }
        }
    }

    private void setupEventListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

        btnCancel.setOnClickListener(v -> onBackPressed());

        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void saveChanges() {
        String nombre = etNombre.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String grupo = spinnerGrupo.getSelectedItem().toString();
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validaciones básicas
        if (nombre.isEmpty()) {
            etNombre.setError("Nombre requerido");
            return;
        }

        if (correo.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreo.setError("Correo electrónico válido requerido");
            return;
        }

        // Verificar si se quiere cambiar la contraseña
        boolean changePassword = !currentPassword.isEmpty() ||
                !newPassword.isEmpty() ||
                !confirmPassword.isEmpty();

        if (changePassword) {
            // Validar cambio de contraseña
            if (currentPassword.isEmpty()) {
                etCurrentPassword.setError("Contraseña actual requerida");
                return;
            }

            if (newPassword.isEmpty()) {
                etNewPassword.setError("Nueva contraseña requerida");
                return;
            }

            if (confirmPassword.isEmpty()) {
                etConfirmPassword.setError("Confirmar nueva contraseña");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                etConfirmPassword.setError("Las contraseñas no coinciden");
                return;
            }

            if (newPassword.length() < 6) {
                etNewPassword.setError("Mínimo 6 caracteres");
                return;
            }

            // Verificar contraseña actual
            // Necesitamos validar contra la base de datos
            Usuario authenticatedUsuario = dbHelper.Validar_Login(currentUsuario.getCorreo(), currentPassword);
            if (authenticatedUsuario == null) {
                etCurrentPassword.setError("Contraseña actual incorrecta");
                return;
            }

            // Actualizar contraseña
            // NOTA: Necesitarías un método en DBHelper para actualizar usuario
            // Por ahora, guardaremos en SharedPreferences y manejaremos en el login
            currentUsuario.setPassword(newPassword);
        }

        // Actualizar otros datos
        currentUsuario.setNombre(nombre);
        currentUsuario.setCorreo(correo);
        currentUsuario.setGrupo(grupo);

        // Guardar cambios en SharedPreferences (hasta que tengas método en DBHelper)
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_name", nombre);
        editor.putString("user_email", correo);
        editor.putString("user_group", grupo);

        // Si cambió la contraseña, guardarla también
        if (changePassword) {
            editor.putString("user_password", newPassword);
        }

        editor.apply();

        Toast.makeText(this, "Perfil actualizado exitosamente", Toast.LENGTH_SHORT).show();

        // Devolver resultado
        Intent resultIntent = new Intent();
        resultIntent.putExtra("profile_updated", true);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}