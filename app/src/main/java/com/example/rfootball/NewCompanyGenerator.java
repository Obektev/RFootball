package com.example.rfootball;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;

import Utils.RFUtils;

public class NewCompanyGenerator {
    private static Typeface typeface;
    public interface DiadlogDoneInterface {
        void onDialogAgreed();
    }
    public static void openDialogMenu(Activity activity, DiadlogDoneInterface listener) {
        typeface = ResourcesCompat.getFont(activity, R.font.aldrich);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackground(AppCompatResources.getDrawable(activity, R.drawable.round_shape));

        TextView title = new TextView(activity);
        title.setTextSize(23);
        title.setTypeface(typeface);
        title.setGravity(Gravity.CENTER);
        title.setText(activity.getText(R.string.create_new_partisapatements));

        TextView seekBarTextView = new TextView(activity);
        seekBarTextView.setText(activity.getText(R.string.choose_number_of_teams));
        seekBarTextView.setTextSize(15);
        seekBarTextView.setTypeface(typeface);
        seekBarTextView.setGravity(Gravity.CENTER);

        ActionBar.LayoutParams seekBarParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80);
        seekBarParams.leftMargin = 5;
        seekBarParams.rightMargin = 5;
        seekBarParams.topMargin = 15;

        SeekBar seekBar = new SeekBar(activity);
        seekBar.setMax(10);
        seekBar.setLayoutParams(seekBarParams);

        TextView currentNumberTextView = new TextView(activity);
        currentNumberTextView.setTextSize(15);
        currentNumberTextView.setTypeface(typeface);
        currentNumberTextView.setGravity(Gravity.CENTER);
        currentNumberTextView.setText(String.format("%s: %s", activity.getString(R.string.current_number), 0));

        TextView anothWayTextView = new TextView(activity);
        anothWayTextView.setTextSize(15);
        anothWayTextView.setTypeface(typeface);
        anothWayTextView.setGravity(Gravity.CENTER);
        anothWayTextView.setText(activity.getString(R.string.or_choose_another_way));

        RelativeLayout.LayoutParams editTextParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editTextParams.setMargins(150, 5, 150, 0);
        EditText editText = new EditText(activity);
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        editText.setHint(activity.getString(R.string.input_text));
        editText.setLayoutParams(editTextParams);

        LinearLayout buttonLayout = new LinearLayout(activity);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER);

        AppCompatButton cancelButton = new AppCompatButton(activity);
        cancelButton.setTypeface(typeface);
        cancelButton.setText(activity.getString(R.string.cancel));
        cancelButton.setTextSize(15);
        cancelButton.setTranslationZ(3);
        cancelButton.setElevation(3);

        AppCompatButton createButton = new AppCompatButton(activity);
        createButton.setTypeface(typeface);
        createButton.setText(activity.getString(R.string.create));
        createButton.setTextSize(15);
        createButton.setElevation(3);
        createButton.setTranslationZ(3);

        buttonLayout.addView(cancelButton);
        buttonLayout.addView(createButton);

        ScrollView scrollView = new ScrollView(activity);
        ViewGroup.LayoutParams scrollViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,280);
        scrollView.setLayoutParams(scrollViewParams);

        LinearLayout.LayoutParams nameStackParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout namesStackLayout = new LinearLayout(activity);
        namesStackLayout.setOrientation(LinearLayout.VERTICAL);
        //namesStackLayout.setBackground(AppCompatResources.getDrawable(activity, R.drawable.round_shape));
        namesStackLayout.setGravity(Gravity.CENTER);
        namesStackLayout.setElevation(5);
        namesStackLayout.setTranslationZ(5);
        namesStackLayout.setLayoutParams(nameStackParams);

        scrollView.addView(namesStackLayout);

        layout.addView(seekBarTextView);
        layout.addView(currentNumberTextView);
        layout.addView(seekBar);
        layout.addView(anothWayTextView);
        layout.addView(editText);
        layout.addView(scrollView);
        layout.addView(buttonLayout);

        builder.setCustomTitle(title);
        builder.setView(layout);

        Dialog dialog = builder.create();
        dialog.show();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                currentNumberTextView.setText(String.format("%s: %s", activity.getString(R.string.current_number), i));
                addNameInput(namesStackLayout, i, activity);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty())
                    return;
                if (containsLetters(charSequence)) {
                    RFUtils.makeToast(activity, activity.getString(R.string.input_only_number));
                    return;
                }
                int progress = Integer.parseInt(charSequence.toString()) % 10;
                seekBar.setProgress(progress);
                addNameInput(namesStackLayout, progress, activity);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        cancelButton.setOnClickListener(view -> dialog.cancel());
        createButton.setOnClickListener(view ->
                generateCommandName(activity, commands ->
                        RFUtils.updateCommandsList(activity, commands, listener::onDialogAgreed)));
    }

    private static List<EditText> namesInputs =  new ArrayList<>();
    private static void addNameInput(LinearLayout namesStackLayout, int count_, Activity activity) {
        namesStackLayout.removeAllViews();
        namesInputs.clear();
        for (int i = 1; i <= count_; i++) {
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cardParams.leftMargin = 10;
            cardParams.rightMargin = 10;
            cardParams.topMargin = 4;
            cardParams.bottomMargin = 4;
            cardParams.gravity = Gravity.CENTER;

            CardView cardView = new CardView(activity);
            cardView.setBackground(AppCompatResources.getDrawable(activity, R.drawable.round_shape));
            cardView.setPadding(5,5,5,5);
            cardView.setLayoutParams(cardParams);
            cardView.setElevation(3);
            cardView.setTranslationZ(3);

            LinearLayout inputLayout = new LinearLayout(activity);
            inputLayout.setOrientation(LinearLayout.HORIZONTAL);
            inputLayout.setGravity(Gravity.START);

            TextView nameNumber = new TextView(activity);
            nameNumber.setTypeface(typeface);
            nameNumber.setTextSize(10);
            nameNumber.setText(String.format("%s: ", i));

            EditText commandNameInput = new EditText(activity);
            commandNameInput.setHint(activity.getString(R.string.input_command_name));
            commandNameInput.setTypeface(typeface);
            commandNameInput.setMaxLines(1);

            inputLayout.addView(nameNumber);
            inputLayout.addView(commandNameInput);

            cardView.addView(inputLayout);

            namesInputs.add(commandNameInput);
            namesStackLayout.addView(cardView);
        }
    }
    interface generationInterface {
        void onCommandsGot(List<String> commands);
    }
    private static void generateCommandName(Activity activity, generationInterface listener) {
        List<String> commands = new ArrayList<>();
        if (namesInputs.size() < 2) {
            RFUtils.makeToast(activity, activity.getString(R.string.null_commands));
            return;
        }
        for (EditText editText : namesInputs) {
            String name = editText.getText().toString();
            if (name.isEmpty()) {
                RFUtils.makeToast(activity, activity.getString(R.string.empty_name));
                return;
            }
            commands.add(name);
        }
        listener.onCommandsGot(commands);

    }
    public static boolean containsLetters(CharSequence charSequence) {
        for (int i = 0; i < charSequence.length(); i++) {
            char currentChar = charSequence.charAt(i);
            if (Character.isLetter(currentChar)) {
                return true;
            }
        }

        return false;
    }
}
