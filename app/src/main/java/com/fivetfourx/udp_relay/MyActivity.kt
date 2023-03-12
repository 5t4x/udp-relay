package com.fivetfourx.udp_relay

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MyActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_layout)

        title = "UDP Relay"

        sharedPref = getSharedPreferences(prefsKey, MODE_PRIVATE)

        findViewById<Button>(R.id.btnStartService).let {
            it.setOnClickListener {
                log("START THE FOREGROUND SERVICE ON DEMAND")
                actionOnService(Actions.START)
            }
        }

        findViewById<Button>(R.id.btnStopService).let {
            it.setOnClickListener {
                log("STOP THE FOREGROUND SERVICE ON DEMAND")
                actionOnService(Actions.STOP)
            }
        }

        setupAddressInput(
            findViewById(R.id.inputRemote),
            findViewById(R.id.inputRemoteLayout),
            "inputRemote"
        )

        setupAddressInput(
            findViewById(R.id.inputRelay),
            findViewById(R.id.inputRelayLayout),
            "inputRelay"
        )
    }

    private fun setupAddressInput(input: TextInputEditText, layout: TextInputLayout, key: String) {
        input.text?.clear()
        input.text?.append(sharedPref.getString(key, defaultRemote))
        input.addTextChangedListener {
            if (!validateAddress(it.toString())) layout.error = "Invalid address"
            else {
                layout.error = null
                sharedPref.edit().putString(key, it.toString()).apply()
            }
        }
    }

    private fun actionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return
        Intent(this, Server::class.java).also {
            it.action = action.name
            log("Starting the service in >=26 Mode")
            startForegroundService(it)
        }
    }
}
