package com.ola.noteBook.notes

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface NoteRepository : CoroutineCrudRepository<Note, String> {
    fun findByOwnerId(ownerId : String) : Flow<Note>
    fun findByBacklinksContains(linkId: String) : Flow<NoteSummaryProjection>
}