package com.example.movingcar

data class Model(
    var isRunning: Boolean = false,
    var isFinished: Boolean = false,
    var isVerticaMove: Boolean = false ,  //Вертикальное направление движения
    var prevVerticaMove: Boolean = false,
    var cx: Int = 0,
    var cy: Int = 50

)
