package br.com.actaholding.mediaplayer.podcast

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import br.com.actaholding.mediaplayer.R
import kotlinx.android.synthetic.main.activity_podcast.*

class PodcastActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_podcast)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        collapsingToolbar.setExpandedTitleColor(ActivityCompat.getColor(this, android.R.color.transparent))
        collapsingToolbar.setCollapsedTitleTextColor(ActivityCompat.getColor(this, android.R.color.transparent))




    }
}
