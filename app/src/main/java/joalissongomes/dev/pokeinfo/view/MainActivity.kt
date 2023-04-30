package joalissongomes.dev.pokeinfo.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.size
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import joalissongomes.dev.pokeinfo.R
import joalissongomes.dev.pokeinfo.model.PokemonDetail
import joalissongomes.dev.pokeinfo.presentation.PokemonPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), PokemonView {

    private lateinit var presenter: PokemonPresenter
    private lateinit var rvMain: RecyclerView
    private val pokemonList = mutableListOf<PokemonDetail>()
    private val adapter = MainAdapter(pokemonList)
    private val scope = CoroutineScope(Dispatchers.Main)
    private var isLoading = false
    private var offset = 0
    private var pokemonTotal = 0
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progress_bar)

        // initialize presenter
        presenter = PokemonPresenter(this)

        rvMain = findViewById(R.id.rv_main)
        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.adapter = adapter

        // get first pokemon list items
        if (rvMain.size == 0) {
            scope.launch {
                presenter.findPokemonDetail(offset)
            }
        }

        addInfiniteScroll()
        addDividerIntoRecyclerView()
    }

    override fun showPokemonDetail(pokemonListResult: List<PokemonDetail>, count: Int) {
        if (pokemonListResult.isNotEmpty()) {
            pokemonList.addAll(pokemonListResult)
            pokemonTotal = count
            adapter.notifyDataSetChanged()
        }
        isLoading = false // sets to false when api call completes
        hideProgressBar()
    }

    override fun showFailurePokemonDetail(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        isLoading = false // re-enables scroll detection in case of failure
        hideProgressBar()
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    // add Infinite Scroll
    private fun addInfiniteScroll() {
        rvMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = rvMain.layoutManager as LinearLayoutManager
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                // Check that the position of the last visible item is correct
                // Checks that it is not already loading a new list
                if (totalItemCount - 1 == lastVisibleItem && totalItemCount < pokemonTotal && !isLoading) {
                    isLoading = true
                    offset = totalItemCount // set pokemon list size to offset on request
                    getNewPokemonListItems()
                }
            }
        })
    }

    // get new pokemon list items
    private fun getNewPokemonListItems() {
        scope.launch {
            presenter.findPokemonDetail(offset)
        }
    }

    // add divider into recyclerView
    private fun addDividerIntoRecyclerView() {
        val dividerItemDecoration = DividerItemDecoration(
            rvMain.context,
            DividerItemDecoration.VERTICAL
        )
        ContextCompat.getDrawable(this, R.drawable.divider)?.let {
            dividerItemDecoration.setDrawable(
                it
            )
        }
        rvMain.addItemDecoration(dividerItemDecoration)

        if (pokemonList.size > 200) {
            rvMain.removeItemDecoration(dividerItemDecoration)
        }
    }
}