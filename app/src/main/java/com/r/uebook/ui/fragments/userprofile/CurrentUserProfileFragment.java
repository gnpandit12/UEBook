package com.r.uebook.ui.fragments.userprofile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.r.uebook.R;
import com.r.uebook.data.remote.model.UserProfile;

import timber.log.Timber;

public class CurrentUserProfileFragment extends Fragment {

    private ImageButton btn_back;
    private TextView tv_username;
    private TextView tv_userEmail;
    private TextView tv_userPhone;
    private NavController navController;
    private CurrentUserProfileViewModel mViewModel;

    public static CurrentUserProfileFragment newInstance() {
        return new CurrentUserProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.current_user_profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        if (getArguments() != null) {
            CurrentUserProfileFragmentArgs fragmentArgs = CurrentUserProfileFragmentArgs.fromBundle(getArguments());
            Timber.d("received arguments: %s", fragmentArgs.getCurrentUser());
            UserProfile userProfile = fragmentArgs.getCurrentUser();
            tv_username.setText(userProfile.getUserName());
            tv_userEmail.setText(userProfile.getUserMail());
            tv_userPhone.setText(userProfile.getUserContactNo().toString());

        }

        btn_back.setOnClickListener(v ->
                        navController.popBackStack()
//                navController.navigate(CurrentUserProfileFragmentDirections.actionCurrentUserProfileFragmentToChatFragment())
        );

    }

    private void initViews(View view) {
        navController = Navigation.findNavController(view);
        btn_back = view.findViewById(R.id.profile_back_btn);
        tv_username = view.findViewById(R.id.profile_name_tv);
        tv_userEmail = view.findViewById(R.id.profile_email_tv);
        tv_userPhone = view.findViewById(R.id.profile_phone_no_tv);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CurrentUserProfileViewModel.class);

    }

}