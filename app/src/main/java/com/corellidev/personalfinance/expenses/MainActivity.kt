package com.corellidev.personalfinance.expenses

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.corellidev.personalfinance.DaggerMainActivityComponent
import com.corellidev.personalfinance.MainActivityComponent
import com.corellidev.personalfinance.MainActivityModule
import com.corellidev.personalfinance.R
import com.corellidev.personalfinance.categories.CategoriesActivity
import com.corellidev.personalfinance.categories.CategoriesRepository
import com.corellidev.personalfinance.categories.CategoryModel
import com.corellidev.personalfinance.statistics.StatisticsActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), AddExpenseDialogFragment.AddClickListener,
        GoogleApiClient.OnConnectionFailedListener {
    private val RC_SIGN_IN: Int = 9003
    @Inject
    lateinit var expensesRepository: ExpensesRepository
    @Inject
    lateinit var categoriesRepository: CategoriesRepository
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

//        setGoogleSignIn()
        //TODO test code - remove when adding categories is done
        categoriesRepository.addCategory(CategoryModel("Food", Color.BLUE))
        categoriesRepository.addCategory(CategoryModel("Car", Color.RED))

        with (expenses_list) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            val categoriesMap = HashMap<String, CategoryModel>()
            categoriesRepository.getAllCategories().forEach({ item -> categoriesMap.put(item.name, item)})
            listAdapter = ExpensesListAdapter(this@MainActivity, categoriesMap)
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
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(dy > 0 ){
                        if(fab.isShown) fab.hide()
                    } else {
                        if(!fab.isShown) fab.show()
                    }
                }
            })
        }

        fab.setOnClickListener { view ->
            val addExpenseDialogFragment = AddExpenseDialogFragment()
            addExpenseDialogFragment.addClickListener = this
            addExpenseDialogFragment.show(supportFragmentManager, addExpenseDialogFragment.TAG)
        }
    }

    override fun onAddClick(expense: ExpenseModel) {
        listAdapter.items.add(expense)
        expensesRepository.addExpense(expense)
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
            R.id.action_categories -> {
                startActivity(Intent(this, CategoriesActivity::class.java))
                true
            }
            R.id.action_statistics -> {
                startActivity(Intent(this, StatisticsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//    private fun setGoogleSignIn() {
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.web_app_client_id))
//                .requestEmail()
//                .build()
//        val mGoogleApiClient = GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build()
//        sign_in_button.setOnClickListener({ view ->
//            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
//            startActivityForResult(signInIntent, RC_SIGN_IN)
//        })
//    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
//            handleSignInResult(result)
//        }
//    }

//    private fun handleSignInResult(result: GoogleSignInResult) {
//        Log.d("MyTag", "handleSignInResult:" + result.isSuccess + " tokenid = " + result.signInAccount?.idToken
//        + " email = " + result.signInAccount?.email)
//        if (result.isSuccess) {
//            Log.d("MyTag", "success")
//            expensesRepository.token = result.signInAccount?.idToken
//        } else {
//            Log.d("MyTag", "failure ")
//        }
//    }
}
