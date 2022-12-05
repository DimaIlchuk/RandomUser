package com.example.randomuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val splashScreenStarter: Thread = object : Thread() {
            override fun run() {
                try {
                    var delay = 0
                    while (delay < 2000) {
                        sleep(150)
                        delay = delay + 100
                    }
                    startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    finish()
                }
            }
        }
        splashScreenStarter.start()
    }

}
