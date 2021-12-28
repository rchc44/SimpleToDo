package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // 1. Remove item from list
                listOfTasks.removeAt(position)
                // 2. Notify adapter of change in data
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        // reference to button
        // set click listener on button
//        findViewById<Button>(R.id.button).setOnClickListener{
//           Log.i("Caren", "User clicked")
//        }

//        listOfTasks.add("Do laundry")
//        listOfTasks.add("Go for a walk")

        loadItems()

        // look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)

        // attach adapter to recyclerView
        recyclerView.adapter = adapter

        // set up layout manager. tells recyclerView how to set up
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up button and input field for user to enter to do
        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        findViewById<Button>(R.id.button).setOnClickListener{
            // 1. grab text from user input
            val userInputtedTask = inputTextField.text.toString()

            // 2. add text to our data
            listOfTasks.add(userInputtedTask)
            // notify adapter that our data has been updated, to change our view
            adapter.notifyItemInserted(listOfTasks.size-1)

            // 3. Clear input field
            inputTextField.setText("")

            saveItems()
        }
    }


    // Save data by writing and reading to file


    // Create method to get files we need
    fun getDataFile() : File {
        // every line of file is a task in todo list

        return File(filesDir, "data.txt")
    }

    // Load items by reading data from file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }
        catch(ioException:IOException)
        {
            ioException.printStackTrace()
        }

    }

    // Save items by writing to file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        }
        catch (ioException: IOException)
        {
            ioException.printStackTrace()
        }
    }
}
