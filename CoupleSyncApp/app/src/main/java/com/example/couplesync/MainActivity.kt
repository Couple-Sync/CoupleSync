package com.example.couplesync

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.codepath.asynchttpclient.RequestHeaders
import okhttp3.Headers


class MainActivity : AppCompatActivity() {
    var restaurantImageURL = ""
    var restaurantName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val restaurantImageView = findViewById<ImageView>(R.id.restaurantImage)
        val nameTextView = findViewById<TextView>(R.id.restaurantName)

        getRandomRestaurant(restaurantImageView, nameTextView)
    }

    private fun getRandomRestaurant(imageView: ImageView, nameTextView: TextView) {
        val client = AsyncHttpClient()
        val apiKey = "Insert Key"
        val url = "https://api.yelp.com/v3/businesses/search?location=NewYork&limit=50"

        val headers = RequestHeaders()
        headers.set("Authorization", "Bearer $apiKey")
        client.get(url, headers, null, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Yelp", "API Request Success")
                val businesses = json.jsonObject.getJSONArray("businesses")
                val randomIndex = (0 until businesses.length()).random()
                val randomRestaurant = businesses.getJSONObject(randomIndex)

                val name = randomRestaurant.getString("name")
                val imageUrl = randomRestaurant.getString("image_url")

                Glide.with(this@MainActivity).load(imageUrl).into(imageView)
                nameTextView.text = name
            }
            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                Log.d("Yelp Error", "API Request Failed: $errorResponse")
            }
        })
    }
}
