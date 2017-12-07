package com.zzw.anim.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zzw.anim.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 主界面
 * Created by zhengzw on 2017/12/4.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)
        btn_main_grid.setOnClickListener { startActivity(Intent(this, GridActivity::class.java)) }
        btn_main_list.setOnClickListener { startActivity(Intent(this, MixActivity::class.java)) }
    }
}