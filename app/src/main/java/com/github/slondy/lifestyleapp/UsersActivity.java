package com.github.slondy.lifestyleapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.slondy.timetableview.Schedule;
import com.github.slondy.timetableview.Time;
import com.github.slondy.timetableview.TimetableView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersActivity extends AppCompatActivity implements View.OnClickListener {

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
    private long now = System.currentTimeMillis();
    Date date = new Date(now);

    private Context context;
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;

    private Button addBtn;
    private Button clearBtn;
    private Button saveBtn;
    private Button loadBtn;


    private TimetableView timetable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        init();

        loadSavedData();
    }

    @SuppressLint("WrongViewCast")
    private void init(){
        this.context = this;
        addBtn = findViewById(R.id.add_btn);
        clearBtn = findViewById(R.id.clear_btn);
        saveBtn = findViewById(R.id.save_btn);
        loadBtn = findViewById(R.id.load_btn);

        mListTv = findViewById(R.id.result1);

        timetable = findViewById(R.id.timetable);
        //timetable.setHeaderHighlight(2);
        initView();
    }

    private void initView(){
        addBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        loadBtn.setOnClickListener(this);

        initMyAPI(BASE_URL);


        timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
                Intent i = new Intent(context, EditActivity.class);
                i.putExtra("mode",REQUEST_EDIT);
                i.putExtra("idx", idx);
                i.putExtra("schedules", schedules);
                startActivityForResult(i,REQUEST_EDIT);
            }
        });
    }

    private void initMyAPI(String baseUrl){

        Log.d(TAG,"initMyAPI : " + baseUrl);

        Gson gson = new GsonBuilder().setDateFormat("HH:mm:ss").create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_btn:
                Intent i = new Intent(this, EditActivity.class);
                i.putExtra("mode", REQUEST_ADD);
                startActivityForResult(i, REQUEST_ADD);
                break;
            case R.id.clear_btn:
                timetable.removeAll();
                break;
            case R.id.save_btn:
                saveByPreference(timetable.createSaveData());
                break;
            case R.id.load_btn:
                Log.d(TAG,"GET");
                Call<List<com.github.slondy.lifestyleapp.PostItem>> getCall = mMyAPI.get_posts();
                getCall.enqueue(new Callback<List<com.github.slondy.lifestyleapp.PostItem>>() {
                    @Override
                    public void onResponse(Call<List<com.github.slondy.lifestyleapp.PostItem>> call, Response<List<com.github.slondy.lifestyleapp.PostItem>> response) {
                        if( response.isSuccessful()){
                            List<com.github.slondy.lifestyleapp.PostItem> mList = response.body();
                            String result ="";
                            String re2 = "";
                            for( com.github.slondy.lifestyleapp.PostItem item : mList){

                                classTitle = item.getClassTitle();
                                classPlace = item.getClassPlace();
                                professorName = item.getProfessorName();
                                day = item.getDay();
                                startTime = item.getStartTime();
                                endTime = item.getEndTime();

                                String sHour = startTime.substring(0,2);
                                String sMin = startTime.substring(3,5);
                                int nsHour = Integer.parseInt(sHour);
                                int nsMin = Integer.parseInt(sMin);

                                String eHour = endTime.substring(0,2);
                                String eMin = endTime.substring(3,5);
                                int neHour = Integer.parseInt(eHour);
                                int neMin = Integer.parseInt(eMin);

                                Time sTime = new Time(nsHour, nsMin);
                                Time nTime = new Time(neHour, neMin);

                                result +=  "제목 : " + item.getClassTitle() + "\n" + " 위치:" + item.getClassPlace() + "\n" + "메모:" + item.getProfessorName() + "\n" +"요일:"+item.getDay() + "\n" +
                                        "시작시간:" + sTime.getHour()+":"+sTime.getMinute() + "\n" + "종료시간:" + nTime.getHour() +":"+ nTime.getMinute() + "\n" + "\n" + "\n";
//                            if (item.getIdx()==1) {
//                            }
                            }

                            mListTv.setText(result);
                        }else {
                            Log.d(TAG,"Status Code : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<com.github.slondy.lifestyleapp.PostItem>> call, Throwable t) {
                        Log.d(TAG,"Fail msg : " + t.getMessage());
                    }
                });
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ADD:
                if (resultCode == EditActivity.RESULT_OK_ADD) {
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    timetable.add(item);
                }
                break;
            case REQUEST_EDIT:
                /** Edit -> Submit */
                if (resultCode == EditActivity.RESULT_OK_EDIT) {
                    int idx = data.getIntExtra("idx", -1);
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    timetable.edit(idx, item);
                }
                /** Edit -> Delete */
                else if (resultCode == EditActivity.RESULT_OK_DELETE) {
                    int idx = data.getIntExtra("idx", -1);
                    timetable.remove(idx);
                }
                break;
        }
    }

    /** save timetableView's data to SharedPreferences in json format */
    private void saveByPreference(String data){
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("timetable_demo",data);
        editor.commit();
        Toast.makeText(this,"saved!",Toast.LENGTH_SHORT).show();
    }

    /** get json data from SharedPreferences and then restore the timetable */
    private void loadSavedData(){
        timetable.removeAll();
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        String savedData = mPref.getString("timetable_demo","");
        if(savedData == null && savedData.equals("")) return;
        timetable.load(savedData);
        Toast.makeText(this,"loaded!",Toast.LENGTH_SHORT).show();
    }
}
