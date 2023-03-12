package com.fivetfourx.udp_relay

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText

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

        findViewById<TextInputEditText>(R.id.inputRemote).let {
            it.text?.clear()
            it.text?.append(sharedPref.getString("inputRemote", defaultRemote))
            it.addTextChangedListener {
                sharedPref.edit().putString("inputRemote", it.toString()).apply()
            }
        }

        findViewById<TextInputEditText>(R.id.inputRelay).let {
            it.text?.clear()
            it.text?.append(sharedPref.getString("inputRelay", defaultRelay))
            it.addTextChangedListener {
                sharedPref.edit().putString("inputRelay", it.toString()).apply()
                false
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
