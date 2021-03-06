package com.example.quizletclone;

import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class CheckAllThatApply extends AppCompatActivity {
    private ModelViewController mvc;
    private Button createQuestion;
    private EditText questionField, fieldA, fieldB, fieldC, fieldD, fieldE;
    private CheckBox checkBoxA, checkBoxB, checkBoxC, checkBoxD, checkBoxE;
    private Spinner spinner;
    private RelativeLayout layout;
    public static final String CATEGORY = "Check All That Apply", PLACEHOLDER = "   ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_all_that_apply);

        /* Retrieve the ModelViewController object from the calling class */
        mvc = ModelViewController.getInstance(this);

        setTitle("Check all that apply");

        initializeGUIComponents();
        createFlashcardObject();
    }

     protected void hideKeyboard(View view) {
         InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
         in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
     }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /** Initialize all the graphical user interface elements. */
    private void initializeGUIComponents() {
        layout = (RelativeLayout)findViewById(R.id.layout);
        createQuestion = (Button)findViewById(R.id.createFlashcard);
        questionField = (EditText)findViewById(R.id.questionField);
        checkBoxA = (CheckBox)findViewById(R.id.checkboxA);
        checkBoxB = (CheckBox)findViewById(R.id.checkboxB);
        checkBoxC = (CheckBox)findViewById(R.id.checkboxC);
        checkBoxD = (CheckBox)findViewById(R.id.checkboxD);
        checkBoxE = (CheckBox)findViewById(R.id.checkboxE);

        fieldA = (EditText)findViewById(R.id.fieldA);
        fieldB = (EditText)findViewById(R.id.fieldB);
        fieldC = (EditText)findViewById(R.id.fieldC);
        fieldD = (EditText)findViewById(R.id.fieldD);
        fieldE = (EditText)findViewById(R.id.fieldE);

        /* Initialize the spinner, create an array adapter that stores array data from
            res/strings.xml. All array modifications are performed in that file. After
            creating the adapter to represent the array objects, specify the layout type
            for the spinner.
         */
        spinner = (Spinner)findViewById(R.id.spinner);

        //make spinner consist of the available tag
        List<String> spinnerItem = mvc.getTags();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                hideKeyboard(view);
                return false;
            }
        });
    }


    /** When the user clicks create question, create a flashcard using the ModelViewController
        object.
    */
    private void createFlashcardObject() {
        createQuestion.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View view) {
                    ArrayList<String> listOfAnswers = new ArrayList<String>();
                    boolean isEmpty = false;
                    listOfAnswers.add(fieldA.getText().toString());
                    listOfAnswers.add(fieldB.getText().toString());
                    listOfAnswers.add(fieldC.getText().toString());
                    listOfAnswers.add(fieldD.getText().toString());
                    listOfAnswers.add(fieldE.getText().toString());

                    String answer = "";

                    for (int i = 0; i < listOfAnswers.size(); ++i) {
                        if (listOfAnswers.get(i).isEmpty()) {
                            isEmpty = true;
                            break;
                        }
                    }

                    if (checkBoxA.isChecked()) {
                        if (!fieldA.getText().toString().isEmpty())
                            answer += fieldA.getText().toString() + PLACEHOLDER;
                    }
                    if (checkBoxB.isChecked()) {
                        if (!fieldA.getText().toString().isEmpty())
                         answer += fieldB.getText().toString() + PLACEHOLDER;
                    }
                    if (checkBoxC.isChecked()) {
                        if (!fieldA.getText().toString().isEmpty())
                         answer += fieldC.getText().toString() + PLACEHOLDER;
                    }
                    if (checkBoxD.isChecked()) {
                        if (!fieldA.getText().toString().isEmpty())
                         answer += fieldD.getText().toString() + PLACEHOLDER;
                    }
                    if (checkBoxE.isChecked()) {
                        if (!fieldA.getText().toString().isEmpty())
                            answer += fieldE.getText().toString() + PLACEHOLDER;
                    }

                    if (isEmpty || questionField.getText().toString().isEmpty() || answer.length() == 0 ||
                            spinner.getSelectedItem() == null) {
                        return;
                    }
                        //The flashcard creation
                    boolean successfulCreation = mvc.createFlashcard(
                        questionField.getText().toString(), answer, listOfAnswers, CATEGORY,
                            spinner.getSelectedItem().toString());

                        questionField.setText("");
                        fieldA.setText("");
                        fieldB.setText("");
                        fieldC.setText("");
                        fieldD.setText("");
                        fieldE.setText("");

                        /* Acknowledge the card was created by using a Toast object to display a
                            message.
                        */
                        if (successfulCreation) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Flashcard created",
                                    Toast.LENGTH_SHORT);
                            toast.show();

                            /* After a 1500 ms delay, return to the list of flashcards */
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 1500);

                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Error creating flashcard",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
    }
}
