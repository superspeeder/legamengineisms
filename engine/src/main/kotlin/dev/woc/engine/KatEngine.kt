package dev.woc.engine

import dev.woc.engine.os.OS
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.StringUtils

object KatEngine {

    const val VERSION_STR: String = "0.1.0"

    fun init() {
        OS.init()
    }

    fun dispose() {
        OS.dispose()
    }

    fun isDebugEnabled(): Boolean {
        return System.getProperties().containsKey("katengine.debug")
    }
}