package com.corellidev.personalfinance.statistics

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.corellidev.personalfinance.R
import com.corellidev.personalfinance.expenses.ExpensesRepository
import com.corellidev.personalfinance.expenses.MainActivity
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_statistics.*
import kotlinx.android.synthetic.main.content_statistics.*
import kotlinx.android.synthetic.main.history_list_item.view.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import javax.inject.Inject

class StatisticsActivity : AppCompatActivity() {

    @Inject
    lateinit var expensesRepository: ExpensesRepository
    lateinit var listAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivity.mainActivityComponent.inject(this)
        setContentView(R.layout.activity_statistics)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener({finish()})

        with (history_list) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@StatisticsActivity)
            listAdapter = HistoryAdapter(this@StatisticsActivity)
            adapter = listAdapter
        }
        setContentForHistoryListAdapter()
    }

    private fun setContentForHistoryListAdapter(): Disposable? {
        return expensesRepository.getAllExpenses().subscribe({ expenses ->
            val historyRecordsList = ArrayList<HistoryRecordModel>()
            expenses
                    .sortedBy { it.time }
                    .groupBy({
                        DateTime(it.time).year().get() * 100 + DateTime(it.time).monthOfYear().get()
                    })
                    .forEach({ mapEntry ->
                        historyRecordsList.add(HistoryRecordModel(mapEntry.value.first().time,
                                mapEntry.value.sumByDouble { it.value }))
                    })
            listAdapter.setContent(historyRecordsList)
        })
    }
}

class HistoryAdapter(val context: Context):
        RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    var items: MutableList<HistoryRecordModel> = mutableListOf()

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder?, position: Int) {
        (holder as ViewHolder).bind(items.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HistoryAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.history_list_item, null, true))
    }

    override fun getItemCount(): Int = items.size

    fun setContent(items: MutableList<HistoryRecordModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateFormat = DateTimeFormat.forPattern("MM.yyyy");
        fun bind(historyRecordModel: HistoryRecordModel) {
            itemView.date.text = DateTime(historyRecordModel.date).toString(dateFormat)
            itemView.value.text = historyRecordModel.value.toString()
        }
    }
}

class HistoryRecordModel(val date: Long, val value: Double)

