package com.example.apipullparser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HandleXml {
    private String country = "country";
    private String temperature = "temperature";
    private String humidity = "humidity";
    private String pressure = "pressure";
    private String urlString = null;
    private XmlPullParserFactory factory;
    public volatile boolean parsingComplete= true;

    public HandleXml(String url){
        this.urlString = url;
    }

    public String getCountry(){
        return country;
    }

    public String getTemperature(){
        return temperature;
    }

    public String getHumidity(){
        return humidity;
    }

    public String getPressure(){
        return pressure;
    }

    private void parseXMLAndStoreIt(XmlPullParser parser){
        int event;
        String text = null;
        try {
        event= parser.getEventType();
        while(event != XmlPullParser.END_DOCUMENT){
            String name = parser.getName();
            switch (event){
                case XmlPullParser.START_TAG:
                    break;
                 case XmlPullParser.TEXT:
                     text=parser.getText();
                     break;
                 case XmlPullParser.END_TAG:
                     if (name.equals("country")){
                         country = parser.getAttributeValue(null, "value");
                     }
                     else if (name.equals("humidity")){
                         humidity = parser.getAttributeValue(null, "value");
                     }
                     else if (name.equals("pressure")){
                         pressure = parser.getAttributeValue(null, "value");
                     }
                     else if (name.equals("temperature")){
                         temperature = parser.getAttributeValue(null, "value");
                     }
                     else{
                         break;
                     }
                         event=parser.next();
                     }
            }
            parsingComplete = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchXml(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                    connection.setReadTimeout(10000);
                    connection.setConnectTimeout(15000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();

                    InputStream is = connection.getInputStream();
                    factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
                    parser.setInput(is,null);
                    parseXMLAndStoreIt(parser);
                    is.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
