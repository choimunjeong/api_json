package com.example.traintest_191112;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView textView;          //결과화면 변수
    EditText start, end, date;  //출발,도착,출발날짜 입력받는 변수
    String starT, enD, datE;    //입력받은 값을 string으로 저장하는 변수
    String receiveMsg;
    String resultText = "";
    ArrayList<String>arrplacenameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //xml과 변수 연결
        Button button = (Button) findViewById(R.id.button);
        start = (EditText) findViewById(R.id.start);
        end = (EditText) findViewById(R.id.end);
        date = (EditText) findViewById(R.id.date);
        textView = (TextView) findViewById(R.id.textView);

        arrplacenameList = new ArrayList<>();

        //버튼 누르면 작동
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    resultText = new Task().execute().get();
                    trianjsonParser(receiveMsg);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                starT = start.getText().toString();  //editText에서 입력받은 값을 저장
                enD = end.getText().toString();       //editText에서 입력받은 값을 저장
                datE = date.getText().toString();     //editText에서 입력받은 값을 저장

            }
        });
    }

    public  class  Task extends AsyncTask<String, Void, String>{
    private String str;
        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            try{
                url = new URL("http://openapi.tago.go.kr/openapi/service/TrainInfoService/getStrtpntAlocFndTrainInfo?serviceKey=7LT0Q7XeCAuzBmGUO7LmOnrkDGK2s7GZIJQdvdZ30lf7FmnTle%2BQoOqRKpjcohP14rouIrtag9KOoCZe%2BXuNxg%3D%3D&numOfRows=10&pageNo=1&depPlaceId=NAT130126&arrPlaceId=NAT021549&depPlandTime=20200204&trainGradeCode=02&_type=json");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                    Log.i("receiveMsg : ", receiveMsg);

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }


    public String[] trianjsonParser(String jsonString){
        String adultcharge = null;
        String arrplacename = null;
        String arrplandtime = null;
        String depplacename = null;
        String depplandtime = null;
        String traingradename = null;

        String[] arraysum = new String[100];
        String result = "";
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            JSONObject jsonObject3 = jsonObject1.getJSONObject("body");
            JSONObject jsonObject4 = jsonObject3.getJSONObject("items");
            JSONArray jsonArray = jsonObject4.getJSONArray("item");

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObject = jsonArray.getJSONObject(i);
                adultcharge = jObject.getString("adultcharge");
                arrplacename = jObject.getString("arrplacename");
                arrplandtime = jObject.getString("arrplandtime");
                depplacename = jObject.getString("depplacename");
                depplandtime = jObject.getString("depplandtime");
                traingradename = jObject.getString("traingradename");

                arraysum[i]=adultcharge  +"\n" + arrplacename  +"\n" + arrplandtime
                            +"\n" + depplacename +"\n" + depplandtime  +"\n" + traingradename;

                result += arraysum[i]+"\n\n";
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        textView.setText(result);
        return  arraysum;

    }
}








//        class SearchTask extends AsyncTask<String, Void, String> {
//
//        //dolnBackground()는 새로 만들어진 스레드, 즉 백그라운드 작업을 할 수 있음
//        @Override
//        protected String doInBackground(String... strings) {
//        }
//            Log.d("시작", "시작");
//            String url = "http://openapi.tago.go.kr/openapi/service/TrainInfoService/getStrtpntAlocFndTrainInfo?serviceKey=" +
//                    "7LT0Q7XeCAuzBmGUO7LmOnrkDGK2s7GZIJQdvdZ30lf7FmnTle%2BQoOqRKpjcohP14rouIrtag9KOoCZe%2BXuNxg%3D%3D&numOfRows=10" +
//                    "&pageNo=1&depPlaceId="+starT+"&arrPlaceId="+enD+"&depPlandTime="+datE+"&trainGradeCode=02&_type=json";    URL xmlUrl;
//            String returnResurlt = "";   //파싱 결과값을 받을 변수
//
//            try {
//                Log.d("시작", "시작1");
//
//                //HTTP 프로토골 기반의 소켓 통신하는 과정
//                //참고) https://javacan.tistory.com/entry/35
//                xmlUrl = new URL(url);
//                xmlUrl.openConnection().getInputStream();
//
//        String adultcharge = null;
//        String arrplacename = null;
//        String arrplandtime = null;
//        String depplacename = null;
//        String depplandtime = null;
//        String traingradename = null;
//
//        String[] arraysum = new String[5];
//
//        try{
//            JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("item");
//            for(int i = 0; i < jsonArray.length(); i++){
//                HashMap map = new HashMap<>();
//                JSONObject jObject = jsonArray.getJSONObject(i);
//
//                adultcharge = jObject.optString("adultcharge");
//                arrplacename = jObject.optString("arrplacename");
//                arrplandtime = jObject.optString("arrplandtime");
//                depplacename = jObject.optString("depplacename");
//                depplandtime = jObject.optString("depplandtime");
//                traingradename = jObject.optString("traingradename");
//
//                arraysum[0]=adultcharge;
//                arraysum[1]=arrplacename;
//                arraysum[2]=arrplandtime;
//                arraysum[3]=depplacename;
//                arraysum[4]=depplandtime;
//                arraysum[5]=traingradename;
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return  arraysum;
//
//    }

    //SearchTask 클래스 정의/ xml 파싱을 위한 작업
    //AsynTask에 대한 이해 필요(구글링해서 찾아볼것, 같이 공부하자 지연언니, 한솔아 *^^*
//    class SearchTask extends AsyncTask<String, Void, String> {
//
//        //dolnBackground()는 새로 만들어진 스레드, 즉 백그라운드 작업을 할 수 있음
//        @Override
//        protected String doInBackground(String... strings) {
//            Log.d("시작", "시작");
//            String url = "http://openapi.tago.go.kr/openapi/service/TrainInfoService/getStrtpntAlocFndTrainInfo?serviceKey=" +
//                    "7LT0Q7XeCAuzBmGUO7LmOnrkDGK2s7GZIJQdvdZ30lf7FmnTle%2BQoOqRKpjcohP14rouIrtag9KOoCZe%2BXuNxg%3D%3D&numOfRows=10" +
//                    "&pageNo=1&depPlaceId="+starT+"&arrPlaceId="+enD+"&depPlandTime="+datE+"&trainGradeCode=02";    URL xmlUrl;
//            String returnResurlt = "";   //파싱 결과값을 받을 변수
//
//            try {
//                Log.d("시작", "시작1");
//                boolean arrplacename= false;    //도착역
//                boolean arrplandtime = false;   //도착일시
//                boolean depplacename = false;   //출발역
//                boolean depplandtime = false;   //출발일시
//                boolean traingradename = false; //열차이름
//                boolean item = false;
//                Log.d("시작", "시작2");
//
//                //HTTP 프로토골 기반의 소켓 통신하는 과정
//                //참고) https://javacan.tistory.com/entry/35
//                xmlUrl = new URL(url);
//                xmlUrl.openConnection().getInputStream();
//
//                //XML 분석을 위한 XmlPullParserFactory 인스턴스를 취득한 후 같은 인스턴스의 newPullParser()을 호출
//                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//                XmlPullParser parser = factory.newPullParser();
//                parser.setInput(xmlUrl.openStream(), "utf-8");
//                Log.d("시작", "시작3");
//
//                int eventType = parser.getEventType();
//                while (eventType != XmlPullParser.END_DOCUMENT) {
//                    switch (eventType) {
//                        case XmlPullParser.START_DOCUMENT: {
//                            if ("item".equals(parser.getName())) {
//                                item = true;
//                                Log.d("시작", "시작4");
//                                break;
//                            }
//                        }
//                            //시작태그를 만나면 boolean값을 true로 바꿔줌
//                            case XmlPullParser.START_TAG: {
//                            if ("arrplacename".equals(parser.getName())) {
//                                arrplacename = true;
//                                Log.d("태그 받음", "시작");
//                            }
//                            if ("arrplandtime".equals(parser.getName())) {
//                                arrplandtime = true;
//                            }
//                            if ("depplacename".equals(parser.getName())) {
//                                depplacename = true;
//                            }
//                            if ("depplandtime".equals(parser.getName())) {
//                                depplandtime = true;
//                            }
//                            if ("traingradename".equals(parser.getName())) {
//                                traingradename = true;
//                            }
//                                break;
//                            }
//
//                            //text를 만나면 returnResurlt에 값을 저장
//                            case XmlPullParser.TEXT: {
//                            if (arrplacename) {
//                                returnResurlt += parser.getText() + "\n";
//                                Log.d("태그 받음", "태그 받음");
//                                arrplacename = false;
//                            }
//                            if (arrplandtime) {
//                                returnResurlt += parser.getText() + "\n";
//                                Log.d("태그 받음", "태그 받음");
//                                arrplandtime = false;
//                            }
//                            if (depplacename) {
//                                returnResurlt += parser.getText() + "\n";
//                                Log.d("태그 받음", "태그 받음");
//                                depplacename = false;
//                            }
//                            if (depplandtime) {
//                                returnResurlt += parser.getText() + "\n";
//                                Log.d("태그 받음", "태그 받음");
//                                depplandtime = false;
//                            }
//                            if (traingradename) {
//                                returnResurlt += parser.getText() + "\n\n\n";
//                                Log.d("태그 받음", "태그 받음");
//                                traingradename = false;
//                            }
//                            break;
//                            }
//
//                            case XmlPullParser.END_TAG:
//                                if ("itme".equals(parser.getName())) {
//                                    break;
//                                }
//                            case XmlPullParser.END_DOCUMENT:
//                                break;
//                        }
//                        eventType = parser.next();
//                    }
//                } catch(Exception e){
//                    e.printStackTrace();
//                    Log.d("에러..", "에러남 ....ㅎ");
//                }
//                return returnResurlt;
//            }
//
//            //onPostExecute()는 백그라운드 작업이 끝나면 호출되고 메모리 리소를 해체하는 작업을 함
//            //백그라운드 작업의 결과를 매개변수로 전달
//            @Override
//            protected void onPostExecute (String result){
//            textView.setText(result);       //결과값을 textview에 뿌려줌
//            }
//        }
//    }

