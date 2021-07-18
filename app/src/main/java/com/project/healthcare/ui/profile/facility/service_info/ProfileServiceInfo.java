package com.project.healthcare.ui.profile.facility.service_info;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.healthcare.R;
import com.project.healthcare.api.ApiCalls;
import com.project.healthcare.data.DialogData;
import com.project.healthcare.data.HealthFacility;
import com.project.healthcare.data.SpecialityType;
import com.project.healthcare.database.Database;
import com.project.healthcare.databinding.FragmentProfileServiceInfoBinding;
import com.project.healthcare.ui.MainActivityViewModel;
import com.project.healthcare.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import static com.project.healthcare.api.ApiCalls.CALL_ID_DELETE_USER;
import static com.project.healthcare.api.ApiCalls.CALL_ID_UPDATE_FACILITY;


public class ProfileServiceInfo extends Fragment implements Observer {
    private static final String TAG = "ProfileServiceInfo";
    private final RecyclerSelectedSpecialityTypeAdapter.SpecialityRemovedNotifier specialityRemovedNotifier = new RecyclerSelectedSpecialityTypeAdapter.SpecialityRemovedNotifier();
    private final RecyclerTypeOfSpecialityAdapter.SpecialityAddedNotifier specialityAddedNotifier = new RecyclerTypeOfSpecialityAdapter.SpecialityAddedNotifier();
    FragmentProfileServiceInfoBinding binding;
    MainActivityViewModel viewModel;
    RecyclerTypeOfSpecialityAdapter typeOfSpecialityAdapter;
    RecyclerSelectedSpecialityTypeAdapter selectedSpecialityTypeAdapter;
    ArrayList<SpecialityType> facType = new ArrayList<>(Arrays.asList(SpecialityType.values()));
    Dialog dialog;
    Utils.GeneralDialog generalDialog = new Utils.GeneralDialog();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        if (binding == null) {
            binding = FragmentProfileServiceInfoBinding.inflate(inflater, container, false);
        }
        dialog = Utils.buildProgressDialog(requireActivity());
        generalDialog.buildGeneralDialog(requireActivity(), new DialogData("", "", "OK", "", View.GONE));
        generalDialog.binding.footer.dialogBtnFooterPositive.setOnClickListener(v -> generalDialog.dialog.dismiss());
        viewModel.getBaseData().setTitleBarName("Facility Profile");
        specialityRemovedNotifier.addObserver(this);
        specialityAddedNotifier.addObserver(this);
        ApiCalls.getInstance().addObserver(this);
        attachAdapters();
        attachListeners();
        addTextWatchers();
        setData();
        return binding.getRoot();
    }

    private void addTextWatchers() {
        binding.about.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = s.toString();
                viewModel.getHealthFacility().setAbout(ss);
                validateAll();
                if (ss.length() > 10 || ss.isEmpty())
                    binding.about.txtError.setVisibility(View.GONE);
            }
        });
    }

    private boolean validateAll() {
        boolean success = true;
        if (viewModel.getHealthFacility().getAbout().length() < 10) {
            success = false;
            binding.about.txtError.setVisibility(View.VISIBLE);
            binding.about.txtError.setText("Please tell us a little bit more about establishment");
            if (binding.about.editText.getText().toString().isEmpty())
                binding.about.txtError.setVisibility(View.GONE);
        } else if (!checkSpecialityType()) {
            success = false;
        }
        if (success) {
            if (viewModel.getHealthFacility().getCompletedStages() < 3)
                viewModel.getHealthFacility().setCompletedStages(3);
            binding.cardUpdate.setCardBackgroundColor(requireActivity().getColor(R.color.blue));
        } else {
            viewModel.getHealthFacility().setCompletedStages(2);
            binding.cardUpdate.setCardBackgroundColor(requireActivity().getColor(R.color.light_blue));
        }
        return success;
    }


    private boolean checkSpecialityType() {
        return !viewModel.getHealthFacility().getSpecialities().isEmpty();
    }


    private void setData() {
        binding.about.editText.setText(viewModel.getHealthFacility().getAbout());
    }

    private void attachListeners() {
        binding.incMoveLeft.cardMoveLeft.setCardBackgroundColor(requireActivity().getColor(R.color.blue));
        binding.incMoveLeft.btnMoveLeft.setOnClickListener(v -> {
            viewModel.getSelectedBottomNumber().setValue(2);
        });
        binding.cardDelete.setOnClickListener(v -> attemptDelete());
        binding.cardUpdate.setOnClickListener(v -> {
            attemptUpdate();
        });
    }

    private void attemptUpdate() {
        if (validateAll()) {
            Log.d(TAG, "attemptUpdate: token : " + viewModel.getCitizen().getToken());
            String token = "Token ";
            token += ((HealthFacility) new Database(requireContext()).getUser()).getToken();
            ApiCalls.getInstance().updateFacility(token, viewModel.getHealthFacility());
            if (!dialog.isShowing())
                dialog.show();
        }
    }

    private void attemptDelete() {
        Utils.GeneralDialog gd = new Utils.GeneralDialog();
        gd.buildGeneralDialog(requireActivity(), new DialogData("Confirm", "Are you sure you want to delete your account?", "NO", "YES", View.VISIBLE));
        gd.binding.footer.dialogBtnFooterPositive.setOnClickListener(v -> {
            gd.dialog.dismiss();
        });
        gd.binding.footer.dialogBtnFooterNegative.setOnClickListener(v -> {
            String token = "Token ";
            token += ((HealthFacility) new Database(requireContext()).getUser()).getToken();
            ApiCalls.getInstance().deleteUser(token);
            if (!dialog.isShowing())
                dialog.show();
        });
        gd.dialog.show();

    }


    private void attachAdapters() {
        ArrayList<SpecialityType> type = new ArrayList<>();
        for (SpecialityType ft : facType) {
            boolean has = false;
            for (SpecialityType f : viewModel.getHealthFacility().getSpecialities()) {
                if (ft == f) {
                    has = true;
                    break;
                }
            }
            if (!has)
                type.add(ft);
        }
        typeOfSpecialityAdapter = new RecyclerTypeOfSpecialityAdapter(type, specialityAddedNotifier);
        selectedSpecialityTypeAdapter = new RecyclerSelectedSpecialityTypeAdapter(viewModel.getHealthFacility().getSpecialities(), specialityRemovedNotifier);
        binding.recyclerRegTypeOfSpeciality.setAdapter(typeOfSpecialityAdapter);
        binding.recyclerRegSelectedSpecialityType.setAdapter(selectedSpecialityTypeAdapter);
    }

    @Override
    public void update(Observable o, Object arg) {
        //Speciality Type added
        if (o instanceof RecyclerTypeOfSpecialityAdapter.SpecialityAddedNotifier) {
            selectedSpecialityTypeAdapter.getList().add((SpecialityType) arg);
            selectedSpecialityTypeAdapter.notifyDataSetChanged();
            typeOfSpecialityAdapter.getList().remove(arg);
            typeOfSpecialityAdapter.notifyDataSetChanged();
            viewModel.getHealthFacility().getSpecialities().add((SpecialityType) arg);
            validateAll();
        }

        //Speciality Type removed
        if (o instanceof RecyclerSelectedSpecialityTypeAdapter.SpecialityRemovedNotifier) {
            typeOfSpecialityAdapter.getList().add((SpecialityType) arg);
            typeOfSpecialityAdapter.notifyDataSetChanged();
            selectedSpecialityTypeAdapter.getList().remove(arg);
            selectedSpecialityTypeAdapter.notifyDataSetChanged();
            viewModel.getHealthFacility().getSpecialities().remove(arg);
            validateAll();
        }

        if (o instanceof ApiCalls) {
            if (dialog.isShowing())
                dialog.cancel();
            ApiCalls.ApiCallReturnObjects objs = (ApiCalls.ApiCallReturnObjects) arg;
            viewModel.getBaseData().setHomeProgressWheelVisibility(View.GONE);
            switch (objs.getCallId()) {
                case CALL_ID_UPDATE_FACILITY:
                    if (objs.isSuccess()) {
                        Database db = new Database(viewModel.getApplication().getApplicationContext());
                        viewModel.getHealthFacility().setToken(((HealthFacility) db.getUser()).getToken());
                        db.deleteUser();
                        db.insertUser(viewModel.getHealthFacility());
                    } else {

                    }
                    generalDialog.binding.getData().setTitleString(objs.getTitle());
                    generalDialog.binding.getData().setTextString(objs.getText());
                    generalDialog.dialog.setOnDismissListener(dialog -> {
                        dialog.dismiss();
                    });
                    generalDialog.dialog.show();
                    break;
                case CALL_ID_DELETE_USER:
                    if (objs.isSuccess()) {
                        //navigateToLogin();
                        Database db = new Database(requireContext());
                        db.deleteUser();
                        navigateToHome();
                    } else {

                    }
                    generalDialog.binding.getData().setTitleString(objs.getTitle());
                    generalDialog.binding.getData().setTextString(objs.getText());
//                    generalDialog.dialog.setOnDismissListener(dialog -> {});
                    generalDialog.dialog.show();
                    break;
            }
        }

    }

    private void navigateToHome() {
        Navigation.findNavController(binding.getRoot()).popBackStack(R.id.homeFragment, false);
    }
}