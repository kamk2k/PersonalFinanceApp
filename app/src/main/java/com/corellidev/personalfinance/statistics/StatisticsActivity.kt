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
import kotlinx.android.synthetic.main.activity_statistics.*
import kotlinx.android.synthetic.main.content_statistics.*
import kotlinx.android.synthetic.main.history_list_item.view.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class StatisticsActivity : AppCompatActivity() {

    lateinit var listAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener({finish()})

        with (history_list) {
            setHasFixedSize(true)

            //TODO test data, remove later
            layoutManager = LinearLayoutManager(this@StatisticsActivity)
            listAdapter = HistoryAdapter(this@StatisticsActivity,
                    mutableListOf(HistoryRecordModel(1503759583691, 1897.34),
                            HistoryRecordModel(1361890806000, 1227.14),
                            HistoryRecordModel(1406386806000, 4897.23)))
            adapter = listAdapter
        }
    }
}

class HistoryAdapter(val context: Context, val items: MutableList<HistoryRecordModel>):
        RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder?, position: Int) {
        (holder as ViewHolder).bind(items.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HistoryAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.history_list_item, null, true))
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateFormat = DateTimeFormat.forPattern("MM.yyyy");
        fun bind(historyRecordModel: HistoryRecordModel) {
            itemView.date.text = DateTime(historyRecordModel.date).toString(dateFormat)
            itemView.value.text = historyRecordModel.value.toString()
        }
    }
}

class HistoryRecordModel(val date: Long, val value: Double)

