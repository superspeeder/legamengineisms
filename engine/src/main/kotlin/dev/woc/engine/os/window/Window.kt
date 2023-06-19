package dev.woc.engine.os.window

import dev.woc.engine.KatEngine
import dev.woc.engine.common.GraphicsApi
import org.joml.Vector2i
import org.lwjgl.glfw.GLFW.*;

enum class GlContextProfile {
    Core,
    Compat
}

enum class GlContextRobustness {
    None,
    NoResetNotification,
    LoseContextOnReset
}

enum class GlReleaseBehavior {
    Any,
    Flush,
    None
}

enum class GlContextCreationApi {
    Native,
    EGL,
    OsMesa
}

data class GlContextMode(
    val majorVersion: Int = 4,
    val minorVersion: Int = 6,
    val contextCreationApi: GlContextCreationApi = GlContextCreationApi.Native,
    val robustness: GlContextRobustness = GlContextRobustness.None,
    val releaseBehavior: GlReleaseBehavior = GlReleaseBehavior.Any,
    val forwardCompatibility: Boolean = true,
    val debugContext: Boolean = KatEngine.isDebugEnabled(),
    val profile: GlContextProfile = GlContextProfile.Core,
) {

}

data class GraphicsApiMode(
    val api: GraphicsApi,
    val glContextMode: GlContextMode? = null
) {
    companion object {
        fun default(): GraphicsApiMode = default(GraphicsApi.DEFAULT)

        fun default(api: GraphicsApi): GraphicsApiMode = when (api) {
            GraphicsApi.Vulkan -> GraphicsApiMode(GraphicsApi.Vulkan)
            GraphicsApi.OpenGL -> GraphicsApiMode(GraphicsApi.OpenGL, GlContextMode())
        }
    }

}

data class WindowMode(
    val width: Int = 800,
    val height: Int = 600,
    val redBits: Int = GLFW_DONT_CARE,
    val greenBits: Int = GLFW_DONT_CARE,
    val blueBits: Int = GLFW_DONT_CARE,
    val refreshRate: Int = GLFW_DONT_CARE,
    val monitor: Monitor? = null
) {
    constructor(monitor: Monitor) : this(monitor.primaryVideoMode, monitor)
    constructor(videoMode: VideoMode, monitor: Monitor?) : this(
        videoMode.width, videoMode.height,
        videoMode.redBits, videoMode.greenBits, videoMode.blueBits,
        videoMode.refreshRate, monitor)


    fun withColorDepth(red: Int, green: Int, blue: Int): WindowMode {
        return WindowMode(width, height, red, green, blue, refreshRate, monitor)
    }

    fun withRefreshRate(refreshRate: Int): WindowMode {
        return WindowMode(width, height, redBits, greenBits, blueBits, refreshRate, monitor)
    }

    fun withSize(size: Vector2i): WindowMode {
        return withSize(size.x, size.y)
    }

    fun withSize(width: Int, height: Int): WindowMode {
        return WindowMode(width, height, redBits, greenBits, blueBits, refreshRate, monitor)
    }

    fun withMonitor(monitor: Monitor?, nonoptimal: Boolean = false): WindowMode {
        return monitor?.let {
            if (nonoptimal) {
                return@let WindowMode(width, height, redBits, greenBits, blueBits, refreshRate, it)
            }
            return@let WindowMode(monitor)
        }?: run {
            return@run WindowMode(width, height, redBits, greenBits, blueBits, refreshRate, null)
        }
    }

    fun withVideoMode(videoMode: VideoMode): WindowMode {
        return WindowMode(videoMode, null)
    }
}

data class ResizeBehavior(
    val resizable: Boolean = false,
    val aspectRatio: Pair<Int, Int> = Pair(GLFW_DONT_CARE, GLFW_DONT_CARE),
    val limits: Pair<Vector2i, Vector2i> = Pair(Vector2i(GLFW_DONT_CARE,GLFW_DONT_CARE), Vector2i(GLFW_DONT_CARE,GLFW_DONT_CARE)),
) {
    fun withResizable(resizable: Boolean = true): ResizeBehavior {
        return ResizeBehavior(resizable, aspectRatio, limits)
    }

    fun withAspect(numer: Int, denom: Int): ResizeBehavior {
        return ResizeBehavior(resizable, Pair(numer, denom), limits)
    }

    fun withLimits(minimum: Vector2i, maximum: Vector2i): ResizeBehavior {
        return ResizeBehavior(resizable, aspectRatio, Pair(minimum, maximum))
    }

    fun withMinimumSize(minimum: Vector2i): ResizeBehavior {
        return withLimits(minimum, limits.second)
    }

    fun withMaximumSize(maximum: Vector2i): ResizeBehavior {
        return withLimits(limits.first, maximum)
    }

    fun withMinimumSize(minWidth: Int, minHeight: Int): ResizeBehavior {
        return withLimits(Vector2i(minWidth, minHeight), limits.second)
    }

    fun withMaximumSize(maxWidth: Int, maxHeight: Int): ResizeBehavior {
        return withLimits(limits.first, Vector2i(maxWidth, maxHeight))
    }

    fun withAspect16x9(): ResizeBehavior {
        return withAspect(16, 9)
    }

    fun withAspect4x3(): ResizeBehavior {
        return withAspect(4, 3)
    }

    fun withAspect1x1(): ResizeBehavior {
        return withAspect(1, 1)
    }

    fun withAspectFucked(): ResizeBehavior {
        return withAspect(30, 1)
    }
}

data class WindowSettings(
    val title: String,
    val windowMode: WindowMode = WindowMode(),
    val graphicsApi: GraphicsApiMode = GraphicsApiMode.default(),
    val resizeBehavior: ResizeBehavior = ResizeBehavior(true),
    val visible: Boolean = true,
    val decorated: Boolean = true,
    val focused: Boolean = true,
    val autoIconify: Boolean = true,
    val floating: Boolean = false,
    val maximized: Boolean = false,
    val centerCursor: Boolean = true,
    val transparentFramebuffer: Boolean = false,
    val scaleToMonitor: Boolean = false,
    val samples: Int = 0,
    val stereographic: Boolean = false,
    val srgbCapable: Boolean = false,
    val cocoaRetinaFramebuffer: Boolean = true,
    val cocoaFrameName: String = "",
    val cocoaGraphicsSwitching: Boolean = false,
    val x11ClassName: String = "",
    val x11InstanceName: String = ""
) {

}

class Window {


}