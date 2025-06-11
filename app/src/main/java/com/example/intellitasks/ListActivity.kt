package com.example.intellitasks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list) // Infla el layout que contiene el FragmentContainerView

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, PendingsFragment()) // R.id.fragment_container_list es el ID colocado en activity_list.xml
                .commit()
        }
    }
}