package com.example.tree.zhihu;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tree.zhihu.fragment.CollegeFragment;
import com.example.tree.zhihu.fragment.IdearFragment;
import com.example.tree.zhihu.fragment.MessageFragment;
import com.example.tree.zhihu.fragment.MoreFragment;
import com.example.tree.zhihu.fragment.ShouyeFragment;

import org.w3c.dom.Text;

import java.net.IDN;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "MainActivity";

    ImageButton bu_shouye;
    ImageButton bu_idear;
    ImageButton bu_college;
    ImageButton bu_message;
    ImageButton bu_more;
    TextView shouye;
    TextView college;
    TextView idear;
    TextView message;
    TextView more;
    TextView te_ask;
    ShouyeFragment shouyeFragment;
    IdearFragment idearFragment;
    CollegeFragment collegeFragment;
    MessageFragment messageFragment;
    MoreFragment moreFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化按钮以及文字控件
        init();
        selectTab(0);
        Log.d(TAG, "onCreate: ");
    }


    private void init() {
        shouye = (TextView) findViewById(R.id.shouye);
        college = (TextView)findViewById(R.id.college);
        idear = (TextView)findViewById(R.id.idear);
        message = (TextView)findViewById(R.id.message);
        more =  (TextView)findViewById(R.id.more);
        bu_shouye = (ImageButton) findViewById(R.id.id_bu_shouye_img);
        bu_idear = (ImageButton) findViewById(R.id.id_bu_idear_img);
        bu_college = (ImageButton) findViewById(R.id.id_bu_college_img);
        bu_message = (ImageButton) findViewById(R.id.id_bu_message_img);
        bu_more = (ImageButton) findViewById(R.id.id_bu_more_img);
        bu_shouye.setOnClickListener(this);
        bu_idear.setOnClickListener(this);
        bu_college.setOnClickListener(this);
        bu_more.setOnClickListener(this);
        bu_message.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        resetImgs();
        switch (v.getId()) {
            case R.id.id_bu_shouye_img:
                selectTab(0);
                break;
            case R.id.id_bu_idear_img:
                selectTab(1);
                break;
            case R.id.id_bu_college_img:
                selectTab(2);
                break;
            case R.id.id_bu_message_img:
                selectTab(3);
                break;
            case R.id.id_bu_more_img:
                selectTab(4);
                break;
        }
    }

    //进行选中Tab的处理
    private void selectTab(int i) {
        //获取FragmentManager对象
        FragmentManager manager = getSupportFragmentManager();
        //获取FragmentTransaction对象
        FragmentTransaction transaction = manager.beginTransaction();
        //先隐藏所有的Fragment
        hideFragment(transaction);
        switch (i) {
            //当选中点击的是首页的Tab时
            case 0:
                //设置首页的ImageButton和TextView为蓝色
                bu_shouye.setImageResource(R.drawable.ic_bottomtabbar_feed_blue);
                shouye.setTextColor(0xFF0283FC);
                //如果首页对应的Fragment没有实例化，则进行实例化，并显示出来
                if (shouyeFragment == null) {
                    shouyeFragment = new ShouyeFragment();
                    transaction.add(R.id.id_content, shouyeFragment);
                } else {
                    //如果首页对应的Fragment已经实例化，则直接显示出来
                    transaction.show(shouyeFragment);
                }
                break;
            case 1:
                bu_idear.setImageResource(R.drawable.ic_bottomtabbar_idear_blue);
                idear.setTextColor(0xFF0283FC);
                if (idearFragment == null) {
                    idearFragment = new IdearFragment();
                    transaction.add(R.id.id_content, idearFragment);
                } else {
                    transaction.show(idearFragment);
                }
                break;
            case 2:
                bu_college.setImageResource(R.drawable.ic_bottomtabbar_shop_blue);
                college.setTextColor(0xFF0283FC);
                if (collegeFragment == null) {
                    collegeFragment = new CollegeFragment();
                    transaction.add(R.id.id_content, collegeFragment);
                } else {
                    transaction.show(collegeFragment);
                }
                break;
            case 3:
                bu_message.setImageResource(R.drawable.ic_bottomtabbar_notification_blue);
                message.setTextColor(0xFF0283FC);
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.id_content, messageFragment);
                } else {
                    transaction.show(messageFragment);
                }
                break;
            case 4:
                bu_more.setImageResource(R.drawable.ic_bottomtabbar_more_blue);
                more.setTextColor(0xFF0283FC);
                if (moreFragment == null) {
                    moreFragment = new MoreFragment();
                    transaction.add(R.id.id_content, moreFragment);
                } else {
                    transaction.show(moreFragment);
                }
                break;
        }
        transaction.commit();

    }

    //已经形成的Fragment隐藏
    private void hideFragment(FragmentTransaction transaction) {
        if (shouyeFragment != null) {
            transaction.hide(shouyeFragment);
        }
        if (collegeFragment != null) {
            transaction.hide(collegeFragment);
        }
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (moreFragment != null) {
            transaction.hide(moreFragment);
        }
        if (idearFragment != null) {
            transaction.hide(idearFragment);
        }
    }

    //将四个ImageButton置为灰色
    private void resetImgs() {
        bu_shouye.setImageResource(R.drawable.ic_bottomtabbar_feed);
        bu_message.setImageResource(R.drawable.ic_bottomtabbar_notification);
        bu_more.setImageResource(R.drawable.ic_bottomtabbar_more);
        bu_idear.setImageResource(R.drawable.ic_bottomtabbar_idear);
        bu_college.setImageResource(R.drawable.ic_bottomtabbar_shop);
        shouye.setTextColor(0xFF9A9A9A);
        college.setTextColor(0xFF9A9A9A);
        message.setTextColor(0xFF9A9A9A);
        more.setTextColor(0xFF9A9A9A);
        idear.setTextColor(0xFF9A9A9A);

    }
}
