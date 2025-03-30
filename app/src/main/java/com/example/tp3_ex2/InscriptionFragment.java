package com.example.tp3_ex2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class InscriptionFragment extends Fragment {

    private EditText editLogin, editPassword, editNom, editPrenom, editDateNaissance, editPhone, editEmail;
    private TextInputLayout layoutPassword, layoutEmail, layoutPhone;
    private CheckBox checkSport, checkMusique, checkLecture, checkVoyage, checkCinema, checkCuisine;
    private Button btnSubmit;
    private ImageView icMale, icFemale;
    private ImageView selectedImage = null;
    private String selectedGender = "";
    private Calendar myCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inscription, container, false);

        editLogin = view.findViewById(R.id.et_login);
        editPassword = view.findViewById(R.id.et_password);
        editNom = view.findViewById(R.id.et_nom);
        editPrenom = view.findViewById(R.id.et_prenom);
        editDateNaissance = view.findViewById(R.id.et_date_naissance);
        layoutPhone = view.findViewById(R.id.layout_phone);
        layoutEmail = view.findViewById(R.id.layout_email);

        editPhone = layoutPhone.getEditText();
        editEmail = layoutEmail.getEditText();

        layoutPassword = view.findViewById(R.id.layout_password);
        layoutEmail = view.findViewById(R.id.layout_email);
        layoutPhone = view.findViewById(R.id.layout_phone);

        checkSport = view.findViewById(R.id.cb_sport);
        checkMusique = view.findViewById(R.id.cb_musique);
        checkLecture = view.findViewById(R.id.cb_lecture);
        checkVoyage = view.findViewById(R.id.cb_voyage);
        checkCinema = view.findViewById(R.id.cb_cinema);
        checkCuisine = view.findViewById(R.id.cb_cuisine);

        btnSubmit = view.findViewById(R.id.btn_soumettre);
        icMale = view.findViewById(R.id.ic_male);
        icFemale = view.findViewById(R.id.ic_female);

        DatePickerDialog.OnDateSetListener dateSetListener = (view1, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        };

        editDateNaissance.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(
                    getContext(),
                    R.style.CustomDatePickerDialogTheme,
                    dateSetListener,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDatePickerDialogTheme;
            dialog.show();
        });

        setupValidations();

        icMale.setOnClickListener(v -> onGenderSelected(icMale, "Homme", R.drawable.ic_male_red, R.drawable.ic_male));
        icFemale.setOnClickListener(v -> onGenderSelected(icFemale, "Femme", R.drawable.ic_female_red, R.drawable.ic_female));

        btnSubmit.setOnClickListener(v -> {
            validateAllFields(isLoginAvailable -> {
                if (isLoginAvailable) {
                    String login = editLogin.getText().toString().trim();
                    new Thread(() -> {
                        User user = new User(
                                login,
                                editPassword.getText().toString().trim(),
                                editNom.getText().toString().trim(),
                                editPrenom.getText().toString().trim(),
                                editDateNaissance.getText().toString().trim(),
                                editPhone.getText().toString().trim(),
                                editEmail.getText().toString().trim(),
                                getSelectedHobbies(),
                                selectedGender
                        );

                        AppDatabase.getInstance(getActivity()).userDao().insert(user);

                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Compte créé avec succès !", Toast.LENGTH_SHORT).show();

                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, new AffichageFragment());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        });
                    }).start();
                } else {
                    Toast.makeText(getContext(), "Veuillez corriger les erreurs dans le formulaire", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }

    private void updateDateLabel() {
        String format = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.FRANCE);
        editDateNaissance.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void setupValidations() {
        editLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String login = s.toString().trim();
                if (login.isEmpty()) {
                    editLogin.setError("Champ obligatoire");
                } else if (!login.matches("^[a-zA-Z].*")) {
                    editLogin.setError("Le login doit commencer par une lettre");
                } else if (login.length() > 10) {
                    editLogin.setError("Le login ne doit pas dépasser 10 caractères");
                } else {
                    editLogin.setError(null);
                }
            }
        });

        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 6) {
                    layoutPassword.setError("Le mot de passe doit contenir exactement 6 caractères");
                } else {
                    layoutPassword.setError(null);
                    layoutPassword.setErrorEnabled(false);
                }
            }
        });

        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    layoutEmail.setError("Format d'email invalide");
                } else {
                    layoutEmail.setError(null);
                    layoutEmail.setErrorEnabled(false);
                }
            }
        });

        editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Pattern phonePattern = Pattern.compile("^\\d{10}$");
                if (!phonePattern.matcher(s).matches()) {
                    layoutPhone.setError("Le numéro doit contenir 10 chiffres");
                } else {
                    layoutPhone.setError(null);
                    layoutPhone.setErrorEnabled(false);
                }
            }
        });
    }

    private void validateAllFields(LoginCheckCallback callback) {
        AtomicBoolean isValid = new AtomicBoolean(true);

        String login = editLogin.getText().toString().trim();
        if (login.isEmpty()) {
            editLogin.setError("Champ obligatoire");
            isValid.set(false);
        } else if (!login.matches("^[a-zA-Z].*")) {
            editLogin.setError("Le login doit commencer par une lettre");
            isValid.set(false);
        } else if (login.length() > 10) {
            editLogin.setError("Le login ne doit pas dépasser 10 caractères");
            isValid.set(false);
        } else {
            new Thread(() -> {
                int count = AppDatabase.getInstance(getActivity()).userDao().countByLogin(login);
                getActivity().runOnUiThread(() -> {
                    if (count > 0) {
                        editLogin.setError("Ce login est déjà utilisé");
                        isValid.set(false);
                    }
                    callback.onLoginChecked(isValid.get());
                });
            }).start();
            return;
        }

        if (editPassword.getText().toString().length() != 6) {
            layoutPassword.setError("Le mot de passe doit contenir exactement 6 caractères");
            isValid.set(false);
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString()).matches()) {
            layoutEmail.setError("Format d'email invalide");
            isValid.set(false);
        }

        Pattern phonePattern = Pattern.compile("^\\d{10}$");
        if (!phonePattern.matcher(editPhone.getText().toString()).matches()) {
            layoutPhone.setError("Le numéro doit contenir 10 chiffres");
            isValid.set(false);
        }

        if (selectedGender.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez sélectionner votre genre", Toast.LENGTH_SHORT).show();
            isValid.set(false);
        }

        callback.onLoginChecked(isValid.get());
    }

    private void onGenderSelected(ImageView clickedImage, String gender, int selectedDrawable, int defaultDrawable) {
        if (selectedImage == clickedImage) {
            clickedImage.setImageResource(defaultDrawable);
            selectedImage = null;
            selectedGender = "";
            Log.d("GenderSelection", "Deselected: " + gender);
        } else {
            if (selectedImage != null) {
                if (selectedImage == icMale) {
                    selectedImage.setImageResource(R.drawable.ic_male);
                } else if (selectedImage == icFemale) {
                    selectedImage.setImageResource(R.drawable.ic_female);
                }
                Log.d("GenderSelection", "Reset previous selection");
            }

            clickedImage.setImageResource(selectedDrawable);
            selectedImage = clickedImage;
            selectedGender = gender;
            Log.d("GenderSelection", "Selected: " + gender);
        }
    }

    private String getSelectedHobbies() {
        StringBuilder centresInteret = new StringBuilder();
        if (checkSport.isChecked()) centresInteret.append("Sport ");
        if (checkMusique.isChecked()) centresInteret.append("Musique ");
        if (checkLecture.isChecked()) centresInteret.append("Lecture ");
        if (checkVoyage.isChecked()) centresInteret.append("Voyage ");
        if (checkCinema.isChecked()) centresInteret.append("Cinéma ");
        if (checkCuisine.isChecked()) centresInteret.append("Cuisine ");

        return centresInteret.length() > 0 ? centresInteret.toString().trim() : "Aucun centre d'intérêt sélectionné";
    }
}