package com.example.android.quizapp;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<TechnoName> listOfQuotes = new ArrayList<>();
    TechnoName correctAnswer;

    TextView counterTextView;
    TextView languageTextView;
    TextView optionOneTextView;
    TextView optionTwoTextView;
    TextView optionThreeTextView;
    Button nextButton;

    boolean isTurnFinished = false;
    boolean isGameFinished = false;

    int languageAlreadyDisplayed = 0;
    int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        counterTextView = (TextView) findViewById(R.id.counter);
        languageTextView = (TextView) findViewById(R.id.quote);
        optionOneTextView = (TextView) findViewById(R.id.optionOne);
        optionTwoTextView = (TextView) findViewById(R.id.optionTwo);
        optionThreeTextView = (TextView) findViewById(R.id.optionThree);
        nextButton = (Button) findViewById(R.id.buttonNext);
        nextButton.setText(R.string.next);

        TechQue.initQuotes(listOfQuotes);
        shuffleList(listOfQuotes);

        displayQuoteAndAnswers();

        optionOneTextView.setOnClickListener(choiceOneOnClickListener);
        optionTwoTextView.setOnClickListener(choiceTwoOnClickListener);
        optionThreeTextView.setOnClickListener(choiceThreeOnClickListener);
        nextButton.setOnClickListener(nextButtonOnClickListener);
        
    }
    
  
    final View.OnClickListener choiceOneOnClickListener = new View.OnClickListener() {
        public void onClick(final View v){
            if (!isTurnFinished && !isGameFinished){
                isOptionChosenCorrect(0);
                isTurnFinished = true;
            }
        }
    };

    final View.OnClickListener choiceTwoOnClickListener = new View.OnClickListener() {
        public void onClick(final View v){
            if (! isTurnFinished && !isGameFinished){
                isOptionChosenCorrect(1);
                isTurnFinished = true;
            }
        }
    };


    final View.OnClickListener choiceThreeOnClickListener = new View.OnClickListener() {
        public void onClick(final View v){
            if (!isTurnFinished && !isGameFinished){
                isOptionChosenCorrect(2);
                isTurnFinished = true;
            }
        }
    };

    final View.OnClickListener nextButtonOnClickListener = new View.OnClickListener() {
        public void onClick(final View v){
            if (isGameFinished) {
                resetGame();
            } else if (isTurnFinished) {
                beginNextTurn();
            }
        }
    };

    private void shuffleList(List items){
        Collections.shuffle(items);
    }

    private List<TechnoName> getOptions(){
        List<TechnoName> options = new ArrayList<>();
        options.add(listOfQuotes.get(languageAlreadyDisplayed));
        options.add(listOfQuotes.get(languageAlreadyDisplayed + 20));
        options.add(listOfQuotes.get(languageAlreadyDisplayed + 40));
        return options;
    }

    private List<TechnoName> getThreeQuotesFromListToDisplayAsAnswer(){
        List<TechnoName> options = getOptions();
        shuffleList(options);
        return options;
    }

    private void displayQuoteAndAnswers(){
        if (languageAlreadyDisplayed == 10){
            isGameFinished = true;
            finalGameDisplay();
            return;
        }
        correctAnswer = listOfQuotes.get(languageAlreadyDisplayed);
        languageTextView.setText(correctAnswer.techname);

        List<TechnoName> answers = getThreeQuotesFromListToDisplayAsAnswer();
        optionOneTextView.setText(answers.get(0).creator);
        optionTwoTextView.setText(answers.get(1).creator);
        optionThreeTextView.setText(answers.get(2).creator);
        languageAlreadyDisplayed += 1;
        String counterText = Integer.toString(languageAlreadyDisplayed) +
                this.getResources().getString(R.string.totalTurns);
        counterTextView.setText(counterText);
    }

    private String getSelectedQuote(int option){
        switch (option) {
            case 0:
                return optionOneTextView.getText().toString();
            case 1:
                return optionTwoTextView.getText().toString();
            case 2:
                return optionThreeTextView.getText().toString();
            default:
                return "";
        }
    }

    private TextView getSelectedTextView(int option){
        switch (option) {
            case 0:
                return optionOneTextView;
            case 1:
                return optionTwoTextView;
            case 2:
                return optionThreeTextView;
            default:
                return null;
        }
    }

    private void isOptionChosenCorrect(int option){
        TextView selectedTextView = getSelectedTextView(option);
        if (correctAnswer.creator != getSelectedQuote(option))
        {
            markOptionChosenAsWrong(selectedTextView);
            return;
        }
        markOptionChosenAsCorrect(selectedTextView);
        correctAnswers += 1;
    }

    private void markOptionChosenAsCorrect(TextView chosen){
        chosen.setBackgroundResource(R.drawable.correct_answer_border);
        changeNextButtonVisibility(View.VISIBLE);
        checkIfTurnHasFinished();
    }

    private void markOptionChosenAsWrong(TextView chosen){
        chosen.setBackgroundResource(R.drawable.wrong_answer_border);
        TextView correctAnswerTextView = getTextViewCorrectAnswer();
        correctAnswerTextView.setBackgroundResource(R.drawable.correct_answer_border);
        changeNextButtonVisibility(View.VISIBLE);
        checkIfTurnHasFinished();
    }

    private TextView getTextViewCorrectAnswer() {
        if (optionOneTextView.getText().toString().equals(correctAnswer.creator)) {
            return optionOneTextView;
        }
        if (optionTwoTextView.getText().toString().equals(correctAnswer.creator)) {
            return optionTwoTextView;
        }
        if (optionThreeTextView.getText().toString().equals(correctAnswer.creator)) {
            return optionThreeTextView;
        }
        return null;
    }

    private void checkIfTurnHasFinished(){
        if (isTurnFinished) {
            beginNextTurn();
        }
    }

    private void finalGameDisplay(){
        // notDisplayCounterAndAnswers();
        changeCounterAndAnswersVisibility(View.INVISIBLE);
        finalGameResults();
        nextButton.setText(R.string.reset);
        changeNextButtonVisibility(View.VISIBLE);
    }

    private void beginNextTurn(){
        isTurnFinished = false;
        correctAnswer = null;
        resetAnswersBackgroundColor();
        changeNextButtonVisibility(View.INVISIBLE);
        displayQuoteAndAnswers();
    }

    private void changeNextButtonVisibility(int visibility){
        nextButton.setVisibility(visibility);
    }

    private void resetAnswersBackgroundColor(){
        optionOneTextView.setBackgroundResource(R.drawable.answers_border);
        optionTwoTextView.setBackgroundResource(R.drawable.answers_border);
        optionThreeTextView.setBackgroundResource(R.drawable.answers_border);
    }

    private void changeCounterAndAnswersVisibility(int visibility){
        optionOneTextView.setVisibility(visibility);
        optionTwoTextView.setVisibility(visibility);
        optionThreeTextView.setVisibility(visibility);
        counterTextView.setVisibility(visibility);
    }

    private void finalGameResults(){
        String finalGameString = this.getResources().getString(R.string.correctAnswer) +
                "\n\n" +
                String.format("%d", correctAnswers) +
                this.getResources().getString(R.string.totalTurns);
        languageTextView.setText(finalGameString);
    }

    private void resetGame(){
        beginNextTurn();
        isTurnFinished = false;
        isGameFinished = false;
        correctAnswers = 0;
        languageAlreadyDisplayed = 0;
        shuffleList(listOfQuotes);
        displayQuoteAndAnswers();
        nextButton.setText(R.string.next);
        changeNextButtonVisibility(View.INVISIBLE);
        changeCounterAndAnswersVisibility(View.VISIBLE);
    }
}

