package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import helper.HttpConnection;
import helper.LocationService;
import helper.Message;
import helper.StaticManager;
import listViewAdapter.ListViewAdapter_MainTab1;

public class Main_Activity extends AppCompatActivity {

    //gps를 사용합니당.
    private LocationService locationService;
    private ListView listView;
    private ListViewAdapter_MainTab1 listViewAdapterMainTab1;
    private ArrayList<String> name; //리스트에 들어가는 데이터
    private ArrayList<String> distance; //리스트에 들어가는 데이터
    private SwipeRefreshLayout swipeRefreshLayout; //당기면 새로고치는 레이아웃

    private FloatingActionButton fab;
    private FloatingActionButton flagMe;

    private HashMap<String, String> dataMap = new HashMap<String, String>();
    private HashMap<String, String> nicknameMap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//타이틀바 없애기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);

        name = new ArrayList<String>();
        distance = new ArrayList<String>();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        flagMe = (FloatingActionButton) findViewById(R.id.flagForFindingMe);

        //사람들 정보를 받아옴.
        getPeopleData();
        //리스트뷰 세팅
        listViewSetting();
        //리스너들 불러옴.
        setListners();




        locationService = new LocationService();
        locationService.startLocationService();
    }


    private void recodeGPS() {
        //gps 받아서 레코딩 함.
        boolean gps = StaticManager.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gps) {
            StaticManager.gps = true;
            StaticManager.testToastMsg("Turn on the gps.\nOther people can see you now.");
        } else {
//            locationService.enableGPSSetting(gps);
            StaticManager.gps = false;
            updateMyGPS("0 0"); //0으로 내 gps를 업데이트 함.
            StaticManager.testToastMsg("Turn off the gps.\nOther people can't see you now.");

        }
    }



    //로컬 브로드캐스트 해서 받아온 것. 바로 위의 recodeGPS에서 방송 한 거임.
    String message;
    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // db_login.php로 보낸 결과값을 여기서 받음.
            message = intent.getStringExtra("gps data");

            if(message!=null) {
                updateMyGPS(message); //계속 업데이트 함
                Log.d("Main_Activity", "gps data get from broadcast");

            }

            message = intent.getStringExtra("chatting_dialog");
            Log.d("Main_Activity", "chatting_dialog에서 브로드캐스트 리시브");

            if (message != null) {
                if (message.equals("no")) {
                    StaticManager.testToastMsg("I am sorry ... ");
                } else {
                    StaticManager.testToastMsg("start to talk to "+message);

                    Log.d("Main_Activity", "start to talk to " + message);
                    Intent in = new Intent(StaticManager.applicationContext, ChattingActivity.class);
                    in.putExtra("oppoNickname", message);
                    in.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    StaticManager.applicationContext.startActivity(in); //이쪽으로 가서 채팅.

                }
            }
            //사람들 정보를 받음
            message = intent.getStringExtra("db_getProfile.php");
            Log.d("Main_Activity", "db_getProfile.php에서 브로드캐스트 리시브");
            if (message != null) {
                dataParser(message); //파싱하고
                finishRefreshing(); //새로고침을 끝냄.
            }


            Log.d("LoginActivity", "local broadcast receiver works");

        }
    };
    private void updateMyGPS(String data) {
        if(data==null) return;
        Log.d("Main_Activity", "update gps call");
        String latitude = data.substring(0, data.indexOf(" ")).trim().toString();
        String longitude = data.substring(data.indexOf(" ") + 1).trim().toString();

        String[] key = {"idpw", "latitude", "longitude"};
        String[] val = {
                String.valueOf(StaticManager.idpw),
                latitude,
                longitude
        };

        //db_updateGPS.php에 업데이트 해달라고 요청함.
        Log.d("Main_Activity", val[0] + " " + val[1] + " " + val[2] + " are sent");
        HttpConnection httpConnection = new HttpConnection();
        httpConnection.connect("http://" + StaticManager.ipAddress + "/eyeballs/db_updateGPS.php", "db_updateGPS.php", key, val);

        Log.d("Main_Activity", "send http msg to db_updateGPS.php");
    }



    private void setListners(){

        //당겨서 새로고침
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPeopleData();
                Log.d("Main_Activity", "refresh starts listView");
            }
        });
        //리스트뷰의 아이템을 클릭하면..!
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showDialogMsg(position);
            }
        });

        //프로필을 변경하는 버튼을 누르면
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Main_Activity.this, EditProfileActivity.class);
                in.putExtra("path", "editProfile");
                startActivity(in);
            }
        });

        //플래그를 조절하는 버튼을 누르면
        flagMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GPS를 기록하자.
                recodeGPS();
            }
        });
    }

    //에소프레소
    private void showDialogMsg(int position){
        Log.d("Main_Activity", "[" + name.get(position).toString() + "]의 \n[" + dataMap.get(nicknameMap.get(name.get(position).toString()).toString()) + "]을 받음");

        StringTokenizer token = new StringTokenizer(dataMap.get(nicknameMap.get(name.get(position).toString()).toString()), "*");
        String nickname = token.nextToken();
        String sex = token.nextToken();
        String comment = token.nextToken();

        String msg =
                "name : \n" + nickname + "\n" +
                        "distance : \n" + distance.get(position) + "\n" +
                        "sex : " + sex + "\n" +
                        "comment : " + comment + "\n\n" +
                        "do you want to chat?\n";
        Message.yesNoMsgShow(msg, "chatting_dialog", name.get(position), "no", Main_Activity.this);
    }

    //사람들 정보를 받음
    private void getPeopleData() {

        HttpConnection httpConnection = new HttpConnection();
        String[] key = {"idpw"}; //나의 idpw를 줌.
        String[] val = {String.valueOf(StaticManager.idpw)};
        httpConnection.connect("http://" + StaticManager.ipAddress + "/eyeballs/db_getProfile.php", "db_getProfile.php", key, val);

    }


    //사람들 정보를 받아오면 여기서 리스트에 올라가게 됨. 맨 처음 나오는 두 값이 내 좌표값.
    private void listViewSetting() {
     //받아온 사람들 정보를 파싱함. 파싱한 값은 name과 distance arrayList에 들어감


        //아래는 리스트뷰 세팅---------------
        listView = (ListView) findViewById(R.id.listViewMainTab1);
        //이렇게 어댑터를 생성하고 나면 리스트 다루는 일은 어댑터가 도맡아 한다.
        listViewAdapterMainTab1 = new ListViewAdapter_MainTab1(this, name, distance);
        //리스트뷰는 단지 보여주는 역할만 할 뿐.
        listView.setAdapter(listViewAdapterMainTab1);


    }


    public ArrayList<String> getName() {return name;}
    public ArrayList<String> getDistance() {return distance;}

    public void callDataParser(String data){
        dataParser(data);
    }

    //데이터를 리스트에 정렬해서 넣어주는 녀석
    private void dataParser(String data) {
        Log.d("Main_Activity", "parsing data : "+data);
        HashMap<String, Double> map = new HashMap<String, Double>();
        dataMap.clear(); //데이터맵 싹 지움
        name.clear(); //네임 싹 지움
        distance.clear(); //거리 싹 지움 초 기 화!
        nicknameMap.clear(); //닉네임-idpw 맵 싹 지움
        listViewAdapterMainTab1.notifyDataSetChanged();
        StringTokenizer token = new StringTokenizer(data, "*");

        while (token.hasMoreTokens()) {
            String idpw = token.nextToken();
            String nickname = token.nextToken();
            String nickname_sex_comment = nickname + "*" + token.nextToken() + "*" + token.nextToken();
            String temp = token.nextToken();
            Double dis = Double.valueOf(temp.substring(0, temp.indexOf(".")+5));

            //map과 dataMap에 각각 idpw를 키로 두고 데이터를 넣음.
            map.put(idpw, dis);
            dataMap.put(idpw, nickname_sex_comment);
            nicknameMap.put(nickname, idpw);
            Log.d("Main_Activity", "idpw:" + idpw + " nickname_sex_comment:" + nickname_sex_comment + " dis:" + dis);

        }//while

        //정렬
        Iterator it = StaticManager.sortByValue(map).iterator();

        while (it.hasNext()) {
            String temp = (String) it.next();
            String nickname = dataMap.get(temp).toString();
            token = new StringTokenizer(nickname, "*");
            name.add(token.nextToken());
            distance.add(String.valueOf(map.get(temp)));

            Log.d("Main_Activity", "name:" + temp + " distance:" + map.get(temp));
//            System.out.println(temp + " = " + map.get(temp));
        }



    }

    private void finishRefreshing(){
        listViewAdapterMainTab1.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        Log.d("Main_Activity", "refresh finishs listView");
    }














    public void onResume() {
        super.onResume();
        //이것도 나중에 스태틱으로 바꿔주자. 여기서 특별히 다르게 처리해야 할 것은 없으니까.
        // Register mMessageReceiver to receive messages. 브로드캐스트 리시버 등록
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, new IntentFilter("localBroadCast"));
        locationService.requestGPS(); //gps 다시 시작
    }

    protected void onPause() {
        //이것도 나중에 스태틱으로 바꿔주자. 여기서 특별히 다르게 처리해야 할 것은 없으니까.
        // Unregister since the activity is not visible 브로드캐스트 리비서 해제
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalBroadcastReceiver);
        locationService.stopGPS(this); //gps 멈추기
        StaticManager.gps = false;
        updateMyGPS("0 0"); //0으로 내 gps를 업데이트 함.
        super.onPause();
    }
}
