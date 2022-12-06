package com.example.autism_care;

import static android.net.wifi.WpsInfo.LABEL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Mypage extends AppCompatActivity {

    // data : {'id':str, 'emotion':int, 'bool':str, 'date':SYSDATE, 'type':int}
    // emotion -> 0:기쁨, 1:당황, 2:불안, 3:슬픔, 4:분노, 5:상처
    // quiz -> 0:얼굴로따라하기(사진+글씨), 1:얼굴로따라하기(사진), 2:얼굴로따라하기(글씨), 3:퀴즈로풀기

    private LineChart lineChart;
    TextView tv_imgtxt, tv_img, tv_txt, tv_quiz;
    ProgressBar pro_imgtxt, pro_img, pro_txt, pro_quiz;
    ProgressDialog progress;
    RequestQueue queue;
    MyApp app;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        tv_imgtxt = findViewById(R.id.tv_imgtxt);
        tv_img = findViewById(R.id.tv_img);
        tv_txt = findViewById(R.id.tv_txt);
        tv_quiz = findViewById(R.id.tv_quiz);
        pro_imgtxt = findViewById(R.id.pro_imgtxt);
        pro_img = findViewById(R.id.pro_img);
        pro_txt = findViewById(R.id.pro_txt);
        pro_quiz = findViewById(R.id.pro_quiz);
        app = (MyApp) getApplication();

        // 통신중..
        progress = new ProgressDialog(Mypage.this);
        progress.setMessage("Uploading...");
        progress.show();

        mypage(app.ID);

        //초기화
        lineChart = (LineChart) findViewById(R.id.Chart);
        lineChart.setExtraBottomOffset(15f); //간격
        lineChart.getDescription().setEnabled(false); //chart 밑에 description

        //차트의 범례
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setXEntrySpace(15);
        legend.setFormToTextSpace(10);

        //x축
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setSpaceMax(4f);
        xAxis.setSpaceMin(1f);
        // 축을 숫자가 아니라 날짜로 표시
/*        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int range;
                return LABEL[range][(int) value];
            }
        });*/

        //y축
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setSpaceMax(1f);
        yAxis.setSpaceMin(0f);

        //데이터를 담을 Arraylist
        ArrayList<Entry> chart1 = new ArrayList<>(); //사진+글씨
        ArrayList<Entry> chart2 = new ArrayList<>(); //사진
        ArrayList<Entry> chart3 = new ArrayList<>(); //글씨
        ArrayList<Entry> chart4 = new ArrayList<>(); //퀴즈

        // x축 날짜, y축 개수
        //데이터 숫자 크기
        chart1.add(new Entry(1, 1)); //chart1에 좌표 데이터를 담음
        chart1.add(new Entry(2, 2));
        chart1.add(new Entry(3, 3));
        chart1.add(new Entry(3, 3));

        chart2.add(new Entry(1, 2)); //chart2에 좌표 데이터를 담음
        chart2.add(new Entry(2, 3));
        chart2.add(new Entry(3, 8));

        chart3.add(new Entry(1, 5)); //chart3에 좌표 데이터를 담음
        chart3.add(new Entry(2, 7));
        chart3.add(new Entry(3, 9));

        chart4.add(new Entry(1, 4)); //chart4에 좌표 데이터를 담음
        chart4.add(new Entry(2, 8));
        chart4.add(new Entry(3, 10));

        LineData chartData = new LineData(); // 차트에 담길 데이터

        // DataSet 생성 - 기존에 생성해둔 Entry 리스트를 가져와서 "사진+글씨" 범주로 묶어서 DataSet 만들기
        // 데이터가 담긴 Arraylist를 LineDataSet으로 변환
        LineDataSet lineDataSet1 = new LineDataSet(chart1, "사진+글씨");
        LineDataSet lineDataSet2 = new LineDataSet(chart2, "사진");
        LineDataSet lineDataSet3 = new LineDataSet(chart3, "글씨");
        LineDataSet lineDataSet4 = new LineDataSet(chart4, "퀴즈");

        // 해당 LineDataSet의 색 설정 :: 각 Line과 관련된 세팅은 여기서 설정
        lineDataSet1.setColor(Color.RED);
        lineDataSet2.setColor(Color.BLUE);
        lineDataSet3.setColor(Color.GREEN);
        lineDataSet4.setColor(Color.YELLOW);

        // 해당 LineDataSet을 적용될 차트에 들어갈 DataSet에 넣음
        chartData.addDataSet(lineDataSet1);
        chartData.addDataSet(lineDataSet2);
        chartData.addDataSet(lineDataSet3);
        chartData.addDataSet(lineDataSet4);

        lineChart.setData(chartData); // 차트에 위의 DataSet을 넣음

        lineChart.invalidate(); // 차트 업데이트
        lineChart.setTouchEnabled(false); // 차트 터치 disable
    }

    //flask로 전송
    private void mypage(String id) {

        // flask 연결
        String flask_url = "http://10.0.2.2:5000/mypage";
        // String flask_url = "http://192.168.0.12:5000/mypage";
        
        StringRequest request = new StringRequest(Request.Method.POST, flask_url,
                response -> {
                    progress.dismiss();
                    Log.e("flask", "sendLogin: " + response);

                    try {
                        jsonArray = new JSONArray(response);
                        Log.e("flask", "jsonArray: " + jsonArray);

                        double zero = jsonArray.getDouble(0);
                        double one = jsonArray.getDouble(1);
                        Log.e("zo", "number : "+(zero+one));
                        Log.e("zo", "number : "+(zero)/(zero+one));
                        Log.e("zo", "number : "+(int)((zero/(zero+one))*100));

                        pro_imgtxt.setProgress(
                                (int)((zero/(zero+one))*100)
                        );
                        tv_imgtxt.setText("맞힌 개수:"+jsonArray.getInt(0)+" / 푼 문제:"+(jsonArray.getInt(0)+jsonArray.getInt(1)));
                        pro_img.setProgress((int)((jsonArray.getDouble(2)/(jsonArray.getDouble(2)+jsonArray.getDouble(3)))*100));
                        tv_img.setText("맞힌 개수:"+jsonArray.getInt(2)+" / 푼 문제:"+(jsonArray.getInt(2)+jsonArray.getInt(3)));
                        pro_txt.setProgress((int)((jsonArray.getDouble(4)/(jsonArray.getDouble(4)+jsonArray.getDouble(5)))*100));
                        tv_txt.setText("맞힌 개수:"+jsonArray.getInt(4)+" / 푼 문제:"+(jsonArray.getInt(4)+jsonArray.getInt(5)));
                        pro_quiz.setProgress((int)((jsonArray.getDouble(6)/(jsonArray.getDouble(6)+jsonArray.getDouble(7)))*100));
                        Log.e("pro_quiz", "percent: " + (jsonArray.getDouble(6)/(jsonArray.getDouble(6)+jsonArray.getDouble(7)))*100);
                        tv_quiz.setText("맞힌 개수:"+jsonArray.getInt(6)+" / 푼 문제:"+(jsonArray.getInt(6)+jsonArray.getInt(7)));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progress.dismiss();
                    Toast.makeText(Mypage.this, "Some error occurred -> "+error, Toast.LENGTH_LONG).show();
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        queue = Volley.newRequestQueue(Mypage.this);
        queue.add(request);
    }

}