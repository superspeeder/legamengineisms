package dev.woc.engine.common

enum class GraphicsApi {
    OpenGL,
    Vulkan;

    companion object {
        val DEFAULT: GraphicsApi = Vulkan
    }
}

