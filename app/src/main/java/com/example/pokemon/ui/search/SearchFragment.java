package com.example.pokemon.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.pokemon.databinding.ActivitySearchBinding;

public class SearchFragment extends Fragment {

    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;
    private String currentUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ActivitySearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            currentUser = getArguments().getString("USERNAME_KEY");
        }

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        viewModel.loadMyTeam(currentUser);


        if (viewModel.getWildPokemon().getValue() == null) {
            viewModel.searchEnemy();
        }

        viewModel.getWildPokemon().observe(getViewLifecycleOwner(), pokemon -> {
            if (pokemon != null) {
                binding.tvWildName.setText(pokemon.getName());
                binding.tvWildPower.setText("Poder: " + pokemon.getStats().get(0).getBaseStat());
                Glide.with(this).load(pokemon.getSprites().getFrontDefault()).into(binding.ivWildImage);
                binding.btnCapture.setEnabled(true);
            }
        });

        viewModel.getMessage().observe(getViewLifecycleOwner(), msg ->
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show()
        );

        //Volver atras
        binding.btnBack.setOnClickListener(v -> {
            // popBackStack elimina la pantalla actual y vuelve a la anterior (Mochila)
            androidx.navigation.Navigation.findNavController(view).popBackStack();
        });
        binding.btnCapture.setOnClickListener(v -> viewModel.tryCapture(currentUser));
    }
}