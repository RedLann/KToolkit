package com.cwsinformatica.photocloud.ui.base

class Consumable<out T> private constructor(private val content: T) {

    companion object {
        fun <T : Any> T.asConsumable(): Consumable<T> = Consumable(this)
    }

    private var consumed = false

    fun <R> consume(block: (T) -> R): Consumable<T> {
        if (!consumed) {
            consumed = true
            block(content)
        }
        return this
    }

    fun peek(): T = content
}

