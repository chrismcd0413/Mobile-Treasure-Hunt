/*  Assignment 6

    Chris McDaniel / mcdachri@oregonstate.edu
    CS 492 / Oregon State University

 */

package com.example.mobiletreasurehunt.utils

fun Long.formatTime(): String {
    val minutes = (this % 3600) / 60
    val remainingSeconds = this % 60
    return String.format("%02d : %02d", minutes, remainingSeconds)
}