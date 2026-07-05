package com.example.pocketinput.controller

import kotlinx.serialization.Serializable

@Serializable
data class ControllerState(
    val lx: Int = 0,
    val ly: Int = 0,
    val rx: Int = 0,
    val ry: Int = 0,

    val lt: Int = 0,
    val rt: Int = 0,

    val a: Boolean = false,
    val b: Boolean = false,
    val x: Boolean = false,
    val y: Boolean = false,

    val lb: Boolean = false,
    val rb: Boolean = false,

    val start: Boolean = false,
    val back: Boolean = false
)