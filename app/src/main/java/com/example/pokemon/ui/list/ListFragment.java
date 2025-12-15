package com.example.pokemon.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu; //Widget para el menu
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

        // Recibir usuario
        if (getArguments() != null) {
            currentUser = getArguments().getString("USERNAME_KEY");
            binding.tvWelcome.setText("Mochila de " + currentUser);
        }

        // Configurar Lista
        adapter = new PokemonAdapter();
        binding.rvPokemons.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvPokemons.setAdapter(adapter);

        // Configurar ViewModel
        viewModel = new ViewModelProvider(this).get(ListViewModel.class);
        viewModel.getUserPokemons(currentUser).observe(getViewLifecycleOwner(), pokemons -> {
            adapter.setPokemons(pokemons);

            int totalPower = 0;
            for (com.example.pokemon.data.local.PokemonEntity p : pokemons) {
                totalPower += p.getPower();
            }
            binding.tvTotalPower.setText("Poder de Equipo: " + totalPower);

            if (pokemons.isEmpty()) {
                binding.tvEmptyState.setVisibility(View.VISIBLE);
                binding.rvPokemons.setVisibility(View.GONE);
            } else {
                binding.tvEmptyState.setVisibility(View.GONE);
                binding.rvPokemons.setVisibility(View.VISIBLE);
            }
        });

        // Botón Buscar (Ir a captura)
        binding.btnSearch.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("USERNAME_KEY", currentUser);
            Navigation.findNavController(view).navigate(R.id.action_list_to_search, bundle);
        });

        //
        binding.btnSettings.setOnClickListener(v -> {
            //  Crear el menú Popup anclado al botón
            PopupMenu popup = new PopupMenu(getContext(), binding.btnSettings);

            //  Inflar el diseño del menú que creo
            popup.getMenuInflater().inflate(R.menu.list_menu, popup.getMenu());

            // Detectar clics en las opciones
            popup.setOnMenuItemClickListener(item -> {

                // Opción A: Cerrar Sesión
                if (item.getItemId() == R.id.menu_logout) {
                    Navigation.findNavController(view).popBackStack(R.id.loginFragment, false);
                    return true;
                }

                // Opción B: Borrar Cuenta
                else if (item.getItemId() == R.id.menu_delete) {
                    showDeleteConfirmation(view); // Llamamos a la función de abajo para no ensuciar esto
                    return true;
                }

                return false;
            });

            //  Mostrar el menú
            popup.show();
        });
    }

    // Función auxiliar para mostrar el diálogo de borrar
    private void showDeleteConfirmation(View view) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("¿Darse de baja?")
                .setMessage("Se borrará tu usuario y todos tus datos. Esta acción no se puede deshacer.")
                .setPositiveButton("BORRAR TODO", (dialog, which) -> {
                    viewModel.deleteAccount(currentUser, () -> {
                        if (getActivity() != null) {
                            android.widget.Toast.makeText(getContext(), "Cuenta eliminada", android.widget.Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).popBackStack(R.id.loginFragment, false);
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