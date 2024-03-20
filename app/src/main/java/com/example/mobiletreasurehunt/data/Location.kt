/*  Assignment 6

    Chris McDaniel / mcdachri@oregonstate.edu
    CS 492 / Oregon State University

 */

package com.example.mobiletreasurehunt.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Location (
    val id: Int,
    @StringRes val name: Int,
    @StringRes val clue: Int,
    @StringRes val hint: Int,
    @StringRes val summary: Int,
    @DrawableRes val photo: Int,
    @StringRes val lat: Int,
    @StringRes val long: Int
)