package com.example.iabsample

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.iabsample.iabv4.BillingDataSource

class MainActivity : AppCompatActivity() {

    companion object {
        // define your valid in-app purchase or subscription items here
        private val VALID_IN_APP_SKU = arrayOf("IN_APP_SKU_1")
        private val VALID_SUB_SKU = arrayOf("SUB_1")
    }

    var billingDataSource: BillingDataSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize BillingDataSource object here
        billingDataSource = BillingDataSource.getInstance(
            application,
            VALID_IN_APP_SKU,
            VALID_SUB_SKU,
            null
        )

        val button:Button = findViewById(R.id.button1)
        button.setOnClickListener {
            // launch purchase flow for sku "IN_APP_SKU_1"
            billingDataSource?.launchBillingFlow(this, "IN_APP_SKU_1", null);
        }

        val textView: TextView = findViewById(R.id.text1)

        // check if specified sku purchased or not
        billingDataSource?.isPurchased("IN_APP_SKU_1")?.observe(this) {
            textView.text = if(it) "Purchased" else "Not purchased"
            button.isEnabled = !it
        }

        // watch for any new purchase and perform any response required
        billingDataSource?.observeNewPurchases()?.observe(this) {
            for(sku in it) {
                Log.d("DEBUG", "New purchase on sku:$sku")
            }
        }
    }

    override fun onResume() {
        // call BillingDataSource's resume() function to keep state updated
        billingDataSource?.resume()
        super.onResume()
    }
}