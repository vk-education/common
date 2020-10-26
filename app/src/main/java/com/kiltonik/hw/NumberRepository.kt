package com.kiltonik.hw

class NumberRepository private constructor() {

    private val numberList = mutableListOf<Int>()

    fun newItem() = numberList.add(numberList.size)

    fun list() = numberList

    fun number(index: Int) = numberList[index]

    companion object {
        val instance by lazy { NumberRepository() }
    }
}
