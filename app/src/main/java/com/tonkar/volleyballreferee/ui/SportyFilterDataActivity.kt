package com.tonkar.volleyballreferee.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tonkar.volleyballreferee.R
import com.tonkar.volleyballreferee.engine.database.VbrRepository
import com.tonkar.volleyballreferee.engine.sporty.parsers.SportyCourseParser
import com.tonkar.volleyballreferee.engine.sporty.parsers.SportyDateParser
import com.tonkar.volleyballreferee.engine.sporty.parsers.SportyStateParser
import com.tonkar.volleyballreferee.ui.rules.IntegerRuleAdapter
import com.tonkar.volleyballreferee.ui.util.UiUtils

// Sporty, screen to filter by courts, days and states
class SportyFilterDataActivity : AppCompatActivity() {

    private lateinit var dateSpinner:Spinner
    private lateinit var courtSpinner:Spinner
    private lateinit var stateSpinner:Spinner
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

    }

    private fun addListeners () {}

}