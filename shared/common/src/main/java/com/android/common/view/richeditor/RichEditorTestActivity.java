package com.android.common.view.richeditor;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.android.common.R;


public class RichEditorTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().inflate(R.layout.activity_rich_editor, null);
        setContentView(rootView);
        new RichEditorContainer(rootView, this, htmlText -> {
            Log.d("wb005", "htmlContent = " + htmlText);
        });
    }
}
