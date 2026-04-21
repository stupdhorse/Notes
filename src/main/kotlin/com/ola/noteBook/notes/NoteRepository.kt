package com.ola.noteBook.notes

import org.springframework.data.mongodb.repository.MongoRepository

interface NoteRepository : MongoRepository<Note, String> {
    fun findByOwnerId(ownerId : String) : List<Note>
}