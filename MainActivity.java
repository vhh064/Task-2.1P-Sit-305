package com.example.week1;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner unitCategorySpinner;
    private Spinner sourceUnitSpinner;
    private Spinner destinationUnitSpinner;
    private EditText valueToConvert;
    private Button convertButton;
    private TextView conversionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unitCategorySpinner = findViewById(R.id.unitCategorySpinner);
        sourceUnitSpinner = findViewById(R.id.sourceUnitSpinner);
        destinationUnitSpinner = findViewById(R.id.destinationUnitSpinner);
        valueToConvert = findViewById(R.id.valueToConvert);
        convertButton = findViewById(R.id.convertButton);
        conversionResult = findViewById(R.id.conversionResult);

        setupUnitCategorySpinner();
    }

    private void setupUnitCategorySpinner() {
        String[] categories = new String[]{"Temperature", "Length", "Weight"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        unitCategorySpinner.setAdapter(categoryAdapter);

        unitCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupUnitSpinners(categories[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Required method, but can be empty if not used.
            }
        });
    }

    private void setupUnitSpinners(String category) {
        String[] units;
        switch (category) {
            case "Temperature":
                units = new String[]{"Celsius", "Fahrenheit", "Kelvin"};
                break;
            case "Length":
                units = new String[]{"inch", "foot", "yard", "mile", "cm"};
                break;
            case "Weight":
                units = new String[]{"pound", "ounce", "ton" , "kg"};
                break;
            default:
                units = new String[]{};
                break;
        }
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units);
        sourceUnitSpinner.setAdapter(unitAdapter);
        destinationUnitSpinner.setAdapter(unitAdapter);

        convertButton.setOnClickListener(v -> performConversion(category));
    }

    private void performConversion(String category) {
        String sourceUnit = sourceUnitSpinner.getSelectedItem().toString();
        String destinationUnit = destinationUnitSpinner.getSelectedItem().toString();
        String inputValueStr = valueToConvert.getText().toString();
        double inputValue;
        double result = 0;
        boolean conversionSuccess = true;

        try {
            inputValue = Double.parseDouble(inputValueStr);
        } catch (NumberFormatException e) {
            Toast.makeText(MainActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (category) {
            case "Temperature":
                result = convertTemperature(sourceUnit, destinationUnit, inputValue);
                break;
            case "Length":
                result = convertLength(sourceUnit, destinationUnit, inputValue);
                break;
            case "Weight":
                result = convertWeight(sourceUnit, destinationUnit, inputValue);
                break;
            default:
                conversionSuccess = false;
                break;
        }

        if (conversionSuccess) {
            conversionResult.setText(String.format("Result: %.2f", result));
        } else {
            Toast.makeText(MainActivity.this, "Conversion not supported", Toast.LENGTH_SHORT).show();
        }
    }

    // Temperature conversion logic
    private double convertTemperature(String sourceUnit, String destinationUnit, double inputValue) {
        if ("Celsius".equals(sourceUnit) && "Fahrenheit".equals(destinationUnit)) {
            return (inputValue * 1.8) + 32;
        } else if ("Celsius".equals(sourceUnit) && "Kelvin".equals(destinationUnit)) {
            return inputValue + 273.15;
        } else if ("Fahrenheit".equals(sourceUnit) && "Celsius".equals(destinationUnit)) {
            return (inputValue - 32) / 1.8;
        } else if ("Fahrenheit".equals(sourceUnit) && "Kelvin".equals(destinationUnit)) {
            return ((inputValue - 32) / 1.8) + 273.15;
        } else if ("Kelvin".equals(sourceUnit) && "Celsius".equals(destinationUnit)) {
            return inputValue - 273.15;
        } else if ("Kelvin".equals(sourceUnit) && "Fahrenheit".equals(destinationUnit)) {
            return ((inputValue - 273.15) * 1.8) + 32;
        } else {
// This branch is for when the source unit equals the destination unit.
            return inputValue; // No conversion needed.
        }
    }

    private double convertLength(String sourceUnit, String destinationUnit, double inputValue) {
        double inCm = inputValue; // Assume inputValue is in cm if it doesn't need conversion.

        // Convert input to centimeters first, if necessary.
        switch (sourceUnit) {
            case "inch":
                inCm = inputValue * 2.54;
                break;
            case "foot":
                inCm = inputValue * 30.48;
                break;
            case "yard":
                inCm = inputValue * 91.44;
                break;
            case "mile":
                inCm = inputValue * 160934;
                break;
            case "cm":
                inCm = inputValue;
                break;
        }

        // Convert from cm to the destination unit.
        switch (destinationUnit) {
            case "inch":
                return inCm / 2.54;
            case "foot":
                return inCm / 30.48;
            case "yard":
                return inCm / 91.44;
            case "mile":
                return inCm / 160934;
            case "cm":
                return inCm; // No conversion needed if destination is cm.
            default:
                return inCm; // Default case returns the value in cm if the destination unit is unrecognized.
        }
    }


    private double convertWeight(String sourceUnit, String destinationUnit, double inputValue) {
        double inKg = inputValue; // Assume inputValue is in kg if it doesn't need conversion.

        // Convert input to kilograms first, if necessary.
        switch (sourceUnit) {
            case "pound":
                inKg = inputValue * 0.453592;
                break;
            case "ounce":
                inKg = inputValue * 0.0283495;
                break;
            case "ton":
                inKg = inputValue * 907.185;
                break;
            case "kg":
                inKg = inputValue; // No conversion needed if already in kg.
                break;
        }

        // Convert from kg to the destination unit.
        switch (destinationUnit) {
            case "pound":
                return inKg / 0.453592;
            case "ounce":
                return inKg / 0.0283495;
            case "ton":
                return inKg / 907.185;
            case "kg":
                return inKg;
            default:
                return inKg;
        }
    }
}
