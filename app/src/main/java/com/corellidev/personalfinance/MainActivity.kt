package com.corellidev.personalfinance

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var expensesRepository: ExpensesRepository
    lateinit var listAdapter: ExpensesListAdapter
    companion object {
        val TAG = "MainActivity"
        lateinit var mainActivityComponent: MainActivityComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivityComponent = DaggerMainActivityComponent.builder()
                .mainActivityModule(MainActivityModule(application))
                .build()
        mainActivityComponent.inject(this)
        setSupportActionBar(toolbar)

        with (expenses_list) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            listAdapter = ExpensesListAdapter()
            adapter = listAdapter
            expensesRepository.getAllExpenses()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                    {
                        expenses -> listAdapter.setContent(expenses.toMutableList())
                    },
                    {
                        error -> Log.d(TAG, "getAllExpenses error " + error.toString())
                    })
        }

        fab.setOnClickListener { view ->
            val addExpenseDialogFragment = AddExpenseDialogFragment()
            addExpenseDialogFragment.show(supportFragmentManager, addExpenseDialogFragment.TAG)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
