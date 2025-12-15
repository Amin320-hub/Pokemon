package com.example.pokemon.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pokemon.R;
import com.example.pokemon.databinding.ActivityListBinding;

public class ListFragment extends Fragment {

    private ActivityListBinding binding;
    private String currentUser;
    private ListViewModel viewModel;
    private PokemonAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //  Obtener usuario actual
        if (getArguments() != null) {
            currentUser = getArguments().getString("USERNAME_KEY");
            binding.tvWelcome.setText("Mochila de " + currentUser);
        }

        //  Configurar RecyclerView
        adapter = new PokemonAdapter();
        binding.rvPokemons.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvPokemons.setAdapter(adapter);

        //  Configurar ViewModel
        viewModel = new ViewModelProvider(this).get(ListViewModel.class);
        viewModel.getUserPokemons(currentUser).observe(getViewLifecycleOwner(), pokemons -> {
            adapter.setPokemons(pokemons);

            // Calcular poder total
            int totalPower = 0;
            for (com.example.pokemon.data.local.PokemonEntity p : pokemons) {
                totalPower += p.getPower();
            }
            binding.tvTotalPower.setText("Poder de Equipo: " + totalPower);

            // Manejo de lista vacía
            if (pokemons.isEmpty()) {
                binding.tvEmptyState.setVisibility(View.VISIBLE);
                binding.rvPokemons.setVisibility(View.GONE);
            } else {
                binding.tvEmptyState.setVisibility(View.GONE);
                binding.rvPokemons.setVisibility(View.VISIBLE);
            }
        });

        //  Botón Buscar
        binding.btnSearch.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("USERNAME_KEY", currentUser);
            Navigation.findNavController(view).navigate(R.id.action_list_to_search, bundle);
        });

        //  Botón Ajustes (Menú Desplegable)
        binding.btnSettings.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getContext(), binding.btnSettings);

            popup.getMenuInflater().inflate(R.menu.list_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {

                // OPCIÓN A: CERRAR SESIÓN
                if (item.getItemId() == R.id.menu_logout) {
                    // CORRECCIÓN: Usamos navigate en vez de popBackStack
                    Navigation.findNavController(view).navigate(R.id.loginFragment);
                    return true;
                }

                // OPCIÓN B: BORRAR CUENTA
                else if (item.getItemId() == R.id.menu_delete) {
                    showDeleteConfirmation(view);
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    // Diálogo de confirmación para borrar
    private void showDeleteConfirmation(View view) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("¿Darse de baja?")
                .setMessage("Se borrará tu usuario y todos tus datos. Esta acción no se puede deshacer.")
                .setPositiveButton("BORRAR TODO", (dialog, which) -> {

                    // Llamada al ViewModel
                    viewModel.deleteAccount(currentUser, () -> {
                        if (getActivity() != null) {
                            android.widget.Toast.makeText(getContext(), "Cuenta eliminada", android.widget.Toast.LENGTH_SHORT).show();
                            // Navegar explícitamente al login
                            Navigation.findNavController(view).navigate(R.id.loginFragment);
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}