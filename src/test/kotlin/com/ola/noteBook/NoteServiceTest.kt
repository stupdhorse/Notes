package com.ola.noteBook

import com.ola.noteBook.notes.CreateNoteCommand
import com.ola.noteBook.notes.Note
import com.ola.noteBook.notes.NoteRepository
import com.ola.noteBook.notes.NoteService
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import java.time.Instant
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import kotlin.test.assertEquals

class NoteServiceTest {
    private val noteRepository = mockk<NoteRepository>()
    private val noteService = NoteService(noteRepository)

    @Test
    fun `should create and save new note`() = runTest {
        val title = "zakupy"
        val content = "zrobic zakupy"
        val color = 123L
        val ownerId = "testOwner123"
        val command = CreateNoteCommand(title = title,content=content,color=color,ownerId=ownerId)
        coEvery {noteRepository.save(any())} answers{
            val savedNote = firstArg<Note>()
            savedNote.copy(id="wygenerowane_id_1", createdAt = Instant.now())
        }

        val result = noteService.createNote(command)

        assertEquals(title, result.title)
        assertEquals(content,result.content)
        assertEquals(color,result.color)
        assertNotNull(result.id)
        assertNotNull(result.createdAt)

        coVerify(exactly = 1){
            noteRepository.save(withArg { noteToSave ->
                assertEquals(title,noteToSave.title)
                assertEquals(content,noteToSave.content)
                assertEquals(color,noteToSave.color)
                assertEquals(ownerId,noteToSave.ownerId)

                assertNull(noteToSave.id)
                assertNull(noteToSave.createdAt)
            })
        }
    }

    @Test
    fun `should get all user's notes`() = runTest {
        val ownerId = "PierwszyWłaściciel"
        val expectedNotes = listOf(
            Note(title = "pierwsza", content = "pierwsza treść", color = 1L, ownerId=ownerId, id="note1", createdAt = Instant.now()),
            Note(title = "druga", content = "druga treść", color = 1L, ownerId=ownerId, id="note2", createdAt = Instant.now()),
        )
        every{noteRepository.findByOwnerId(ownerId)} returns expectedNotes.asFlow()

        val result = noteService.getUserNotes(ownerId).toList()

        assertEquals(2,result.size)
        assertEquals(expectedNotes[0].id,result[0].id)
        assertEquals(expectedNotes[1].id,result[1].id)
        assertEquals(expectedNotes[0].title,result[0].title)
        assertEquals(expectedNotes[1].title,result[1].title)

        verify(exactly = 1){
            @Suppress("UnusedFlow")
            noteRepository.findByOwnerId(ownerId)
        }
    }

}