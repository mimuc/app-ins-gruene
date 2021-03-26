package de.lmu.treeapp.activities.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import java.util.List;
import java.util.Random;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.MainActivity;
import de.lmu.treeapp.contentClasses.UserProfileCategory;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.database.daos.content.UserProfileDao;
import de.lmu.treeapp.contentData.database.entities.app.UserProfileState;
import de.lmu.treeapp.contentData.database.entities.content.UserProfileOption;
import de.lmu.treeapp.tutorial.CustomTapTargetPromptBuilder;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence;

public class ProfileFragment extends Fragment {

    public static String PROFILE_KEY = "profile";

    public UserProfileState userProfileState;
    public List<UserProfileOption> userProfileOptions;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(UserProfileState user) {
        ProfileFragment profFrag = new ProfileFragment();
        Bundle args = new Bundle();

        args.putSerializable(PROFILE_KEY, user);
        profFrag.setArguments(args);

        return profFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = this.getArguments();
        this.userProfileState = (UserProfileState) args.getSerializable(PROFILE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.fragment_profile, container, false).getRoot();

        TextView treeTextView = view.findViewById(R.id.profileSlide_tree_text);
        ImageView treeImgView = view.findViewById(R.id.profileSlide_tree_Img);
        TextView userNameView = view.findViewById(R.id.profileSlide_name);
        TextView ageTextView = view.findViewById(R.id.profileSlide_age);
        TextView locationTextView = view.findViewById(R.id.profileSlide_location);
        ImageView locationFlagView = view.findViewById(R.id.profile_location_flag);
        TextView leafTextView = view.findViewById(R.id.profileSlide_leaf_text);
        ImageView leafImgView = view.findViewById(R.id.profileSlide_leaf_Img);
        TextView seasonTextView = view.findViewById(R.id.profileSlide_season_text);
        ImageView seasonImgView = view.findViewById(R.id.profileSlide_season_Img);
        TextView profileBubbleText = view.findViewById(R.id.profile_bubbleText);
        ImageView avatarImg = view.findViewById(R.id.profile_avatar_img);

        DataManager.getInstance(getContext()).getUserProfileOptions().observeOn(AndroidSchedulers.mainThread()).subscribe(userProfileOptions -> {
            this.userProfileOptions = userProfileOptions;

            Context context = getContext();

            // Check if context is still available on reinitialization
            if (context == null) return;

            if (this.userProfileState.name != null && this.userProfileState.age != null) {
                profileBubbleText.setText(context.getString(R.string.profile_bubble_text, this.userProfileState.name));
                userNameView.setText(this.userProfileState.name.substring(0, 1).toUpperCase() + this.userProfileState.name.substring(1).toLowerCase());
                ageTextView.setText(this.userProfileState.age);

                if(this.userProfileState.tree != -1){
                    treeTextView.setText(this.userProfileState.tree);
                }
                else{
                    treeTextView.setText("Ahorn");
                }

                if(this.userProfileState.leaf != -1){
                    leafTextView.setText(this.userProfileState.leaf);
                }
                else{
                    treeTextView.setText("Ahornblatt");
                }

                if(this.userProfileState.season != -1){
                    seasonTextView.setText(this.userProfileState.season);
                }
                else{
                    treeTextView.setText("Winter");
                }

                int defaultAvatar = R.drawable.ic_singleplayer_squirrel;
                if (this.userProfileState.avatar != null) {
                    UserProfileOption option = UserProfileDao.getByPositionAndCategory(userProfileState.avatar, UserProfileCategory.AVATAR, userProfileOptions);
                    if (option != null) {
                        defaultAvatar = getResources().getIdentifier(
                                option.imageResource, "drawable", getContext().getApplicationContext().getPackageName());
                    }
                }
                avatarImg.setImageResource(defaultAvatar);

                int defaultLocationFlag = R.drawable.ic_flag_germany;
                String defaultLocationName = context.getString(R.string.profile_location_default_val);
                if (this.userProfileState.location != null) {
                    UserProfileOption option = UserProfileDao.getByPositionAndCategory(userProfileState.location, UserProfileCategory.LOCATION, userProfileOptions);
                    if (option != null) {
                        defaultLocationFlag = getResources().getIdentifier(
                                option.imageResource, "drawable", getContext().getApplicationContext().getPackageName());
                        defaultLocationName = option.name;
                    }
                }
                locationFlagView.setImageResource(defaultLocationFlag);
                locationTextView.setText(defaultLocationName);

                int defaultTree = R.drawable.ic_ahorn_baum;
                if (this.userProfileState.tree != null) {
                    UserProfileOption option = UserProfileDao.getByPositionAndCategory(userProfileState.tree, UserProfileCategory.TREE, userProfileOptions);
                    if (option != null) {
                        defaultTree = getResources().getIdentifier(
                                option.imageResource, "drawable", getContext().getApplicationContext().getPackageName());
                    }
                }
                treeImgView.setImageResource(defaultTree);

                int defaultLeaf = R.drawable.ic_ahorn_blatt;
                if (this.userProfileState.leaf != null) {
                    UserProfileOption option = UserProfileDao.getByPositionAndCategory(userProfileState.leaf, UserProfileCategory.LEAF, userProfileOptions);
                    if (option != null) {
                        defaultLeaf = getResources().getIdentifier(
                                option.imageResource, "drawable", getContext().getApplicationContext().getPackageName());
                    }
                }
                leafImgView.setImageResource(defaultLeaf);

                int defaultSeason = R.drawable.ic_profil_seasons_spring;
                if (this.userProfileState.season != null) {
                    UserProfileOption option = UserProfileDao.getByPositionAndCategory(userProfileState.season, UserProfileCategory.SEASON, userProfileOptions);
                    if (option != null) {
                        defaultSeason = getResources().getIdentifier(
                                option.imageResource, "drawable", getContext().getApplicationContext().getPackageName());
                    }
                }
                seasonImgView.setImageResource(defaultSeason);

            } else {
                profileBubbleText.setText(R.string.profile_welcome);
                Random rand = new Random();
                int rngName = rand.nextInt(2);
                int rngAge = rand.nextInt(8) + 4;
                if (rngName == 0) {
                    userNameView.setText(R.string.profile_name_default_val_2);
                } else {
                    userNameView.setText(R.string.profile_name_default_val_3);
                }
                ageTextView.setText(String.valueOf(rngAge));

            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Boolean profile_show = preferences.getBoolean("profile_show", false);
        if (profile_show == false) {
            presentMaterialTapTargetSequence();
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("profile_show", true);
        editor.apply();
        return view;
    }

    public void presentMaterialTapTargetSequence() {
        final Toolbar tb = getActivity().findViewById(R.id.profile_app_bar);
        final View child = tb.getChildAt(1);
        if (child instanceof ActionMenuView) {
            final ActionMenuView actionMenuView = ((ActionMenuView) child);
            new MaterialTapTargetSequence()
                    .addPrompt(new CustomTapTargetPromptBuilder(getActivity())
                            .setTarget(R.id.profile_edit)
                            .setSecondaryText(R.string.profile_edit_text))
                    .addPrompt(new CustomTapTargetPromptBuilder(getActivity())
                            .setTarget(R.id.profile_add)
                            .setSecondaryText(R.string.profile_add_text))
                    .addPrompt(new CustomTapTargetPromptBuilder(getActivity())
                            .setTarget(actionMenuView.getChildAt(actionMenuView.getChildCount() - 1))
                            .setSecondaryText(R.string.profile_about)).show();
        }
    }

}

