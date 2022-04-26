package com.bao.currency;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bao.currency.Controller.Database;
import com.bao.currency.Model.Exchange;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Database database;
    String url="https://aud.fxexchangerate.com/rss.xml";
    public static List<String> list = new ArrayList<>();
    AutoCompleteTextView cbb_from, cbb_to;
    ArrayAdapter<String> listArrayAdapter;
    public List<String> tempList=new ArrayList<>();
    public static List<Exchange> exchangeList = new ArrayList<>();
    TextView text_from, text_to,input_to;
    EditText input_from;
    Button btn_convert;
    ImageView img_history;

    Thread t1=null;
    Thread t2=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testData();
        initView();
        getCountry();


        cbb_from.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = getCurrency(adapterView.getItemAtPosition(i).toString());
                //Toast.makeText(MainActivity.this, "Item: "+item, Toast.LENGTH_SHORT).show();
                text_from.setText(item);
            }
        });

        cbb_to.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = getCurrency(adapterView.getItemAtPosition(i).toString());
                //Toast.makeText(MainActivity.this, "Item: "+item, Toast.LENGTH_SHORT).show();
                text_to.setText(item);
            }
        });

        btn_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new CustomTask().execute((Void[])null);
                /*t1=new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        try{
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                            LocalDateTime now = LocalDateTime.now();
                            String from=text_from.getText().toString().toLowerCase();
                            String to=text_to.getText().toString().toLowerCase();
                            String sotien=input_from.getText().toString();
                            String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36";
                            Document doc = Jsoup.connect("https://" + from + ".fxexchangerate.com/" + to + "/" + sotien + "-currency-rates.html").userAgent(userAgent).get();

                            Element s=doc.getElementById("srate");
                            input_to.setText(s.text());


                            database=new Database(getApplicationContext());
                            database.insert(from, to, sotien, s.text(), dtf.format(now));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });


                t1.start();

                 */
            }
        });

        img_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), HistoryActivity.class);
                getApplicationContext().startActivity(intent);
            }
        });


    }


    private void testData(){
        database=new Database(this);
    }

    private void initView() {
        cbb_from = findViewById(R.id.cbb_from);
        cbb_to = findViewById(R.id.cbb_to);
        text_from=findViewById(R.id.text_from);
        text_to=findViewById(R.id.text_to);
        input_from=findViewById(R.id.input_from);
        input_to=findViewById(R.id.input_to);
        btn_convert=findViewById(R.id.btn_convert);
        img_history=findViewById(R.id.btn_history);
    }

    private void getCountry() {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                        XmlPullParser parser=factory.newPullParser();
                        parser.setInput(response.body().byteStream(),"UTF-8");

                        int eventType= parser.getEventType();
                        while (eventType!=XmlPullParser.END_DOCUMENT){

                            if(eventType==XmlPullParser.START_TAG){

                                if(parser.getName().equals("title")){
                                    String quocgia=parser.nextText();
                                    tempList.add(getcountry(quocgia));
                                    //String tiente=layTiGia(quocgia);
                                    //country.setTiente(layTiGia(parser.nextText()));
                                    //Log.d("TAG", "On response: START_TAG "+ parser.getName());
                                }

                            }else if(eventType==XmlPullParser.END_TAG){
                                //Log.d("TAG", "On response END_TAG"+ parser.getName());
                            }else if(eventType==XmlPullParser.TEXT){
                                //Log.d("TAG", "On response: TEXT"+ parser.getText());
                            }

                            eventType=parser.next();
                        }

                        tempList.remove(0);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, tempList);
                                cbb_from.setAdapter(listArrayAdapter);
                                cbb_to.setAdapter(listArrayAdapter);
                            }
                        });

                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    private void exchangeRate() {
        String code=text_from.getText().toString().toLowerCase();
        String url1="https://"+code+".fxexchangerate.com/rss.xml";
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(url1).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        Exchange exchange=new Exchange();
                        XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                        XmlPullParser parser=factory.newPullParser();
                        parser.setInput(response.body().byteStream(),"UTF-8");

                        int eventType= parser.getEventType();
                        while (eventType!=XmlPullParser.END_DOCUMENT){

                            if(eventType==XmlPullParser.START_TAG){

                                if(parser.getName().equals("title")){
                                    exchange=new Exchange();
                                    String quocgia=parser.nextText();
                                    exchange.setTiente(getCurrency(quocgia));
                                    //Log.d("TAG", "On response: START_TAG "+ parser.getName());
                                } else if(parser.getName().equals("description")){
                                    String tigia=parser.nextText();
                                    exchange.setRate(tigia);
                                    exchangeList.add(exchange);
                                }

                            }else if(eventType==XmlPullParser.END_TAG){
                                //Log.d("TAG", "On response END_TAG"+ parser.getName());
                            }else if(eventType==XmlPullParser.TEXT){
                                //Log.d("TAG", "On response: TEXT"+ parser.getText());
                            }

                            eventType=parser.next();
                        }

                        exchangeList.remove(0);
                        String kq=String.valueOf(result(exchangeList));
                        input_to.setText(kq);
                        String from=text_from.getText().toString().toLowerCase();
                        String to=text_to.getText().toString().toLowerCase();
                        String sotien=input_from.getText().toString();

                        database=new Database(getApplicationContext());
                        database.insert(from, to, sotien, kq, dtf.format(now));

                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    static String tachSo(String text){
        String[] s=text.split("=");

        String[] kq=s[1].split(" ");

        return kq[1];
    }
    String getCurrency(String text) {
        String[] s = text.split("");

        for (String t : s) {
            list.add(t);
        }

        String s1 = list.get(list.size() - 4) + "" + list.get(list.size() - 3) + "" + list.get(list.size() - 2);
        //System.out.println(t);
        return s1.toUpperCase();
    }

    String getcountry(String text) {
        String[] s = text.split("/");
        String last = s[s.length - 1];
        return last;
    }

    double result(List<Exchange> list){
        double kq=0;
        for(Exchange ex: list){
            if(text_to.getText().toString().equals(ex.getTiente())){
                System.out.println(Double.parseDouble(input_from.getText().toString()));
                kq=Double.parseDouble(input_from.getText().toString())*Double.parseDouble(tachSo(ex.getRate()));
                System.out.println(text_to.getText().toString());
                System.out.println(ex.getTiente());
                System.out.println(Double.parseDouble(tachSo(ex.getRate())));
                System.out.println(kq);
            }
        }
        return kq;
    }
    private class CustomTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... param) {
            exchangeRate();
            return null;
        }

        protected void onPostExecute(Void param) {
            //Print Toast or open dialog
        }
    }
}



