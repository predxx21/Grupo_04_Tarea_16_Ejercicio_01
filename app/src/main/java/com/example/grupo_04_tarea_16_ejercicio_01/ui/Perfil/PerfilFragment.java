package com.example.grupo_04_tarea_16_ejercicio_01.ui.Perfil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.grupo_04_tarea_16_ejercicio_01.R;
import com.example.grupo_04_tarea_16_ejercicio_01.db.DBHelper;
import com.example.grupo_04_tarea_16_ejercicio_01.model.Experiencia;
import com.example.grupo_04_tarea_16_ejercicio_01.model.Usuario;
import com.example.grupo_04_tarea_16_ejercicio_01.ui.Login.LoginActivity;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class PerfilFragment extends Fragment {

    // Vistas
    private ImageView imgProfilePhoto;
    private TextView tvNombreCompleto, tvEmail, tvGrupo, tvFechaRegistro;
    private TextView tvLugaresVisitados, tvFotosTomadas, tvAudiosGrabados;
    private TextView tvChangePhoto, tvPais; // Agregamos tvPais aquí
    private MaterialButton btnEditarPerfil, btnCerrarSesion;

    // Variables lógicas
    private Uri photoUri;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private Usuario currentUsuario;
    private int userId;

    // Launchers
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String[]> permissionLauncher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Inicializamos TODO usando la vista inflada 'view'
        initViews(view);
        initDatabase();
        initActivityResultLaunchers();
        loadUserData();
        loadUserStatistics();
        setupEventListeners();
        loadProfilePhoto();

        return view;
    }

    private void initViews(View view) {
        // Vinculación de IDs (Asegúrate que estos IDs existan en tu XML)
        imgProfilePhoto = view.findViewById(R.id.img_profile_photo);
        tvNombreCompleto = view.findViewById(R.id.tv_nombre_completo);
        tvEmail = view.findViewById(R.id.tv_email);
        tvFechaRegistro = view.findViewById(R.id.tv_fecha_registro);

        // Estadísticas
        tvLugaresVisitados = view.findViewById(R.id.tv_lugares_visitados);
        tvFotosTomadas = view.findViewById(R.id.tv_fotos_tomadas);
        tvAudiosGrabados = view.findViewById(R.id.tv_audios_grabados);

        // Botones y acciones
        tvChangePhoto = view.findViewById(R.id.tv_change_photo);
        btnEditarPerfil = view.findViewById(R.id.btn_editar_perfil);
        // OJO: Faltaba inicializar btnCerrarSesion en tu código original
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);

        // CORRECCIÓN: Inicializamos tvPais aquí, no en loadUserData
        tvPais = view.findViewById(R.id.tv_pais);
    }

    private void initDatabase() {
        dbHelper = new DBHelper(getContext());
        // Usamos getActivity() con verificación de nulos es más seguro
        if (getActivity() != null) {
            sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            userId = sharedPreferences.getInt("user_id", -1);
            String userEmail = sharedPreferences.getString("user_email", "");

            if (!userEmail.isEmpty()) {
                currentUsuario = new Usuario();
                currentUsuario.setId(userId);
                currentUsuario.setNombre(sharedPreferences.getString("user_name", "Usuario"));
                currentUsuario.setCorreo(userEmail);
                currentUsuario.setGrupo(sharedPreferences.getString("user_group", "Grupo 04"));
            }
        }
    }

    private void initActivityResultLaunchers() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        if (selectedImage != null) {
                            setProfilePhoto(selectedImage);
                            savePhotoUri(selectedImage.toString());
                        }
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && photoUri != null) {
                        setProfilePhoto(photoUri);
                        savePhotoUri(photoUri.toString());
                    }
                }
        );

        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    Boolean cameraGranted = permissions.getOrDefault(Manifest.permission.CAMERA, false);
                    Boolean storageGranted;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        storageGranted = permissions.getOrDefault(Manifest.permission.READ_MEDIA_IMAGES, false);
                    } else {
                        storageGranted = permissions.getOrDefault(Manifest.permission.READ_EXTERNAL_STORAGE, false);
                    }

                    if (Boolean.TRUE.equals(cameraGranted) && Boolean.TRUE.equals(storageGranted)) {
                        showImagePickerDialog();
                    } else {
                        Toast.makeText(getContext(), "Permisos necesarios", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void loadUserData() {
        // CORRECCIÓN: Usamos las variables de clase ya inicializadas
        String fechaActual = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES")).format(new Date());

        if (currentUsuario == null) {
            showDefaultData();
            return;
        }

        // Validamos que las vistas no sean nulas antes de setear texto
        if (tvNombreCompleto != null) tvNombreCompleto.setText(currentUsuario.getNombre());
        if (tvEmail != null) tvEmail.setText(currentUsuario.getCorreo());

        // CORRECCIÓN: Usamos la variable tvPais inicializada en initViews
        if (tvPais != null) {
            tvPais.setText(currentUsuario.getGrupo() != null ? currentUsuario.getGrupo() : "Grupo no especificado");
        }

        if (tvFechaRegistro != null) tvFechaRegistro.setText(fechaActual);
    }

    private void loadUserStatistics() {
        // Validamos vistas nulas para evitar crashes si el XML no tiene estadísticas
        if (tvLugaresVisitados == null || tvFotosTomadas == null || tvAudiosGrabados == null) return;

        if (currentUsuario == null || userId == -1) {
            setStatsZero();
            return;
        }

        ArrayList<Experiencia> experiencias = dbHelper.Obtener_Experiencias_Por_Usuario(userId);

        if (experiencias != null) {
            int fotosCount = experiencias.size();
            Set<String> lugaresUnicos = new HashSet<>();
            for (Experiencia exp : experiencias) {
                String lugarKey = exp.getLatitud() + "," + exp.getLongitud();
                lugaresUnicos.add(lugarKey);
            }
            int lugaresCount = lugaresUnicos.size();

            tvLugaresVisitados.setText(String.valueOf(lugaresCount));
            tvFotosTomadas.setText(String.valueOf(fotosCount));
            tvAudiosGrabados.setText("0"); // Placeholder
        } else {
            setStatsZero();
        }
    }

    private void setStatsZero() {
        if(tvLugaresVisitados != null) tvLugaresVisitados.setText("0");
        if(tvFotosTomadas != null) tvFotosTomadas.setText("0");
        if(tvAudiosGrabados != null) tvAudiosGrabados.setText("0");
    }

    private void showDefaultData() {
        if (tvNombreCompleto != null) tvNombreCompleto.setText("Usuario");
        if (tvEmail != null) tvEmail.setText("usuario@ejemplo.com");
        if (tvPais != null) tvPais.setText("Grupo 04");

        String fechaActual = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES")).format(new Date());
        if (tvFechaRegistro != null) tvFechaRegistro.setText(fechaActual);
    }

    private void loadProfilePhoto() {
        if (sharedPreferences == null || imgProfilePhoto == null) return;

        String userEmail = sharedPreferences.getString("user_email", "");
        if (userEmail.isEmpty()) return;

        String savedPhotoUri = sharedPreferences.getString("profile_photo_" + userEmail, null);

        if (savedPhotoUri != null) {
            try {
                photoUri = Uri.parse(savedPhotoUri);
                imgProfilePhoto.setImageURI(photoUri);
                imgProfilePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } catch (Exception e) {
                e.printStackTrace();
                imgProfilePhoto.setImageResource(R.drawable.ic_person); // Asegúrate que este drawable exista
            }
        } else {
            // Recurso por defecto
            // imgProfilePhoto.setImageResource(R.drawable.ic_person);
        }
    }

    private void savePhotoUri(String uri) {
        String userEmail = sharedPreferences.getString("user_email", "");
        if (!userEmail.isEmpty()) {
            sharedPreferences.edit().putString("profile_photo_" + userEmail, uri).apply();
        }
    }

    private void setProfilePhoto(Uri uri) {
        try {
            imgProfilePhoto.setImageURI(uri);
            Toast.makeText(getContext(), "Foto actualizada", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            imgProfilePhoto.setImageResource(R.drawable.ic_person);
        }
    }

    private void setupEventListeners() {
        if (tvChangePhoto != null) {
            tvChangePhoto.setOnClickListener(v -> checkPermissionsAndPickImage());
        }
        if (btnEditarPerfil != null) {
            btnEditarPerfil.setOnClickListener(v -> navigateToEditProfile());
        }
        if (btnCerrarSesion != null) {
            btnCerrarSesion.setOnClickListener(v -> logoutUser());
        }
    }

    // ... (El resto de métodos checkPermissionsAndPickImage, showImagePickerDialog,
    // openCamera, openGallery se mantienen igual que en tu código original) ...

    // Método abreviado para no repetir todo el código de permisos/cámara aquí,
    // asumo que los tienes bien en tu archivo original.

    private void checkPermissionsAndPickImage() {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES};
        } else {
            permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        // ... lógica de permisos ...
        showImagePickerDialog(); // Simplificado para la respuesta
    }

    private void showImagePickerDialog() {
        // ... tu código original ...
        openCamera(); // Placeholder
    }

    private void openCamera() {
        // ... tu código original ...
    }

    private void openGallery() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(pickPhotoIntent);
    }

    private void navigateToEditProfile() {
        if (currentUsuario == null) {
            Toast.makeText(getContext(), "Inicia sesión primero", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getContext(), EditarPerfilActivity.class);
        intent.putExtra("user_id", currentUsuario.getId());
        startActivity(intent);
    }

    private void logoutUser() {
        // ... tu código original de logout ...
        performLogout();
    }

    private void performLogout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Limpia todo
        editor.apply();

        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        initDatabase(); // Recargar datos al volver de editar
        loadUserData();
        loadUserStatistics();
    }
}