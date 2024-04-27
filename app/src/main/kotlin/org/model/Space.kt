package org.model

class Space(id: Long?, var name: String, var owner: String) {
    var id = id
        get() = field
        set(value) {
            if (id == null) field = value
        }
}
