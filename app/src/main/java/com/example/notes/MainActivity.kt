package com.example.notes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.db.Word
import com.example.notes.recyclerview.WordListAdapter
import com.example.notes.viewmodels.WordViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val newWordActivityRequestCode = 1
    private lateinit var wordViewModel: WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = WordListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)

        //set observer to update words in the database
        wordViewModel.allWords.observe(this, Observer { words->
            //update cached copy
            words?.let {
                adapter.submitList(it)
            }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.word_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val delete = item.itemId
        when(delete) {
            R.id.delete -> wordViewModel.clearAll()

            else -> return false
        }

        //returns selected items
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
                data?.getStringExtra(NewWordActivity.EXTRA_REPLY)?.let {
                    val word = Word(it)
                    wordViewModel.insert(word)
                    Toast.makeText(applicationContext,"Added a note",Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Not saved",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
