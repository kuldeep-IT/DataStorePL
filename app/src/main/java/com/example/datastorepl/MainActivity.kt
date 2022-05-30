package com.example.datastorepl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.datastorepl.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    val dataStore: DataStore<Preferences> by preferencesDataStore(name = "DATA_STORE")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mainBinding.save.setOnClickListener {
            lifecycleScope.launch {
                saveData(
                    mainBinding.edtKeySave1.text.toString(),
                    mainBinding.edtValueSave1.text.toString()
                )
            }
        }


        mainBinding.read.setOnClickListener {
            lifecycleScope.launch {
                val result = readData(
                    mainBinding.edtKeyRead1.text.toString()
                )
                mainBinding.tvResult.text = result ?: "Not found"
            }



        }

//        setContentView(R.layout.activity_main)

    }


    //Save shared preference
    private suspend fun saveData(key: String, value: String?) {
        val dataStoreKey = stringPreferencesKey(key)

        dataStore.edit {
            it[dataStoreKey] = value!!
        }

    }


    //Read shared preference
    private suspend fun readData(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)

        val preferences = dataStore.data.first()

        return preferences[dataStoreKey]
    }

}