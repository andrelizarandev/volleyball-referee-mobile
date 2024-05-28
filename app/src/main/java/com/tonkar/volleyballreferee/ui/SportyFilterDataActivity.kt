package com.tonkar.volleyballreferee.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.tonkar.volleyballreferee.R
import com.tonkar.volleyballreferee.engine.api.JsonConverters
import com.tonkar.volleyballreferee.engine.api.VbrApi
import com.tonkar.volleyballreferee.engine.api.model.ApiSportyPostRequestFilterGames
import com.tonkar.volleyballreferee.engine.api.model.ApiSportyPostResponseFilterGames
import com.tonkar.volleyballreferee.engine.database.VbrRepository
import com.tonkar.volleyballreferee.engine.database.model.SportyGameEntity
import com.tonkar.volleyballreferee.engine.sporty.parsers.SportyCourseParser
import com.tonkar.volleyballreferee.engine.sporty.parsers.SportyDateParser
import com.tonkar.volleyballreferee.engine.sporty.parsers.SportyStateParser
import com.tonkar.volleyballreferee.ui.rules.IntegerRuleAdapter
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection

// Sporty, screen to filter by courts, days and states
class SportyFilterDataActivity : AppCompatActivity() {

    private lateinit var dateSpinner:Spinner
    private lateinit var courtSpinner:Spinner
    private lateinit var stateSpinner:Spinner
    private lateinit var searchBtn:Button
    private val vbrRepository = VbrRepository(this)

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sporty_filter_data)
        initUi()
        configureToolbar()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun configureToolbar () {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle("Filtrar Sporty")
        setSupportActionBar(toolbar)
    }

    private fun initUi () {
        getComponents()
        addListeners()
    }

    private fun getComponents () {

        val courtList = vbrRepository.listCourts()
        val stateList = vbrRepository.listStates()
        val datesList = vbrRepository.listDates()
        val inflater = LayoutInflater.from(this)

        val courtArray = SportyCourseParser.parseCourtDataToString(courtList)
        val courtIndexes = SportyCourseParser.getIndexesFromCourList(courtList)

        val stateArray = SportyStateParser.parseStateDataToString(stateList)
        val stateIndexes = SportyStateParser.getIndexesFromStateList(stateList)

        val dateArray = SportyDateParser.parseDateDataToString(datesList);
        val dateIndexes = SportyDateParser.getIndexesFromDateList(datesList);

        courtSpinner = findViewById(R.id.sporty_filter_data_court_spinner)
        val courtAdapter = IntegerRuleAdapter(this, inflater, courtArray, courtIndexes)
        courtSpinner.adapter = courtAdapter

        dateSpinner = findViewById(R.id.sporty_filter_data_date_spinner)
        val dayAdapter = IntegerRuleAdapter(this, layoutInflater, dateArray, dateIndexes)
        dateSpinner.adapter = dayAdapter

        stateSpinner = findViewById(R.id.sporty_filter_data_state_spinner)
        val stateAdapter = IntegerRuleAdapter(this, layoutInflater, stateArray, stateIndexes)
        stateSpinner.adapter = stateAdapter

        searchBtn = findViewById(R.id.sporty_filter_data_search_btn)

    }

    private fun addListeners () {
        searchBtn.setOnClickListener { v -> fetchByFilters() }
    }

    private fun fetchByFilters () {

        val court = courtSpinner.selectedItem as Int
        val courtsList = vbrRepository.listCourts()
        val selectedCourt = courtsList[court]

        val date = dateSpinner.selectedItem as Int
        val datesList = vbrRepository.listDates()
        val selectedDate = datesList[date]

        val state = stateSpinner.selectedItem as Int
        val statesList = vbrRepository.listStates()
        val selectedState = statesList[state]

        val token = vbrRepository.sportyToken

        val obj = ApiSportyPostRequestFilterGames(token[0].token, selectedCourt.cve, selectedDate.date, selectedState.cve)

        VbrApi.getInstance().postSportyFilters(obj, this, object : Callback {
            override fun onFailure(call: Call, e: IOException) { call.cancel() }
            override fun onResponse(call: Call, response:Response) {
                if (response.code != HttpURLConnection.HTTP_OK) { call.cancel() }
                else {
                    val resp = JsonConverters.GSON.fromJson(response.body!!.string(), ApiSportyPostResponseFilterGames::class.java)
                    vbrRepository.deleteAllSportyGames()
                    for (game in resp.juegos) {
                        val parsedContent = Gson().toJson(game)
                        val entity = SportyGameEntity(game.cve, parsedContent)
                        vbrRepository.insertSportyGame(entity)
                    }
                    val intent = Intent(this@SportyFilterDataActivity, ListSportyGamesActivity::class.java)
                    startActivity(intent)
                }
            }
        })

    }

}