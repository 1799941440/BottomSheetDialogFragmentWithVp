package com.wz.bottomsheetdialogfragmentwithvp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wz.bottomsheetdialogfragmentwithvp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.click.setOnClickListener {
            MyDialogImpl().show(supportFragmentManager, "MyDialogImpl")
        }
    }

}