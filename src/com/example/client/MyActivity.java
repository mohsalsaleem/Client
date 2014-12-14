package com.example.client;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
 
import java.util.ArrayList;
 
public class MyActivity extends Activity
{
    private ListView mList;
    private ArrayList<String> arrayList;
    private MyCustomAdapter mAdapter;
    private TCPClient mTcpClient;
 
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
 
        arrayList = new ArrayList<String>();
 
        final EditText editText = (EditText) findViewById(R.id.editText);
        Button send = (Button)findViewById(R.id.send_button);
 
        mList = (ListView)findViewById(R.id.list);
        mAdapter = new MyCustomAdapter(this, arrayList);
        mList.setAdapter(mAdapter);
 
         new connectTask().execute("");
 
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 
                String message = editText.getText().toString();
 
 
                arrayList.add("c: " + message);
 
				if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }
 
                
                mAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });
 
    }
 
    public class connectTask extends AsyncTask<String,String,TCPClient> {
 
        @Override
        protected TCPClient doInBackground(String... message) {
 
            
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                
                public void messageReceived(String message) {
                    
                    publishProgress(message);
                }
            });
            mTcpClient.run();
 
            return null;
        }
 
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
 
            arrayList.add(values[0]);
            mAdapter.notifyDataSetChanged();
        }
    }
}