package com.example.tp3_ex2;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class AffichageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_affichage, container, false);

        TextView tvLogin = view.findViewById(R.id.tv_login);
        TextView tvNomPrenom = view.findViewById(R.id.tv_nom_prenom);
        ImageView ivGenre = view.findViewById(R.id.iv_genre);
        TextView tvDateNaissance = view.findViewById(R.id.tv_date_naissance);
        TextView tvTelephone = view.findViewById(R.id.tv_telephone);
        TextView tvEmail = view.findViewById(R.id.tv_email);
        LinearLayout layoutInterets = view.findViewById(R.id.layout_interets);
        view.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.gradient_background_blue_sky));
        AppDatabase.getInstance(getActivity()).userDao().getAllUsers().observe(getViewLifecycleOwner(), users -> {
            if (users != null && !users.isEmpty()) {
                User user = users.get(users.size() - 1);

                setUserInfoWithAnimation(tvLogin, user.login);
                setUserInfoWithAnimation(tvNomPrenom, user.nom + " " + user.prenom);
                setUserInfoWithAnimation(tvDateNaissance, user.dateNaissance);
                setUserInfoWithAnimation(tvTelephone, user.telephone);
                setUserInfoWithAnimation(tvEmail, user.email);

                setupGenderIcon(ivGenre, user.genre);

                displayInterests(inflater, layoutInterets, user.centresInteret);
            }
        });

        return view;
    }

    private void setUserInfoWithAnimation(TextView textView, String value) {
        textView.animate()
                .alpha(0)
                .setDuration(200)
                .withEndAction(() -> {
                    textView.setText(value);
                    textView.animate()
                            .alpha(1)
                            .setDuration(200)
                            .start();
                })
                .start();
    }

    private void setupGenderIcon(ImageView imageView, String gender) {
        if (gender.equals("Homme")) {
            imageView.setImageResource(R.drawable.ic_male);
            imageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_background_male));
        } else if (gender.equals("Femme")) {
            imageView.setImageResource(R.drawable.ic_female);
            imageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_background_female));
        }
        imageView.setScaleX(0);
        imageView.setScaleY(0);
        imageView.animate()
                .scaleX(1)
                .scaleY(1)
                .setDuration(500)
                .start();
    }

    private void displayInterests(LayoutInflater inflater, LinearLayout container, String interestsStr) {
        container.removeAllViews();
        String[] interests = interestsStr.split(" ");

        if (interests.length == 0 || (interests.length == 1 && interests[0].equals("Aucun"))) {
            addNoInterestsView(container);
        } else {
            for (int i = 0; i < interests.length; i++) {
                if (!interests[i].isEmpty() && !interests[i].equals("Aucun")) {
                    addInterestView(inflater, container, interests[i], i);
                }
            }
        }
    }

    private void addNoInterestsView(LinearLayout container) {
        TextView noInterestView = new TextView(getContext());
        noInterestView.setText("Aucun centre d'intérêt sélectionné");
        noInterestView.setTextSize(16);
        noInterestView.setTextColor(ContextCompat.getColor(getContext(), R.color.text_secondary));
        noInterestView.setPadding(32, 32, 32, 32);
        noInterestView.setGravity(Gravity.CENTER);
        container.addView(noInterestView);
    }

    private void addInterestView(LayoutInflater inflater, LinearLayout container, String interest, int position) {
        View interestView = inflater.inflate(R.layout.item_interet, container, false);
        ImageView ivInterest = interestView.findViewById(R.id.iv_interet);
        TextView tvInterest = interestView.findViewById(R.id.tv_interet);

        tvInterest.setText(interest);
        ivInterest.setImageResource(getInterestIcon(interest));
        ivInterest.setBackgroundColor(getInterestBackgroundColor(position));

        interestView.setAlpha(0);
        interestView.setTranslationY(20);
        interestView.animate()
                .alpha(1)
                .translationY(0)
                .setStartDelay(100 * position)
                .setDuration(300)
                .start();

        container.addView(interestView);
    }

    private int getInterestIcon(String interest) {
        switch (interest) {
            case "Sport": return R.drawable.ic_sport;
            case "Musique": return R.drawable.ic_music;
            case "Lecture": return R.drawable.ic_book;
            case "Voyage": return R.drawable.ic_travel;
            case "Cinéma": return R.drawable.ic_cinema;
            case "Cuisine": return R.drawable.ic_cooking;
            default: return R.drawable.ic_interests;
        }
    }

    private int getInterestBackgroundColor(int position) {
        int[] colors = {
                ContextCompat.getColor(getContext(), R.color.accent_blue),
                ContextCompat.getColor(getContext(), R.color.accent_green),
                ContextCompat.getColor(getContext(), R.color.accent_orange),
                ContextCompat.getColor(getContext(), R.color.accent_purple),
                ContextCompat.getColor(getContext(), R.color.accent_red),
                ContextCompat.getColor(getContext(), R.color.accent_teal)
        };
        return colors[position % colors.length];
    }
}