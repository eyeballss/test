package kr.ac.kookmin.embedded.mobilecloudchattingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

import helper.HttpConnection.HttpConnection;
import helper.StaticManager.StaticManager;

public class ChattingActivity extends AppCompatActivity {

    private Button sendBtn;
    private EditText editxtForChat;
    private String oppoNickname; //상대방 닉네임.
    private String myNickname = StaticManager.nickname; //나의 닉네임
    private Intent intent;
    private Handler handler;

    //서버 소켓
    private Socket socket;
    private ClientReceiver clientReceiver;
    private ClientSender clientSender;
    private StartNetwork startNetwork;

    //아래는 채팅 리스트
    private ListView mChattingList;
    private ArrayAdapter<String> mChattingAdapter; //이걸로 조종하면 됨.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//타이틀바 없애기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chatting);

        //상대방 아이디를 저장하는 곳
        intent = getIntent();
        oppoNickname = intent.getStringExtra("oppoNickname");

        //채팅에 필요한 뷰와 핸들러
        sendBtn = (Button) findViewById(R.id.sendBtn);
        editxtForChat = (EditText) findViewById(R.id.editxtForChat);
        handler = new Handler(Looper.getMainLooper());

        //채팅 리스트 객체 만들고 어댑터 적용
        mChattingList = (ListView) findViewById(R.id.chattingList);
        mChattingAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_chatting_items);
        mChattingList.setAdapter(mChattingAdapter);

        //채팅 쓰레드 시작
        socket = new Socket();
        startNetwork = new StartNetwork(socket);
        startNetwork.start();

        initializeChatRequest();
    }//onCreate

    private void initializeChatRequest() {
        Log.d("ChattingActivity", "call initializeChatRequest()");

        String[] key = {
                "sender",
                "receiver",
        };
        String[] val = {
                oppoNickname,
                myNickname,
        };
        HttpConnection httpConnection = new HttpConnection();
        httpConnection.connect("http://" + StaticManager.ipAddress + "/eyeballs/db_getChat.php", "db_getChat.php", key, val);

    }

    //서버에서 가져온 값을 알려주는 브로드캐스트 리시버
    public BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("ChattingActivity", "mLocalBroadcastReceiver");
            // db_login.php로 보낸 결과값을 여기서 받음.
            final String message = intent.getStringExtra("db_getChat.php");

            if (message != null) {
                if (!message.trim().equals("")) {

                    parser(message);
                }
            }

            Log.d("ChattingActivity", "local broadcast receiver is done");
        }
    };//mLocalBroadcastReceiver

    private void parser(String msg) {
        Log.d("ChattingActivity", "call parser()");
        StringTokenizer token = new StringTokenizer(msg, "*");
        while (token.hasMoreTokens()) {
            loadOnChatList(token.nextToken());
        }
    }//parser

    public void loadOnChatList(final String msg) {
        handler.post(new Runnable() { //VIEW 들을 만져줌.
            public void run() {
                mChattingAdapter.add(msg); //채팅창에 올림
            }
        });
        Log.d("ChattingActivity", "call loadOnChatList(). msg : "+msg);
    }//loadOnChatList

    //아래는 채팅 쓰레드
    public class StartNetwork extends Thread {
        private Socket socket;

        public StartNetwork(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                socket = new Socket(StaticManager.ipAddress, 5011);
                Log.d("Chatting activity", "start act success to connect");

                clientReceiver = new ClientReceiver(socket);
                clientSender = new ClientSender(socket);

                clientReceiver.start();
                clientSender.start();
            } catch (IOException e) {
                StaticManager.testToastMsg("network error!!!");
            }
        }
    }

    public class ClientSender extends Thread {
        Socket socket;
        DataOutputStream output;

        public ClientSender(Socket socket) {
            this.socket = socket;
            try {
                output = new DataOutputStream(socket.getOutputStream());
                write(StaticManager.nickname); //서버에 먼저 내 아이디를 보내줌. 나인 것을 등록.
                Log.d("Chatting activity", "startNetwork register my id in server");
            } catch (Exception e) {
            }
        }

        public void write(String msg) {
            try {
                output.writeUTF(msg);
            } catch (IOException e) {
                Log.d("ChattingActivity", "write method io exception");
            } catch (Exception e) {
                Log.d("ChattingActivity", "write method exception");
            }
        }

        @Override
        public void run() {
            while (output != null) {

                //send 버튼을 누르면
                sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d("Chatting activity", "startNetwork sendBtn clicked");

                        if (!editxtForChat.getText().toString().trim().equals("")) {
                            //위에 있는게 정상적인 예제고 아래가 하드코딩 한 것
                            String msg = oppoNickname + "*" + editxtForChat.getText().toString();
                            try {
                                Log.d("Chatting activity", "startNetwork ready to send");
                                write(msg); //서버로 보내고
                                Log.d("Chatting activity", "msg for Server : " + msg);

                                loadOnChatList(myNickname + " : " + editxtForChat.getText().toString()); //채팅창에 올리고

                                handler.post(new Runnable() { //VIEW 들을 만져줌.
                                    public void run() {
                                        editxtForChat.setText(""); //비움.
                                    }
                                });

                                Log.d("Chatting activity", "startNetwork success to send");
                            } catch (Exception e) {
                            }
                        }

                    }
                });//onClickListener


            }
        }

        public void stopDataOutputStream() {
            try {
                output.close();
                output = null;
            } catch (Exception e) {
            }
        }
    }

    public class ClientReceiver extends Thread {
        Socket socket;
        DataInputStream input;
        String inputStr;

        public ClientReceiver(Socket socket) {
            this.socket = socket;
            try {
                input = new DataInputStream(socket.getInputStream());
//                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
            }
        }

        public void run() {
            inputStr = null;
            while (input != null) {
                try {
                    //상대방으로부터 내용을 받아옴.
                    inputStr = input.readUTF();

                    Log.d("Chatting activity", "msg form Server : " + inputStr);
                    loadOnChatList(oppoNickname + " : " + inputStr);//채팅창에 올림

                    if (!inputStr.contains("is not here")) {
                        String[] key = {
                                "sender",
                                "receiver",
                                "talk"
                        };
                        String[] val = {
                                oppoNickname,
                                myNickname,
                                oppoNickname + " : " + inputStr
                        };

                        HttpConnection httpConnection = new HttpConnection();
                        httpConnection.connect("http://" + StaticManager.ipAddress + "/eyeballs/db_saveChat.php", "db_saveChat.php", key, val);
                    }

                } catch (IOException e) {
                }
            }
        }

        public void stopDataInputStream() {
            try {
                input.close();
                input = null;
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, new IntentFilter("localBroadCast"));
    }

    @Override
    protected void onPause() {
        Log.d("chatting activity", "onPause method call");

        try {
            if(socket!=null) socket.close();
            socket = null;
            startNetwork.interrupt();
            clientReceiver.stopDataInputStream();
            clientReceiver.interrupt();
            clientSender.stopDataOutputStream();
            clientSender.interrupt();

            Log.d("chatting activity", "socket and threads close");
        } catch (IOException e) {
            Log.d("chatting activity", "socket close fail");
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalBroadcastReceiver);
        super.onPause();
    }
}
