package dev.woc.legame

import dev.woc.engine.KatEngine

fun main(args: Array<String>) {
    println("Hello World!")
    println("Program arguments: ${args.joinToString()}")
    println("Using version ${KatEngine.VERSION_STR} of KatEngine")
}