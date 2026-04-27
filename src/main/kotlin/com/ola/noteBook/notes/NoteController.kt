package com.ola.noteBook.notes

import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
@RestController
@RequestMapping("/notes")
class NoteController(private val noteService: NoteService) {


    @PostMapping
    suspend fun create(@RequestBody body: NoteRequest) : NoteResponse{
        val ownerId = "tymaczasowyId"
        val command = CreateNoteCommand(
            title = body.title,
            content = body.content,
            color = body.color,
            ownerId = ownerId
        )
        return noteService.createNote(command)
    }
    @GetMapping
    fun getByOwner(@RequestParam ownerId : String) : Flow<NoteResponse> {
        return noteService.getUserNotes(ownerId)
    }
    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id : String) {
        noteService.deleteNote(id)
    }

    @GetMapping("/{id}/backlinks")
    fun getBacklinks(@PathVariable id : String) : Flow<NoteSummaryResponse>{
        return noteService.getBacklinks(id)
    }
}