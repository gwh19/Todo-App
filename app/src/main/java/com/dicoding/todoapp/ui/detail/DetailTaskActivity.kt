package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID
import com.google.android.material.textfield.TextInputEditText

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var detailTaskViewModel: DetailTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //TODO 11 : Show detail task and implement delete action

        detailTaskViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))
            .get(DetailTaskViewModel::class.java)

        val taskId = intent.getIntExtra(TASK_ID, -1)
        detailTaskViewModel.setTaskId(taskId)

        detailTaskViewModel.task.observe(this) {
            val titleTextView: TextView = findViewById<TextInputEditText>(R.id.detail_ed_title)
            val descriptionTextView: TextView = findViewById<TextInputEditText>(R.id.detail_ed_description)
            val timeTextView: TextView = findViewById<TextInputEditText>(R.id.detail_ed_due_date)

            titleTextView.text = Editable.Factory.getInstance().newEditable(it.title)
            descriptionTextView.text = Editable.Factory.getInstance().newEditable(it.description)
            timeTextView.text = Editable.Factory.getInstance().newEditable(DateConverter.convertMillisToString(it.dueDateMillis))
        }

        detailTaskViewModel.deletedTask.observe(this) { event ->
            val savedContent = event.getContentIfNotHandled()
            savedContent?.let {
                if (it) {
                    detailTaskViewModel.task.removeObservers(this)
                    finish()
                }
            }
        }

        val deleteButton: Button = findViewById(R.id.btn_delete_task)
        deleteButton.setOnClickListener{
            detailTaskViewModel.deleteTask()
            Toast.makeText(applicationContext, "Task Deleted", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
