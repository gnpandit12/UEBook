package com.r.uebook.ui.fragments.chatprofile;

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
import com.r.uebook.data.remote.model.Friend;

import timber.log.Timber;

public class UserProfileFragment extends Fragment {
    private ImageButton btn_back;
    private TextView tv_username;
    private TextView tv_userEmail;
    private TextView tv_userPhone;
    private NavController navController;
    private UserProfileViewModel mViewModel;

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_profile_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        if (getArguments() != null) {
            UserProfileFragmentArgs fragmentArgs = UserProfileFragmentArgs.fromBundle(getArguments());
            Timber.d("received arguments: %s", fragmentArgs.getFriendProfile());
            Friend friendProfile = fragmentArgs.getFriendProfile();
            tv_username.setText(friendProfile.getUserName());
            tv_userEmail.setText(friendProfile.getUserMail());
            tv_userPhone.setText(friendProfile.getUserContactNo().toString());


            btn_back.setOnClickListener(v ->
                    navController.popBackStack()
//                    navController.navigate(UserProfileFragmentDirections.actionProfileFragmentToChatScreenFragment(friendProfile))
            );
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);

    }

    private void initViews(View view) {
        navController = Navigation.findNavController(view);
        btn_back = view.findViewById(R.id.profile_back_btn);
        tv_username = view.findViewById(R.id.user_profile_name_tv);
        tv_userEmail = view.findViewById(R.id.user_profile_email_tv);
        tv_userPhone = view.findViewById(R.id.user_profile_phone_no_tv);
    }

}