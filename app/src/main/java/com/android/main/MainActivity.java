package com.android.main;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import com.android.common.network.DownloadHelper;
import com.android.common.network.ProgressListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            DownloadHelper downloadHelper = new DownloadHelper(new ProgressListener() {

                @Override
                public void onStart() {
                    Log.d("wb005", "onStart");
                }

                @Override
                public void onProgress(long total, long progress) {
                }

                @Override
                public void onFinish(boolean success, Object result, String msg) {
                    Log.d("wb005", "onFinish success = " + success);
                    Log.d("wb005", "onFinish msg = " + msg);
                }
            });

            downloadHelper.downloadFile("https://oss.pgyer.com/be0736ca5475edf5e3b00b21cbba80c0.apk?auth_key=1595215097-7e9d2ab7542226b132a49f938eeeaf0d-0-1f1c62dfdfe914918e02508e7bb8531c", Environment.getExternalStorageDirectory().getPath(), "/test.apk");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}