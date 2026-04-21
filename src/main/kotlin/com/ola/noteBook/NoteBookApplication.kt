package com.ola.noteBook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NoteBookApplication

fun main(args: Array<String>) {
    runApplication<NoteBookApplication>(*args)
}