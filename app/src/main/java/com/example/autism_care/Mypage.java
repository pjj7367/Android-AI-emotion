package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class Mypage extends AppCompatActivity {

    // data : {'id':string, 'emotion':int, 'bool':BOOL, 'date':SYSDATE, 'type':int}
    // emotion -> 0:기쁨, 1:당황, 2:불안, 3:슬픔, 4:분노, 5:상처
    // quiz -> 0:얼굴로따라하기(사진+글씨), 1:얼굴로따라하기(사진), 2:얼굴로따라하기(글씨), 3:퀴즈로풀기

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        //데이터를 담을 Arraylist
        ArrayList<Entry> entry_chart1 = new ArrayList<>();
        ArrayList<Entry> entry_chart2 = new ArrayList<>();

        lineChart = (LineChart) findViewById(R.id.Chart);

        LineData chartData = new LineData(); // 차트에 담길 데이터

        entry_chart1.add(new Entry(1, 1)); //entry_chart1에 좌표 데이터를 담음
        entry_chart1.add(new Entry(2, 2));
        entry_chart1.add(new Entry(3, 3));

        entry_chart2.add(new Entry(1, 2)); //entry_chart2에 좌표 데이터를 담음
        entry_chart2.add(new Entry(2, 3));
        entry_chart2.add(new Entry(3, 4));

        // 데이터가 담긴 Arraylist를 LineDataSet으로 변환
        LineDataSet lineDataSet1 = new LineDataSet(entry_chart1, "LineGraph1");
        LineDataSet lineDataSet2 = new LineDataSet(entry_chart1, "LineGraph2");

        // 해당 LineDataSet의 색 설정 :: 각 Line과 관련된 세팅은 여기서 설정
        lineDataSet1.setColor(Color.RED);
        lineDataSet2.setColor(Color.BLACK);

        // 해당 LineDataSet을 적용될 차트에 들어갈 DataSet에 넣음
        chartData.addDataSet(lineDataSet1);
        chartData.addDataSet(lineDataSet2);

        lineChart.setData(chartData); // 차트에 위의 DataSet을 넣음

        lineChart.invalidate(); // 차트 업데이트
        lineChart.setTouchEnabled(false); // 차트 터치 disable

    }

}