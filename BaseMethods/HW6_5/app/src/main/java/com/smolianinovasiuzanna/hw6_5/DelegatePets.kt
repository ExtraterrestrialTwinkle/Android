package com.smolianinovasiuzanna.hw6_5

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class DelegatePets <T> (
    private var value: T
        ): ReadWriteProperty <Person, T> {
    override fun getValue(thisRef: Person, property: KProperty<*>): T = value

    override fun setValue(thisRef: Person, property: KProperty<*>, value: T) {
        this.value = value


    }

}
