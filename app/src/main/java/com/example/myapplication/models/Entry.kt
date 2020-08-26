package com.example.myapplication.models

class Entry (
    private val title: String,
    private val body: String
) {
    override fun toString(): String {
        return "${this.title}: ${this.body}"
    }
}