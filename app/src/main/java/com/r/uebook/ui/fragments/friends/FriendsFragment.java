package com.r.uebook.ui.fragments.friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.r.uebook.R;
import com.r.uebook.data.remote.model.Friend;
import com.r.uebook.ui.adapters.FriendsListAdapter;
import com.r.uebook.ui.login_register.LoginActivity;
import com.r.uebook.utils.AppUtils;
import com.r.uebook.utils.ApplicationConstants;
import com.r.uebook.utils.Prefs;

import timber.log.Timber;

public class FriendsFragment extends Fragment {

    FriendsListAdapter adapter;
    RecyclerView recyclerView;
    Toolbar toolbar;
    Context context;
    private NavController navController;
    private FriendsViewModel mViewModel;

    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.friends_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        adapter.setOnItemClickListener(position -> {
            Friend friend = adapter.getItemAt(position);
            navController.navigate(FriendsFragmentDirections.actionFriendsFragmentToChatScreenFragment(friend));
        });
    }

    private void initViews(View view) {
        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.recyclerViewContact);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new FriendsListAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FriendsViewModel.class);
        String username = Prefs.getPreferences(ApplicationConstants.USERNAME);
        String password = Prefs.getPreferences(ApplicationConstants.PASSWORD);

        Timber.d("username :%s and password is %s", username, password);

        if (username != null && password != null) {
            mViewModel.getFriendsList(username, password).observe(getViewLifecycleOwner(), loginResponse -> {

                if (loginResponse != null) {
                    Timber.d("received friends list of size %s", loginResponse.friends.size());
                    adapter.setFriends(loginResponse.friends);
                }

            });
        }


        mViewModel.getErrorResponse().observe(getViewLifecycleOwner(), s ->
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show());


    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Timber.d("onMenuItemClick: search clicked");
                return true;

            case R.id.log_out:
                Timber.d("log out clicked");
                mViewModel.clearDatabase();
                AppUtils.loggedOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();


            default:
                return super.onOptionsItemSelected(item);

        }
    }


}