package com.bravovcloud.gezhaoyou.hlsplayer;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private final String  TAG = "LOG111";

    MediaPlayer video_player = null;  //
    SurfaceView video_surface;
    EditText hls_url_text = null;
    SurfaceHolder video_surfaceHolder;
    Button playptn, pauseptn, stopptn;  //视频播放控制按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //读取设置视频地址
        hls_url_text = (EditText) findViewById(R.id.hls_stream_address);
        SharedPreferences get_data = getSharedPreferences("data",MODE_PRIVATE);
        String hls_url = get_data.getString("hls_url","http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8");
        hls_url_text.setText(hls_url);
        Log.i("hls_url",hls_url);
        //
        playptn = (Button) findViewById(R.id.play_button);
        pauseptn = (Button) findViewById(R.id.pause_button);
        pauseptn.setEnabled(false);
        stopptn = (Button) findViewById(R.id.stop_button);
        stopptn.setEnabled(false);
        video_surface = (SurfaceView) findViewById(R.id.vedio_surface);

        video_surfaceHolder = video_surface.getHolder();
        video_surfaceHolder.addCallback(this);
        video_surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        video_player = new MediaPlayer();
        video_player.setScreenOnWhilePlaying(true);
        video_player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //button operate
        playptn.setOnClickListener(new View.OnClickListener() {//start
            @Override
            public void onClick(View view) {
                play();
            }
        });
        pauseptn.setOnClickListener(new View.OnClickListener() {//pause
            @Override
            public void onClick(View view) {
                if (video_player.isPlaying()) {
                    video_player.pause();
                }
                else {
                    video_player.start();
                }
            }
        });
        stopptn.setOnClickListener(new View.OnClickListener() {//stop
            @Override
            public void onClick(View view) {
                if (video_player.isPlaying()) {
                    video_player.stop();
                    pauseptn.setEnabled(false);
                    stopptn.setEnabled(false);
                }
            }
        });
    }

    private void play(){

        try {
            String url = hls_url_text.getText().toString();//"http://192.168.9.237/live/livestream.m3u8";
            if (!url.endsWith(".m3u8")){
                Toast note = Toast.makeText(MainActivity.this,"HLS流地址有误，请重新输入！例如：http://xxxxxx/xx/xx.m3u8",Toast.LENGTH_LONG);
                note.setGravity(Gravity.TOP,0,600);
                note.show();
                return;
            }
            //保存视频地址
            SharedPreferences.Editor save_data = getSharedPreferences("data",MODE_PRIVATE).edit();
            save_data.putString("hls_url",url.trim());
            save_data.commit();

            stopptn.setEnabled(true);
            pauseptn.setEnabled(true);

            video_player.reset();
            video_player.setDataSource(url.trim());
            video_player.prepare();
            video_player.start();
            video_player.setDisplay(video_surfaceHolder);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override //surface when 变得可见，surfaceview被创建；surfaceview隐藏前，surface被销毁。
    public void surfaceCreated(SurfaceHolder holder){
//        this.play();
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0){

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"999999999999999");
    }

    protected void onRestart(){
        super.onRestart();

    }
}
