package com.android.common.view.richeditor;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import androidx.annotation.IdRes;
import com.android.common.R;
import com.android.common.utils.KeyBoardUtil;

public class RichEditorContainer {
    private Activity mActivity;
    private RichEditor mEditor;
    private View mRootView;
    Listener mListener;
    HorizontalScrollView mHsEditorBar;

    public  RichEditorContainer(View rootView, Activity activity,Listener listener){
        mRootView = rootView;
        mActivity = activity;
        mListener = listener;
        initView();
    }

    public <T extends View> T findViewById(@IdRes int id) {
        return mRootView.findViewById(id);
    }


    public interface  Listener{
        void onRichEditorChange(String content);
    }

    public void initView(){
        mHsEditorBar = findViewById(R.id.hs_editor_bar);
        KeyBoardUtil.addGlobalLayoutListener(mActivity, new KeyBoardUtil.KeyboardListener() {
            @Override
            public void onKeyboardClosed() {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)mHsEditorBar.getLayoutParams();
                layoutParams.bottomMargin = 0;
                mHsEditorBar.setLayoutParams(layoutParams);
            }

            @Override
            public void onKeyboardOpened(int keyBoardHeight) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)mHsEditorBar.getLayoutParams();
                layoutParams.bottomMargin = keyBoardHeight;
            }
        });
        mEditor = findViewById(R.id.editor);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        //mEditor.setInputEnabled(false);

        mEditor.setOnTextChangeListener(htmlText -> {
            if (null != mListener){
                mListener.onRichEditorChange(htmlText);
            }
        });

        findViewById(R.id.action_undo).setOnClickListener(v -> mEditor.undo());

        findViewById(R.id.action_redo).setOnClickListener(v -> mEditor.redo());

        findViewById(R.id.action_bold).setOnClickListener(v -> {
                mEditor.setBold();
                v.setSelected(!v.isSelected());
            }
        );

        findViewById(R.id.action_italic).setOnClickListener(v -> {
            mEditor.setItalic();
            v.setSelected(!v.isSelected());
        });

        findViewById(R.id.action_subscript).setOnClickListener(v -> mEditor.setSubscript());

        findViewById(R.id.action_superscript).setOnClickListener(v -> mEditor.setSuperscript());

        findViewById(R.id.action_strikethrough).setOnClickListener(v -> {
            mEditor.setStrikeThrough();
            v.setSelected(!v.isSelected());
        });

        findViewById(R.id.action_underline).setOnClickListener(v -> {
            mEditor.setUnderline();
            v.setSelected(!v.isSelected());
        });

        findViewById(R.id.action_heading1).setOnClickListener(v -> mEditor.setHeading(1));

        findViewById(R.id.action_heading2).setOnClickListener(v -> mEditor.setHeading(2));

        findViewById(R.id.action_heading3).setOnClickListener(v -> mEditor.setHeading(3));

        findViewById(R.id.action_heading4).setOnClickListener(v -> mEditor.setHeading(4));

        findViewById(R.id.action_heading5).setOnClickListener(v -> mEditor.setHeading(5));

        findViewById(R.id.action_heading6).setOnClickListener(v -> mEditor.setHeading(6));

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(v -> mEditor.setIndent());

        findViewById(R.id.action_outdent).setOnClickListener(v -> mEditor.setOutdent());

        findViewById(R.id.action_align_left).setOnClickListener(v -> mEditor.setAlignLeft());

        findViewById(R.id.action_align_center).setOnClickListener(v -> mEditor.setAlignCenter());

        findViewById(R.id.action_align_right).setOnClickListener(v -> mEditor.setAlignRight());

        findViewById(R.id.action_blockquote).setOnClickListener(v -> mEditor.setBlockquote());

        findViewById(R.id.action_insert_bullets).setOnClickListener(v -> mEditor.setBullets());

        findViewById(R.id.action_insert_numbers).setOnClickListener(v -> mEditor.setNumbers());

        findViewById(R.id.action_insert_image).setOnClickListener(v -> mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG",
            "dachshund"));

        findViewById(R.id.action_insert_link).setOnClickListener(v -> mEditor.insertLink("https://github.com/wasabeef", "wasabeef"));
        findViewById(R.id.action_insert_checkbox).setOnClickListener(v -> mEditor.insertTodo());
    }
}
