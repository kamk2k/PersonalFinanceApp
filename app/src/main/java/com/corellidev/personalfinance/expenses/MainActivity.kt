package com.corellidev.personalfinance.expenses

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
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


class MainActivity : AppCompatActivity(), AddExpenseDialogFragment.AcceptClickListener,
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
        fillDatabases()

        with (expenses_list) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            val categoriesMap = HashMap<String, CategoryModel>()
            categoriesRepository.getAllCategories().forEach({ item -> categoriesMap.put(item.name, item)})
            listAdapter = ExpensesListAdapter(this@MainActivity, categoriesMap,
                    object : ExpensesListAdapter.Callback {
                        override fun OnExpenseRemoved(expenseModel: ExpenseModel) {
                            expensesRepository.deleteExpense(expenseModel)
                        }
                        override fun onExpenseClick(expenseModel: ExpenseModel) {
                            val addExpenseDialogFragment = AddExpenseDialogFragment()
                            val bundle = Bundle()
                            bundle.putString(EDITED_EXPENSE_ID, expenseModel.id)
                            addExpenseDialogFragment.arguments = bundle
                            addExpenseDialogFragment.acceptClickListener = this@MainActivity
                            addExpenseDialogFragment.show(supportFragmentManager, addExpenseDialogFragment.TAG)
                        }
                    })
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
            val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.DOWN or ItemTouchHelper.UP) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                    target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun getSwipeDirs(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
                    return ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    val snackbar = Snackbar.make(main_activity_coordinator_layout,
                            String.format(getString(R.string.expense_deleted_message),
                                    listAdapter.items.get(viewHolder.adapterPosition).name),
                                Snackbar.LENGTH_LONG)
                    listAdapter.onItemSwiped(viewHolder)
                    snackbar.setAction("Undo", {listAdapter.onUndoDeleteClicked()})
                    snackbar.show()
                }
            }
            ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(this)
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
            addExpenseDialogFragment.acceptClickListener = this
            addExpenseDialogFragment.show(supportFragmentManager, addExpenseDialogFragment.TAG)
        }
    }

    override fun onStart() {
        super.onStart()
        if(categoriesRepository.checkIfModified()) {
            val categoriesMap = HashMap<String, CategoryModel>()
            categoriesRepository.getAllCategories().forEach({ item -> categoriesMap.put(item.name, item)})
            listAdapter.categoriesMap = categoriesMap
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
    }

    private fun fillDatabases() {
//        categoriesRepository.deleteAllCategoires()
        categoriesRepository.addCategory(CategoryModel("Food", Color.BLUE))
        categoriesRepository.addCategory(CategoryModel("Car", Color.RED))
        categoriesRepository.addCategory(CategoryModel("Bills", Color.GREEN))

        expensesRepository.deleteAllExpenses()
        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Groceries", 123.13, "Food", 1475413783000))
        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Groceries", 234.11, "Food", 1478524183000))
        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Groceries", 11.2, "Food", 1481634583000))
        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Groceries", 412.34, "Food", 1485176983000))
        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Groceries", 189.65, "Food", 1486904983000))
        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Groceries", 453.21, "Food", 1490879383000))

        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Car service", 50.00, "Car", 1475413183000))
        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Car service", 25.00, "Car", 1478178583000))
        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Car service", 50.00, "Car", 1481461783000))
        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Car service", 100.00, "Car", 1485004183000))
        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Car service", 125.00, "Car", 1486904983000))
        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Car service", 150.00, "Car", 1488373783000))

        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Flat rent", 900.00, "Bills", 1475413456000))
        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Flat rent", 900.00, "Bills", 1480424983000))
        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Flat rent", 900.00, "Bills", 1483016983000))
        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Flat rent", 900.00, "Bills", 1485695383000))
        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Flat rent", 900.00, "Bills", 1488373783000))
        expensesRepository.addExpense(ExpenseModel(BLANK_ID, "Flat rent", 900.00, "Bills", 1490792983000))
    }

    override fun onAddClick(expense: ExpenseModel) {
        val updatedId = expensesRepository.addExpense(expense)
        listAdapter.items.add(ExpenseModel(updatedId, expense.name, expense.value, expense.category, expense.time))
    }

    override fun onEditAcceptClick(expense: ExpenseModel) {
        val editedExpenseIndex = listAdapter.items.map { it.id }.indexOf(expense.id)
        listAdapter.items.removeAt(editedExpenseIndex)
        listAdapter.items.add(editedExpenseIndex, expense)
        listAdapter.notifyDataSetChanged()
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
