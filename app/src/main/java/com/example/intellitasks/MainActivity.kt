/*Paquetes*/
package com.example.intellitasks

/*Imports*/
import android.content.Intent // Importar Intent para ListActivity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth // Importar FirebaseAuth

/*Se implementa la interfaz FragmentCommunicator*/
class MainActivity : AppCompatActivity(), FragmentCommunicator {

    private lateinit var navController: NavController
    private lateinit var lottieLoaderView: LottieAnimationView // Cambiado el nombre para mayor claridad

    private var loaderRequestCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        /*Set up navigation*/
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        /*Se hace referencia al Lottie AnimationView*/
        lottieLoaderView = findViewById(R.id.view)

        /*Manejar la navegación inicial: si el usuario ya está logeado, ir directo a ListActivity*/
        /*Requisito: Mantener sesión de usuario*/
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            /*Si el usuario ya está logeado, navegar directamente a ListActivity*/
            val intent = Intent(this, ListActivity::class.java)
            /*Flags para limpiar el historial de actividades*/
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish() /*Finaliza MainActivity para que el usuario no pueda volver al login/splash con el botón de atrás*/
            return /*Se sale de onCreate para no configurar más la UI de MainActivity*/
        }

        /*Mostrar/Ocultar el loader en el SplashFragment y otros casos*/
        navController.addOnDestinationChangedListener { _, destination, _ ->
            /* Si la animación Lottie NO está activa por una petición de loader de un ViewModel,
               entonces su visibilidad se puede controlar por el SplashFragment.*/
            /* Si ya está activa por un ViewModel, la visibilidad la controla el ViewModel.*/
            if (loaderRequestCount == 0) { /*Solo si no hay loaders activos por ViewModels*/
                if (destination.id == R.id.splashFragment) {
                    showLoaderInternal() /*Llama a la función interna para mostrar el Lottie*/
                } else {
                    hideLoaderInternal() /*Llama a la función interna para ocultar el Lottie*/
                }
            }
            /* Si loaderRequestCount > 0, significa que un ViewModel está pidiendo el loader,
               por lo que no lo ocultamos aunque salgamos del SplashFragment.*/
        }

        /* Barra de estado, navegación, etc.*/
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /*Implementación de los métodos de FragmentCommunicator*/
    /*Requisito: "Implementar el Loader usando el FragmentCommunicator en su actividad"*/
    override fun showLoader() {
        loaderRequestCount++ /*Se incrementa el contador en 1*/
        if (loaderRequestCount == 1) { /*Si es la primera petición, muestra el loader*/
            showLoaderInternal()
        }
    }

    override fun hideLoader() {
        loaderRequestCount-- /*Se decrementa el contador en 1*/
        if (loaderRequestCount <= 0) { /*Si no hay más peticiones, oculta el loader*/
            loaderRequestCount = 0 /*Se asegura que no baje a cero*/
            hideLoaderInternal()
        }
    }

    override fun openUpdateTaskFragment(task: Task, userId: String) {
        /*No se usa en MainActivity, pero se requiere por la interface FragmentCommunicator*/
    }

    /*Funciones internas para controlar la visibilidad y animación del Lottie*/
    private fun showLoaderInternal() {
        lottieLoaderView.visibility = View.VISIBLE
        lottieLoaderView.playAnimation()
    }

    private fun hideLoaderInternal() {
        lottieLoaderView.visibility = View.GONE
        lottieLoaderView.pauseAnimation() /*Detiene la animación para liberar recursos*/
    }
}