package com.example.notes.repo

import androidx.lifecycle.LiveData
import com.example.notes.db.Word
import com.example.notes.db.WordDao

class WordRepository(private val wordDao:WordDao) {

    //Getting all words from Word table
    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()

    //calling insert function from our DAO
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }

    suspend fun clear() {
        wordDao.deleteAll()
    }

}