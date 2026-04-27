package com.ola.noteBook.notes

import java.time.Instant

data class NoteRequest(
    val title: String,
    val content: String,
    val color : Long
)

data class NoteResponse(
    val id: String,
    val title: String,
    val content: String,
    val color: Long,
    val createdAt : Instant
)

data class NoteSummaryProjection(
    val id: String,
    val title: String,
)
data class NoteSummaryResponse(
    val id: String,
    val title: String,
)
data class CreateNoteCommand(
    val title: String,
    val content: String,
    val color : Long,
    val ownerId : String
)