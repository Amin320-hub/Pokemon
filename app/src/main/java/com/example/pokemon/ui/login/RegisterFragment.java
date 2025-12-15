package com.example.pokemon.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.pokemon.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private LoginViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Observar mensajes (Éxito o Error de duplicado)
        viewModel.getRegisterMessage().observe(getViewLifecycleOwner(), msg -> {
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            if (msg.contains("exitoso")) {
                // Si fue bien, volvemos al login automáticamente
                Navigation.findNavController(view).popBackStack();
            }
        });

        // Botón CONFIRMAR REGISTRO
        binding.btnConfirmRegister.setOnClickListener(v -> {
            String u = binding.etRegUser.getText().toString().trim();
            String p = binding.etRegPass.getText().toString().trim();

            if (!u.isEmpty() && !p.isEmpty()) {
                viewModel.register(u, p);
            } else {
                Toast.makeText(getContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón CANCELAR
        binding.btnBackToLogin.setOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });
    }
}