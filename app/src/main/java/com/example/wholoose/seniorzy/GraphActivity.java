package com.example.wholoose.seniorzy;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Arrays;
import java.util.List;

public class GraphActivity extends AppCompatActivity {
    //sztywno ustawiona lista imitująca odczyty z opaski
    List<Integer> pulsePointsList = Arrays.asList(60, 80, 77, 65, 75, 77, 76, 73, 70, 80, 75, 73, 69, 65);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        final GraphView graph = findViewById(R.id.graph);
        Button button = findViewById(R.id.addButton);

        //Konfiguracja wykresu
        graph.setVisibility(View.VISIBLE);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(pulsePointsList.size() - 1);
        graph.setTitleTextSize(46);
        graph.setTitleColor(Color.rgb(255, 0, 0));
        graph.setTitle("Seniors pulse graph");
        graph.setBackgroundColor(Color.argb(50, 50, 0, 200));

        //Tytuly osi
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setPadding(32);
        gridLabel.setHorizontalAxisTitle("Time");
        gridLabel.setVerticalAxisTitle("Pulse");
        gridLabel.setHorizontalAxisTitleTextSize(30);
        gridLabel.setVerticalAxisTitleTextSize(30);
        gridLabel.setHorizontalAxisTitleColor(Color.argb(180, 255, 0, 0));
        gridLabel.setVerticalAxisTitleColor(Color.argb(180, 255, 0, 0));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    DataPoint[] dp = new DataPoint[pulsePointsList.size()];
                    for (int i = 1; i <= pulsePointsList.size(); i++) {
                        dp[i - 1] = new DataPoint(i - 1, pulsePointsList.get(i - 1));
                    }

                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
                    series.setColor(Color.argb(190, 255, 0, 0));
                    series.setDrawDataPoints(true);
                    series.setDataPointsRadius(6);
                    graph.addSeries(series);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(GraphActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}