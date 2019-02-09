package com.example.tree.zhihu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PutQuestionActivity extends AppCompatActivity implements View.OnClickListener{

    Button bu_ask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_question);

        bu_ask = (Button)findViewById(R.id.ask_close);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ask_close:

        }
    }
}
