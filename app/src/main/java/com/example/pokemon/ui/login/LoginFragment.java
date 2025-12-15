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

import com.example.pokemon.R;
import com.example.pokemon.databinding.ActivityLoginBinding;

public class LoginFragment extends Fragment {

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
//Lógica de inico de sesión
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        viewModel.getLoginResult().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                String usuarioActual = binding.etUsername.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("USERNAME_KEY", usuarioActual);

                Navigation.findNavController(view).navigate(R.id.action_login_to_list, bundle);

                binding.etPassword.setText("");
            } else {
                Toast.makeText(getContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getRegisterMessage().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        binding.btnLogin.setOnClickListener(v -> {
            String u = binding.etUsername.getText().toString().trim();
            String p = binding.etPassword.getText().toString().trim();
            if (!u.isEmpty() && !p.isEmpty()) {
                viewModel.login(u, p);
            } else {
                Toast.makeText(getContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnRegister.setOnClickListener(v -> {
            // Ya no registramos aquí, solo navegamos a la pantalla de registro
            Navigation.findNavController(view).navigate(R.id.action_login_to_register);
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}