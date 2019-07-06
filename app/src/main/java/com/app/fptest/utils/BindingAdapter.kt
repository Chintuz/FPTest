package com.app.fptest.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.app.fptest.R


@BindingAdapter("imageDrawable")
fun loadImageUrl(view: ImageView, type: String) {
    when (type) {
        "apartment" -> view.setImageResource(R.drawable.apartment_3x)
        "condo" -> view.setImageResource(R.drawable.condo_3x)
        "boat" -> view.setImageResource(R.drawable.boat_3x)
        "land" -> view.setImageResource(R.drawable.land_3x)
        "rooms" -> view.setImageResource(R.drawable.rooms_3x)
        "no-room" -> view.setImageResource(R.drawable.no_room_3x)
        "swimming" -> view.setImageResource(R.drawable.swimming_3x)
        "garden" -> view.setImageResource(R.drawable.garden_3x)
        "garage" -> view.setImageResource(R.drawable.garage_3x)
    }
}

