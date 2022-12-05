package com.example.randomuser

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL


class MainActivity : AppCompatActivity() {

    var tv_name: TextView? = null
    var tv_email: TextView? = null
    var tv_gender: TextView? = null
    var tv_birthdate: TextView? = null
    var tv_age: TextView? = null
    var iv_user_picture: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_name = findViewById(R.id.tv_name)
        tv_email = findViewById(R.id.tv_email)
        tv_gender = findViewById(R.id.tv_gender)
        tv_birthdate = findViewById(R.id.tv_birthdate)
        tv_age = findViewById(R.id.tv_age)
        iv_user_picture = findViewById(R.id.iv_user_picture)
        findViewById<View>(R.id.btn_generate).setOnClickListener {
            val nt: NetworkTask = NetworkTask()
            nt.execute()

        }
        
    }

    internal inner class NetworkTask :
        AsyncTask<Any?, Any?, Any?>() {
        override fun doInBackground(objects: Array<Any?>): Any? {
            try {
                val url = URL("https://randomuser.me/api/")
                val `is` = url.openStream()
                val buffer = ByteArray(4096)
                val sb = StringBuilder("")
                while (`is`.read(buffer) != -1) {
                    sb.append(String(buffer))
                }
                Log.i("apiresponse", sb.toString())
                try {
                    val obj = JSONObject(sb.toString())
                    val results = obj.getJSONArray("results")
                    val user = results.getJSONObject(0)
                    val nameObj = user.getJSONObject("name")
                    val name =
                        nameObj.getString("title") + ". " + nameObj.getString("first") + " " + nameObj.getString(
                            "last"
                        )
                    publishProgress(name, 0)
                    val email = user.getString("email")
                    publishProgress(email, 1)
                    val gender = user.getString("gender")
                    publishProgress(gender, 2)
                    val image_url = user.getJSONObject("picture").getString("medium")
                    publishProgress(image_url, 3)
                    val birthdate = user.getJSONObject("dob").getString("date")
                    publishProgress(birthdate, 4)
                    val age = user.getJSONObject("dob").getString("age")
                    publishProgress(age, 5)
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }


        override fun onProgressUpdate(values: Array<Any?>) {
            Toast.makeText(this@MainActivity, values[0].toString(), Toast.LENGTH_SHORT).show()
            when ((values[1].toString() + "").toInt()) {
                0 -> tv_name!!.text =
                    "Name: " + values[0].toString()
                1 -> tv_email!!.text = "Email: " + values[0].toString()
                2 -> tv_gender!!.text = "Gender: " + values[0].toString()
                3 -> {
                    Picasso.get().load(values[0].toString()).into(iv_user_picture)
                    tv_birthdate!!.text = "Date: " + values[0]
                    tv_age!!.text = "Age: " + values[0]
                }
                4 -> {
                    tv_birthdate!!.text = "Date: " + values[0]
                    tv_age!!.text = "Age: " + values[0]
                }
                5 -> tv_age!!.text = "Age: " + values[0]
            }
        }
    }
}
