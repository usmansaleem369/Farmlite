package com.tracko.automaticchickendoor.models.local

import androidx.annotation.DrawableRes

data class OnBoardingItem(
    @DrawableRes val imageRes: Int,
    val title: String,
    val description: String
)
