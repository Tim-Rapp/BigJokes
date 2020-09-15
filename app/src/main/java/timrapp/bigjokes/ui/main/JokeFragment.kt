package timrapp.bigjokes.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.joke_fragment.*
import timrapp.bigjokes.R

class JokeFragment : Fragment() {

    companion object {
        fun newInstance() = JokeFragment()
    }

    private lateinit var viewModel: JokeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(JokeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.joke_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            updateJoke()
        } else {
            // Fragment was recreated, display last joke
            jokeTextView.text = viewModel.lastJokeText
        }

        swipeRefresh.setOnRefreshListener {
            updateJoke()
        }
    }

    private fun updateJoke() {
        viewModel.getJoke().observe(this) {
            // viewmodel changed, update the UI
            jokeTextView.text = it
            swipeRefresh.isRefreshing = false
        }
    }

}