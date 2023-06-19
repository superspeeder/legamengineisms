package dev.woc.engine

import org.lwjgl.system.MemoryStack
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@ExperimentalContracts
inline fun <R> memStack(f: (MemoryStack) -> R): R {
    contract {
        callsInPlace(f, InvocationKind.EXACTLY_ONCE)
    }

    val stack = MemoryStack.stackPush()
    val r = f(stack)
    stack.pop()

    return r
}

data class Rectangle2i(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
)