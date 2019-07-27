package com.example.apipullparser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText location, curency, temp, humidity, pressure;
    private String url1 = "http://api.openweathermap.org/data/2.5/weather?q=";
    private String url2 = "&mode=xml";
    private HandleXml obj;
    private TextView txtView1, txtView2, txtView3;
    private Button weather_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weather_btn = findViewById(R.id.btn_weather);

        location=findViewById(R.id.txtLocation);
        curency=findViewById(R.id.txt_curency);
        temp=findViewById(R.id.txt_temp);
        humidity=findViewById(R.id.txt_humidity);
        pressure=findViewById(R.id.txt_pressure);

        weather_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = location.getText().toString();
                String finalUrl = url1 + url + url2;
                curency.setText(finalUrl);

                obj = new HandleXml(finalUrl);
                obj.fetchXml();

                while (obj.parsingComplete);
                curency.setText(obj.getCountry());
                temp.setText(obj.getTemperature());
                humidity.setText(obj.getHumidity());
                pressure.setText(obj.getPressure());

            }
        });
    }
}
