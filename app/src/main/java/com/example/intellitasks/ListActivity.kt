package com.example.intellitasks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView // ¡Asegúrate de importar esto!

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list) // Infla el layout que contiene tu FragmentContainerView

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, PendingsFragment()) // R.id.fragment_container_list es el ID que pusimos en activity_list.xml
                .commit()
        }
    }
}