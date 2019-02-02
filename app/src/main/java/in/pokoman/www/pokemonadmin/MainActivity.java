package in.pokoman.www.pokemonadmin;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import in.pokoman.www.pokemonadmin.Model.Question;
import in.pokoman.www.pokemonadmin.Model.Quiz;
import in.pokoman.www.pokemonadmin.Model.QuizStatus;

public class MainActivity extends AppCompatActivity {

    private TextView questionText;
    private Button optbtn1, optbtn2, optbtn3, optbtn4;
    private Button startbtn, showbtn, nextbtn, endbtn;
    private DatabaseReference mQuizStatusReference;
    private DatabaseReference mQuizReference;
    private DatabaseReference mQuestionsReference;
    private String liveStatus, showQuestion;
    private String currentQuesno = "1";
    private List<Question> questionList;
    private ValueEventListener mQuizStatusListner;
    private ValueEventListener mQuizListener;
    private ValueEventListener mQuestionListner;
    private VideoView v1;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intialise Ui
        initUI();

        //setting On CLick Listner
        attachOnClickListner();

        //Listen to Quiz Status
        attachQuizStatusListener();

        //Enable Disable Button
        disableButton();

        //Listener to Quiz
        attachQuizListener();

        //Listener to Question
        attachQuestionListener();

    }

    private void initUI() {
        questionText = (TextView) findViewById(R.id.question_text);
        optbtn1 = (Button) findViewById(R.id.optionbtn1);
        optbtn2 = (Button) findViewById(R.id.optionbtn2);
        optbtn3 = (Button) findViewById(R.id.optionbtn3);
        optbtn4 = (Button) findViewById(R.id.optionbtn4);
        startbtn = (Button) findViewById(R.id.start_btn);
        showbtn = (Button) findViewById(R.id.showque_btn);
        nextbtn = (Button) findViewById(R.id.nextque_btn);
        endbtn = (Button) findViewById(R.id.end_btn);
        mQuizStatusReference = FirebaseDatabase.getInstance().getReference().child("QuizStatus");
        mQuestionsReference = FirebaseDatabase.getInstance().getReference().child("Question");
        mQuizReference = FirebaseDatabase.getInstance().getReference().child("Quizzes");
        questionList = new ArrayList<>();
        v1=findViewById(R.id.video);

    }

    private void disableButton() {
        //When QUiz is Live
        if(liveStatus == "1"){
            startbtn.setClickable(false);
            showbtn.setClickable(true);
            nextbtn.setClickable(true);
            endbtn.setClickable(true);
        }
        else{
            showbtn.setClickable(false);
            nextbtn.setClickable(false);
            endbtn.setClickable(false);
            startbtn.setClickable(true);
        }
    }

    private void updateQuestion(int currentQuestionNo, List<Question> questions) {
        int currentno = currentQuestionNo;
        questionText.setText(questions.get(currentno).getQues());
        optbtn1.setText(questions.get(currentno).getOp1());
        optbtn2.setText(questions.get(currentno).getOp2());
        optbtn3.setText(questions.get(currentno).getOp3());
        optbtn4.setText(questions.get(currentno).getOp4());
    }

    private void attachOnClickListner() {
        //To Start the Quiz
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liveStatus = "1";

                v1.setVideoURI(uri);
                v1.requestFocus();
                v1.start();
                currentQuesno = "0";
                updateChange();
            }
        });

        //To showQuestion
        showbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showQuestion.equals("0")) {
                    showQuestion = "1";
                    int curr = Integer.parseInt(currentQuesno);
                    updateQuestion(curr, questionList);
                    showbtn.setText("Hide Question");
                } else {
                    showQuestion = "0";
                    showbtn.setText("Show Question");
                }
                updateChange();
            }
        });

        //To Show Next Question
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int curr = Integer.parseInt(currentQuesno);
                System.out.println("Current question number : "+ curr);
                if (curr >= 5) {
                    curr = 0;
                }
                updateQuestion(curr, questionList);
                curr++;
                currentQuesno = Integer.toString(curr);
                updateChange();


            }
        });

        //TO End the Quiz
        endbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liveStatus = "0";
                showQuestion="0";
                currentQuesno="0";
                v1.stopPlayback();
                updateChange();
            }
        });
    }

    private void updateChange() {
        QuizStatus status = new QuizStatus(liveStatus, showQuestion, currentQuesno);
        mQuizStatusReference.setValue(status);
    }

    private void attachQuestionListener() {
        if (mQuestionListner == null) {
            mQuestionListner = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot questionsnap : dataSnapshot.getChildren()) {
                        Question questions = questionsnap.getValue(Question.class);
                        questionList.add(questions);
                    }
                    // updateQuestion(currentQuesno, questionList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mQuestionsReference.addValueEventListener(mQuestionListner);
        }
    }

    private void attachQuizListener() {
        if (mQuizListener == null) {
            mQuizListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Quiz quiz = dataSnapshot.getValue(Quiz.class);
                    uri = uri.parse(quiz.getQvideourl());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mQuizReference.addValueEventListener(mQuizListener);
        }
    }

    private void attachQuizStatusListener() {
        if (mQuizStatusListner == null) {
            mQuizStatusListner = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    QuizStatus status = dataSnapshot.getValue(QuizStatus.class);
                    liveStatus = status.getLive();
                    currentQuesno = status.getCurques();
                    showQuestion = status.getShowques();
                    //Enable Disable Button
                    disableButton();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mQuizStatusReference.addValueEventListener(mQuizStatusListner);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        endbtn.callOnClick();
        detachAllListener();
    }

    private void detachAllListener() {
        if(mQuestionListner!=null)
            mQuestionListner = null;
        if(mQuizListener!=null)
            mQuizListener = null;
        if(mQuizStatusListner!=null)
            mQuizStatusListner = null;
    }
}
