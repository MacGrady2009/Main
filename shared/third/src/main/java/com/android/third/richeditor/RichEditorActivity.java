package com.android.third.richeditor;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.android.third.R;


public class RichEditorActivity extends AppCompatActivity implements RichEditorContainer.Listener{

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    View rootView = getLayoutInflater().inflate(R.layout.activity_rich_editor,null);
    setContentView(rootView);
    RichEditorContainer container = new RichEditorContainer(rootView,this);
    container.setListener(this);
  }

  @Override
  public void onRichEditorChange(String htmlContent) {

  }
}
