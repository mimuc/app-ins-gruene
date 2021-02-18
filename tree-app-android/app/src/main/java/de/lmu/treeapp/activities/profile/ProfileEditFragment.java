package de.lmu.treeapp.activities.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Random;

import de.lmu.treeapp.R;
import de.lmu.treeapp.components.DropDownView;
import de.lmu.treeapp.contentClasses.UserProfileCategory;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.database.AppDatabase;
import de.lmu.treeapp.contentData.database.daos.content.UserProfileDao;
import de.lmu.treeapp.contentData.database.entities.app.UserProfileState;
import de.lmu.treeapp.contentData.database.entities.content.UserProfileOption;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileEditFragment extends Fragment {

    public static String PROFILE_KEY = "profile";
    public static String EDIT_KEY = "edit";

    public UserProfileState userProfileState;
    public List<UserProfileOption> userProfileOptions;
    public boolean isEdit;

    public ProfileEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileEditFragment newInstance(UserProfileState user, boolean isEdit) {
        ProfileEditFragment profEditFrag = new ProfileEditFragment();
        Bundle args = new Bundle();

        args.putSerializable(PROFILE_KEY, user);
        args.putBoolean(EDIT_KEY, isEdit);
        profEditFrag.setArguments(args);

        return profEditFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = this.getArguments();
        this.isEdit = (boolean) args.getBoolean(EDIT_KEY);
        this.userProfileState = (UserProfileState) args.getSerializable(PROFILE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View profileEditView = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.fragment_profile_edit, container, false).getRoot();

        Toolbar toolbar = profileEditView.findViewById(R.id.profile_app_bar);
        toolbar.inflateMenu(R.menu.profile_edit_menu);

        if (isEdit) {
            toolbar.setTitle(R.string.profile_edit);
        } else {
            toolbar.setTitle(R.string.profile_create);
        }

        View inputWrapperName = profileEditView.findViewById(R.id.inputNameWrapper);
        TextInputEditText inputName = inputWrapperName.findViewById(R.id.profile_inputField);
        inputName.setText(userProfileState.name);

        View inputWrapperAge = profileEditView.findViewById(R.id.inputAgeWrapper);
        TextInputEditText inputAge = inputWrapperAge.findViewById(R.id.profile_inputField);
        inputAge.setText(userProfileState.age);
        inputAge.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Enabling the Back Button, even with different fragment manager
        profileEditView.setFocusableInTouchMode(true);
        profileEditView.requestFocus();
        profileEditView.setOnKeyListener((view1, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_BACK) {
                onClose();
                return true;
            }
            return false;
        });

        //random background image
        ImageView backgroundImage = profileEditView.findViewById(R.id.profile_bg);
        Random rand = new Random();
        int rng = rand.nextInt(10) + 1;
        switch (rng) {
            case 1:
                backgroundImage.setImageResource(R.drawable.ic_ahorn_baum);
                break;
            case 2:
                backgroundImage.setImageResource(R.drawable.ic_eberesche_baum);
                break;
            case 3:
                backgroundImage.setImageResource(R.drawable.ic_eiche_baum);
                break;
            case 4:
                backgroundImage.setImageResource(R.drawable.ic_fichte_baum);
                break;
            case 5:
                backgroundImage.setImageResource(R.drawable.ic_birke_baum);
                break;
            case 6:
                backgroundImage.setImageResource(R.drawable.ic_hasel_baum);
                break;
            case 7:
                backgroundImage.setImageResource(R.drawable.ic_linde_baum);
                break;
            case 8:
                backgroundImage.setImageResource(R.drawable.ic_buche_baum);
                break;
            case 9:
                backgroundImage.setImageResource(R.drawable.ic_tanne_baum);
                break;
            case 10:
                backgroundImage.setImageResource(R.drawable.ic_kiefer_baum);
                break;
        }

        DropDownView dropDownLocExposed = profileEditView.findViewById(R.id.spinnerLocExposed);
        DropDownView dropDownTreeExposed = profileEditView.findViewById(R.id.spinnerTreeExposed);
        DropDownView dropDownLeafExposed = profileEditView.findViewById(R.id.spinnerLeafExposed);
        DropDownView dropDownSeasonExposed = profileEditView.findViewById(R.id.spinnerSeasonExposed);
        DropDownView dropDownAvatarExposed = profileEditView.findViewById(R.id.spinnerAvatarExposed);

        DataManager.getInstance(getContext()).getUserProfileOptions().subscribe(userProfileOptions -> {
            this.userProfileOptions = userProfileOptions;

            UserProfileOption defaultOption = new UserProfileOption();
            defaultOption.position = -1;
            defaultOption.name = getResources().getString(R.string.please_select);
            defaultOption.imageResource = null;

            List<UserProfileOption> locationItems = UserProfileDao.getByCategory(UserProfileCategory.LOCATION, userProfileOptions);
            List<UserProfileOption> treeItems = UserProfileDao.getByCategory(UserProfileCategory.TREE, userProfileOptions);
            List<UserProfileOption> leafItems = UserProfileDao.getByCategory(UserProfileCategory.LEAF, userProfileOptions);
            List<UserProfileOption> seasonItems = UserProfileDao.getByCategory(UserProfileCategory.SEASON, userProfileOptions);
            List<UserProfileOption> avatarItems = UserProfileDao.getByCategory(UserProfileCategory.AVATAR, userProfileOptions);

            locationItems.add(0, defaultOption);
            treeItems.add(0, defaultOption);
            leafItems.add(0, defaultOption);
            seasonItems.add(0, defaultOption);
            avatarItems.add(0, defaultOption);

            ArrayAdapter<String> adapterLoc = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, mapUserProfileOptionsToString(locationItems));
            ArrayAdapter<String> adapterTree = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, mapUserProfileOptionsToString(treeItems));
            ArrayAdapter<String> adapterLeaf = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, mapUserProfileOptionsToString(leafItems));
            ArrayAdapter<String> adapterSeason = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, mapUserProfileOptionsToString(seasonItems));
            ArrayAdapter<String> adapterAvatar = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, mapUserProfileOptionsToString(avatarItems));

            dropDownLocExposed.setAdapter(adapterLoc);
            dropDownTreeExposed.setAdapter(adapterTree);
            dropDownLeafExposed.setAdapter(adapterLeaf);
            dropDownSeasonExposed.setAdapter(adapterSeason);
            dropDownAvatarExposed.setAdapter(adapterAvatar);

            // Set saved values
            if (userProfileState.location != null)
                dropDownLocExposed.setPosition(locationItems.indexOf(UserProfileDao.getByPositionAndCategory(userProfileState.location, UserProfileCategory.LOCATION, locationItems)));
            if (userProfileState.tree != null)
                dropDownTreeExposed.setPosition(treeItems.indexOf(UserProfileDao.getByPositionAndCategory(userProfileState.tree, UserProfileCategory.TREE, treeItems)));
            if (userProfileState.leaf != null)
                dropDownLeafExposed.setPosition(leafItems.indexOf(UserProfileDao.getByPositionAndCategory(userProfileState.leaf, UserProfileCategory.LEAF, leafItems)));
            if (userProfileState.season != null)
                dropDownSeasonExposed.setPosition(seasonItems.indexOf(UserProfileDao.getByPositionAndCategory(userProfileState.season, UserProfileCategory.SEASON, seasonItems)));
            if (userProfileState.avatar != null)
                dropDownAvatarExposed.setPosition(avatarItems.indexOf(UserProfileDao.getByPositionAndCategory(userProfileState.avatar, UserProfileCategory.AVATAR, avatarItems)));

            toolbar.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.profile_delete:
                        AppDatabase.getInstance(getContext()).userProfileDao().deleteOne(this.userProfileState).subscribeOn(Schedulers.io()).subscribe();
                        onClose();
                        return true;

                    case R.id.profile_save:
                        userProfileState.name = inputName.getText().toString();
                        if (userProfileState.name.isEmpty()) {
                            userProfileState.name = getResources().getString(R.string.profile_name_default_val);
                        }
                        userProfileState.age = inputAge.getText().toString();
                        if (userProfileState.age.isEmpty()) {
                            userProfileState.age = getResources().getString(R.string.profile_age_default_val);
                        }
                        userProfileState.location = (locationItems.get(dropDownLocExposed.getPosition())).position;
                        userProfileState.tree = (treeItems.get(dropDownTreeExposed.getPosition())).position;
                        userProfileState.leaf = (leafItems.get(dropDownLeafExposed.getPosition())).position;
                        userProfileState.season = (seasonItems.get(dropDownSeasonExposed.getPosition())).position;
                        userProfileState.avatar = (avatarItems.get(dropDownAvatarExposed.getPosition())).position;

                        onClose();
                        return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }
            });
        });

        return profileEditView;
    }

    private String[] mapUserProfileOptionsToString(List<UserProfileOption> options) {
        String[] strings = new String[options.size()];
        for (int i = 0; i < options.size(); i++) {
            strings[i] = options.get(i).name;
        }
        return strings;
    }

    private void onClose() {
        AppDatabase.getInstance(getContext()).userProfileDao().updateOne(this.userProfileState).subscribeOn(Schedulers.io()).subscribe(() -> {
            Intent intent = new Intent();
            intent.putExtra(ProfileSliderFragment.PAGER_USER_ID, userProfileState.id);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            getParentFragmentManager().popBackStack();
        });
    }
}
