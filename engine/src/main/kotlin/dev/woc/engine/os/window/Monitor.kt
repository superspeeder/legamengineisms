package dev.woc.engine.os.window

import dev.woc.engine.Rectangle2i
import dev.woc.engine.memStack
import org.joml.Vector2f
import org.joml.Vector2i
import org.joml.Vector3f
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWGammaRamp

data class VideoMode(
    val redBits: Int,
    val greenBits: Int,
    val blueBits: Int,
    val width: Int,
    val height: Int,
    val refreshRate: Int
) {
    constructor(vm: GLFWVidMode) : this(
        vm.redBits(), vm.greenBits(), vm.blueBits(),
        vm.width(), vm.height(),
        vm.refreshRate()
    )
}

data class GammaRamp(
    val points: Array<Vector3f>
)

class Monitor(val handle: Long) {

    val contentScale: Vector2f = memStack { stack ->
        val x = stack.mallocFloat(1)
        val y = stack.mallocFloat(1)

        glfwGetMonitorContentScale(handle, x, y)

        return@memStack Vector2f(x.get(), y.get())
    }


    val primaryVideoMode: VideoMode
    val videoModes: List<VideoMode>
    val name: String
    val position: Vector2i = memStack { stack ->
        val x = stack.mallocInt(1)
        val y = stack.mallocInt(1)

        glfwGetMonitorPos(handle, x, y)

        return@memStack Vector2i(x.get(), y.get())
    }

    val workarea: Rectangle2i = memStack { stack ->
        val x = stack.mallocInt(1)
        val y = stack.mallocInt(1)
        val w = stack.mallocInt(1)
        val h = stack.mallocInt(1)

        glfwGetMonitorWorkarea(handle, x, y, w, h)

        return@memStack Rectangle2i(x.get(), y.get(), w.get(), h.get())
    }

    val physicalSize: Vector2i = memStack { stack ->
        val x = stack.mallocInt(1)
        val y = stack.mallocInt(1)

        glfwGetMonitorPhysicalSize(handle, x, y)

        return@memStack Vector2i(x.get(), y.get())
    }

    var gammaRamp: GammaRamp
        get() {
            val ramp = glfwGetGammaRamp(handle)?: error("Failed to get gamma ramp for monitor $handle")
            val red = ramp.red()
            val green = ramp.green()
            val blue = ramp.blue()

            return GammaRamp(Array(ramp.size()) { i ->
                return@Array Vector3f(red[i] / 255.0f, green[i] / 255.0f, blue[i] / 255.0f)
            })
        }
        set(value) = memStack { stack ->
            val ramp = GLFWGammaRamp.malloc(stack)
            val red = stack.mallocShort(value.points.size)
            val green = stack.mallocShort(value.points.size)
            val blue = stack.mallocShort(value.points.size)

            for (p in value.points.withIndex()) {
                red.put(p.index, (p.value.x * 255).toInt().toShort())
                green.put(p.index, (p.value.y * 255).toInt().toShort())
                blue.put(p.index, (p.value.z * 255).toInt().toShort())
            }

            ramp.set(red.rewind(), green.rewind(), blue.rewind(), value.points.size)
            glfwSetGammaRamp(handle, ramp)
        }

    fun setGamma(gamma: Float) {
        glfwSetGamma(handle, gamma)
    }


    init {
        primaryVideoMode = VideoMode(glfwGetVideoMode(handle) ?: error("Failed to get video mode for monitor $handle"))

        videoModes = glfwGetVideoModes(handle)?.run {
            stream().map { VideoMode(it) }.toList()
        }?: error("Failed to get video modes for monitor $handle")

        name = glfwGetMonitorName(handle)?: error("Failed to get monitor name for monitor $handle")

    }
}