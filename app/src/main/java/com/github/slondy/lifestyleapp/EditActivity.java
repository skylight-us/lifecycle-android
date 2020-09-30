package com.github.slondy.lifestyleapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.github.slondy.timetableview.Schedule;
import com.github.slondy.timetableview.Time;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int RESULT_OK_ADD = 1;
    public static final int RESULT_OK_EDIT = 2;
    public static final int RESULT_OK_DELETE = 3;

    private Context context;

    private Button deleteBtn;
    private Button submitBtn;
    private EditText subjectEdit;
    private EditText classroomEdit;
    private EditText professorEdit;
    private Spinner daySpinner;
    private TextView startTv;
    private TextView endTv;

    //request mode
    private int mode;

    private Schedule schedule;
    private int editIdx;


    private final  String TAG = getClass().getSimpleName();

    // server의 url을 적어준다
    private final String BASE_URL = "https://b887a999e251.ngrok.io";
    private MyAPI mMyAPI;
    Time time = new Time();
    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    String f_time= format.format(System.currentTimeMillis());
    static String classTitle = "";
    static String classPlace = "";
    static String professorName = "";
    static int day = 0;
    static String startTime = "";
    static String endTime = "";


    private TextView mListTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        initMyAPI(BASE_URL);
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        return builder.build();
    }

    private void initMyAPI(String baseUrl){



        Log.d(TAG,"initMyAPI : " + baseUrl);

        Gson gson = new GsonBuilder().setDateFormat("HH:mm:ss").create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(createOkHttpClient())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);

    }

    private void init(){
        this.context = this;
        deleteBtn = findViewById(R.id.delete_btn);
        submitBtn = findViewById(R.id.submit_btn);
        subjectEdit = findViewById(R.id.subject_edit);
        classroomEdit = findViewById(R.id.classroom_edit);
        professorEdit = findViewById(R.id.professor_edit);
        daySpinner = findViewById(R.id.day_spinner);
        startTv = findViewById(R.id.start_time);
        endTv = findViewById(R.id.end_time);

        mListTv = findViewById(R.id.result1);

        //set the default time
        schedule = new Schedule();
        schedule.setStartTime(new Time(10,0));
        schedule.setEndTime(new Time(13,30));

        checkMode();
        initView();
    }

    /** check whether the mode is ADD or EDIT */
    private void checkMode(){
        Intent i = getIntent();
        mode = i.getIntExtra("mode", UsersActivity.REQUEST_ADD);

        if(mode == UsersActivity.REQUEST_EDIT){
            loadScheduleData();
            deleteBtn.setVisibility(View.VISIBLE);
        }
    }
    private void initView(){
        submitBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                schedule.setDay(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        startTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(context,listener,schedule.getStartTime().getHour(), schedule.getStartTime().getMinute(), false);
                dialog.show();
            }

            private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    startTv.setText(hourOfDay + ":" + minute);
                    schedule.getStartTime().setHour(hourOfDay);
                    schedule.getStartTime().setMinute(minute);
                }
            };
        });
        endTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(context,listener,schedule.getEndTime().getHour(), schedule.getEndTime().getMinute(), false);
                dialog.show();
            }

            private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    endTv.setText(hourOfDay + ":" + minute);
                    schedule.getEndTime().setHour(hourOfDay);
                    schedule.getEndTime().setMinute(minute);
                }
            };
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_btn:

                int sHour = schedule.getStartTime().getHour();
                int sMin = schedule.getStartTime().getMinute();
                int eHour = schedule.getEndTime().getHour();
                int eMin = schedule.getEndTime().getMinute();

                String sTime = sHour + ":" + sMin;
                String eTime = eHour + ":" + eMin;

                Log.d(TAG,"POST");
                com.github.slondy.lifestyleapp.PostItem item = new com.github.slondy.lifestyleapp.PostItem();
                //item.setIdx(5);
                item.setClassTitle(subjectEdit.getText().toString());
                item.setClassPlace(classroomEdit.getText().toString());
                item.setProfessorName(professorEdit.getText().toString());
                item.setStartTime(sTime);
                    item.setEndTime(eTime);
                    item.setDay(schedule.getDay());


                Call<com.github.slondy.lifestyleapp.PostItem> postCall = mMyAPI.post_posts(item);
                //mMyAPI.post_posts(input).enqueue(new Callback<PostItem>() {
                postCall.enqueue(new Callback<com.github.slondy.lifestyleapp.PostItem>() {
                    @Override
                    public void onResponse(Call<com.github.slondy.lifestyleapp.PostItem> call, Response<com.github.slondy.lifestyleapp.PostItem> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG,"등록 완료");
                        }else {
                            Log.d(TAG,"Status Code : " + response.code());
                            Log.d(TAG,response.errorBody().toString());
                            Log.d(TAG,call.request().body().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.github.slondy.lifestyleapp.PostItem> call, Throwable t) {
                        Log.d(TAG,"Fail msg : " + t.getMessage());
                    }
                });
                if(mode == UsersActivity.REQUEST_ADD){
                    inputDataProcessing();
                    Intent i = new Intent();
                    ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                    //you can add more schedules to ArrayList
                    schedules.add(schedule);
                    i.putExtra("schedules",schedules);
                    setResult(RESULT_OK_ADD,i);
                    finish();

                }
                else if(mode == UsersActivity.REQUEST_EDIT){
                    inputDataProcessing();
                    Intent i = new Intent();
                    ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                    schedules.add(schedule);
                    i.putExtra("idx",editIdx);
                    i.putExtra("schedules",schedules);
                    setResult(RESULT_OK_EDIT,i);
                    finish();
                }
                break;
            case R.id.delete_btn:
                Intent i = new Intent();
                i.putExtra("idx",editIdx);
                setResult(RESULT_OK_DELETE, i);
                finish();
                break;
        }
    }

    private void loadScheduleData(){
        Intent i = getIntent();
        editIdx = i.getIntExtra("idx",-1);
        ArrayList<Schedule> schedules = (ArrayList<Schedule>)i.getSerializableExtra("schedules");
        schedule = schedules.get(0);
        subjectEdit.setText(schedule.getClassTitle());
        classroomEdit.setText(schedule.getClassPlace());
        professorEdit.setText(schedule.getProfessorName());
        daySpinner.setSelection(schedule.getDay());
    }

    private void inputDataProcessing(){
        schedule.setClassTitle(subjectEdit.getText().toString());
        schedule.setClassPlace(classroomEdit.getText().toString());
        schedule.setProfessorName(professorEdit.getText().toString());
    }
}
