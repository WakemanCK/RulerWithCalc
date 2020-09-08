package com.simpleruler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.simpleruler.MainActivity.decimalPlace;
import static com.simpleruler.MainActivity.guidingLines;
import static com.simpleruler.MainActivity.metricCM;
import static com.simpleruler.MainActivity.numberColor;
import static com.simpleruler.MainActivity.rulerColor;
import static com.simpleruler.MainActivity.rulerHead;
import static com.simpleruler.MainActivity.shortFormUnit;
import static com.simpleruler.MainActivity.hasSound;
import static com.simpleruler.MainActivity.thickLine;


public class Option extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels * 0.7);
        int height = (int) (displayMetrics.heightPixels * 0.09f * (13f - displayMetrics.heightPixels / displayMetrics.ydpi));
        getWindow().setLayout(width, height);

        // Init
        if (!String.valueOf(java.util.Locale.getDefault()).contains("zh")) {
            findViewById(R.id.shortFormUnitCheckBox).setVisibility(View.GONE);
        }
        CheckBox toggleHead = findViewById(R.id.headCheckBox);
        toggleHead.setChecked(rulerHead);
        CheckBox toggleSound = findViewById(R.id.soundCheckBox);
        toggleSound.setChecked(hasSound);
        CheckBox toggleThickLines = findViewById(R.id.thickLinesCheckBox);
        toggleThickLines.setChecked(thickLine);
        CheckBox toggleShortFormUnit = findViewById(R.id.shortFormUnitCheckBox);
        toggleShortFormUnit.setChecked(shortFormUnit);
        CheckBox toggleGuidingLines = findViewById(R.id.measurementLinesCheckBox);
        toggleGuidingLines.setChecked(guidingLines);
        // Color list
        final ListView colorList = findViewById(R.id.colorListView);
        colorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                rulerColor = getRulerColor(position);
                numberColor = getNumberColor(position);
                colorList.setVisibility(View.INVISIBLE);
                Intent intent = new Intent();
                setResult(10, intent);   // Request 1 result 5 = adjust decimal places
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        // Seek bar and metric buttons
        SeekBar seekBar = findViewById(R.id.decimalPlaceBar);
        seekBar.setProgress(decimalPlace);
        TextView decimalPlaceText = findViewById(R.id.decimalPlaceTextView);
        RadioButton cmRadioButton = findViewById(R.id.cmButton);
        RadioButton mmRadioButton = findViewById(R.id.mmButton);
        TextView metricText = findViewById(R.id.metricTextView);
        String s = (getString(R.string.decimalPlaceText) + decimalPlace);
        decimalPlaceText.setText(s);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                decimalPlace = progress;
                TextView textView = findViewById(R.id.decimalPlaceTextView);
                String s = (getString(R.string.decimalPlaceText) + decimalPlace);
                textView.setText(s);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Intent intent = new Intent();
                setResult(5, intent);   // Request 1 result 5 = adjust decimal places
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        if (metricCM) {
            cmRadioButton.setChecked(true);
        } else {
            mmRadioButton.setChecked(true);
        }
        if (guidingLines) {
            seekBar.setEnabled(true);
            decimalPlaceText.setTextColor(Color.BLACK);
            cmRadioButton.setEnabled(true);
            mmRadioButton.setEnabled(true);
            metricText.setTextColor(Color.BLACK);
        } else {
            seekBar.setEnabled(false);
            decimalPlaceText.setTextColor(Color.GRAY);
            cmRadioButton.setEnabled(false);
            mmRadioButton.setEnabled(false);
            metricText.setTextColor(Color.GRAY);
        }
    }

    public void clearAll(View view) {
        Intent intent = new Intent();
        setResult(7, intent);  // Request 1 result 7 = clear data
        finish();
    }

    private int getRulerColor(int getPosition) {
        int colorTemp = 0;
        switch (getPosition) {
            case 0:
                colorTemp = getResources().getColor(R.color.colorWhite);
                break;
            case 1:
                colorTemp = getResources().getColor(R.color.colorBlack);
                break;
            case 2:
                colorTemp = getResources().getColor(R.color.colorRed);
                break;
            case 3:
                colorTemp = getResources().getColor(R.color.colorYellow);
                break;
            case 4:
                colorTemp = getResources().getColor(R.color.colorBlue);
        }
        return colorTemp;
    }

    private int getNumberColor(int getPosition) {
        int colorTemp = 0;
        switch (getPosition) {
            case 0:
                colorTemp = getResources().getColor(R.color.colorBlack);
                break;
            case 1:
                colorTemp = getResources().getColor(R.color.colorWhite);
                break;
            case 2:
                colorTemp = getResources().getColor(R.color.colorYellow);
                break;
            case 3:
                colorTemp = getResources().getColor(R.color.colorBlack);
                break;
            case 4:
                colorTemp = getResources().getColor(R.color.colorYellow);
        }
        return colorTemp;
    }

    public void chooseColor(View view) {
        final ListView colorList = findViewById(R.id.colorListView);
        if (colorList.getVisibility() == View.INVISIBLE) {
            colorList.setVisibility(View.VISIBLE);
            String[] colorString = new String[5];
            for (int i = 0; i < 5; i++) {
                colorString[i] = getString(R.string.colorText) + (i + 1);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_1, colorString) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView textView = (TextView) super.getView(position, convertView, parent);
                    textView.setBackgroundColor(getRulerColor(position));
                    textView.setTextColor(getNumberColor(position));
                    return textView;
                }
            };
            colorList.setAdapter(adapter);
        } else {
            colorList.setVisibility(View.INVISIBLE);
        }
    }

    public void checkHead(View view) {
        CheckBox box = findViewById(R.id.headCheckBox);
        rulerHead = box.isChecked();
        Intent intent = new Intent();
        setResult(2, intent);    // Request 1 result 2 = switch head
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void checkSound(View view) {
        CheckBox box = findViewById(R.id.soundCheckBox);
        hasSound = box.isChecked();
    }

    public void checkThickLine(View view) {
        CheckBox box = findViewById(R.id.thickLinesCheckBox);
        thickLine = box.isChecked();
        Intent intent = new Intent();
        setResult(3, intent);   // Request 1 result 3 = switch lines thickness
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void checkShortForm(View view) {
        CheckBox box = findViewById(R.id.shortFormUnitCheckBox);
        shortFormUnit = box.isChecked();
        Intent intent = new Intent();
        setResult(8, intent);   // Request 1 result 8 = switch shortform unit
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void checkMeasurementLine(View view) {
        CheckBox box = findViewById(R.id.measurementLinesCheckBox);
        guidingLines = box.isChecked();
        Intent intent = new Intent();
        setResult(4, intent);   // Request 1 result 4 = switch guiding lines presence
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void chooseCM(View view) {
        metricCM = true;
        Intent intent = new Intent();
        setResult(6, intent);   // Request 1 result 6 = pick cm/mm
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void chooseMM(View view) {
        metricCM = false;
        Intent intent = new Intent();
        setResult(6, intent);   // Request 1 result 6 = pick cm/mm
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void goCalibrate(View view) {
        Intent intent = new Intent();
        setResult(1, intent);  // Request 1 result 1 = calibrate
        finish();
    }


    public void showResetPopup(View view) {
        Intent intent = new Intent(this, ResetPopup.class);
        startActivityForResult(intent, 2); // Reset popup Request Code = 2
    }

    public void showInformationPopup(View view) {
        Intent intent = new Intent(this, InformationPopup.class);
        startActivity(intent);
    }

    // Reset preferences
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(9, intent); // request 1 result 9 = reset
            finish();
        }
    }
}