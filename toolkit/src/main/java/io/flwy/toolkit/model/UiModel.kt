package io.flwy.toolkit.model

interface UiModel {
    var key: String

    fun areContentsTheSame(obj: UiModel): Boolean {
        if (this === obj) return true
        if (javaClass != obj.javaClass) return false
        if (key != obj.key) return false
        return propertyDiff(obj)
    }

    fun propertyDiff(obj: UiModel): Boolean
}