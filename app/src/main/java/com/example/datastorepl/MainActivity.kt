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
import com.example.datastorepl.utils.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    val dataStore: DataStore<Preferences> by preferencesDataStore(name = "DATA_STORE")

    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        dataStoreManager = DataStoreManager(this)

        mainBinding.save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
               /* saveData(
                    mainBinding.edtKeySave1.text.toString(),
                    mainBinding.edtValueSave1.text.toString()
                )*/

                dataStoreManager.saveDataString(
                    mainBinding.edtKeySave1.text.toString(),
                    mainBinding.edtValueSave1.text.toString()
                )
            }
        }


        mainBinding.read.setOnClickListener {
           /* lifecycleScope.launch {
                val result = readData(
                    mainBinding.edtKeyRead1.text.toString()
                )
                mainBinding.tvResult.text = result ?: "Not found"
            }*/

            CoroutineScope(Dispatchers.Main).launch {
              val result = dataStoreManager.getDataStore(
                  mainBinding.edtKeyRead1.text.toString()
              )
                mainBinding.tvResult.text = result
            }

        }
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