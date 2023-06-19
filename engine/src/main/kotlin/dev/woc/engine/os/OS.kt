package dev.woc.engine.os

import org.apache.commons.lang3.SystemUtils
import dev.woc.engine.os.window.Monitor

import org.lwjgl.glfw.GLFW.*

object OS {
    val name: String = SystemUtils.OS_NAME
    var initialized: Boolean = false
        private set

    var monitors: List<Monitor> = listOf()
        private set

    fun init() {
        if (!initialized) {
            // Do initialization

            glfwInit()

            glfwGetMonitors()?.let { pb ->
                val ms = mutableListOf<Monitor>()

                while (pb.hasRemaining()) {
                    val mh = pb.get()

                    ms.add(Monitor(mh))
                }
            } ?: error("Failed to obtain monitor list")
        }
    }

    fun dispose() {
        glfwTerminate()
    }
}