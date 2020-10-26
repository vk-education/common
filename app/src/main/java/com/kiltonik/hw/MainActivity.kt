package com.kiltonik.hw

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), RecycleFragment.FragmentListener {

    companion object {
        const val BIG_NUMBER = "BIG_NUMBER"
        const val RECYCLE = "RECYCLE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.frame, RecycleFragment.newInstance(), RECYCLE)
                .commitAllowingStateLoss()
    }

    override fun onNumberClicked(number: Int) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(RECYCLE)
            .replace(R.id.frame, NumberFragment.newInstance(number), BIG_NUMBER)
            .commitAllowingStateLoss()
    }
}
