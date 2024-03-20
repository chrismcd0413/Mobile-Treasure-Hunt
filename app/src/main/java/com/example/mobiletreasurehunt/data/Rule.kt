/*  Assignment 6

    Chris McDaniel / mcdachri@oregonstate.edu
    CS 492 / Oregon State University

 */

package com.example.mobiletreasurehunt.data

import androidx.annotation.StringRes

data class Rule(
    @StringRes val id: Int,
    @StringRes val header: Int,
    @StringRes val rule: Int
)
