package timrapp.bigjokes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import timrapp.bigjokes.ui.main.JokeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, JokeFragment.newInstance())
                    .commitNow()
        }
    }
}