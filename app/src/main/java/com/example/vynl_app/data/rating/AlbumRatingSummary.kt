package com.example.vynl_app.data.rating

data class AlbumRatingSummary(

// Mirrors the ratingSum/ratingCount fields stored on albums/{albumId} in Firestore.
// Reading this is one cheap document read, never a loop over every rating
    val ratingSum: Long = 0,
    val ratingCount: Long = 0
) {
    // Average stars out of 5. Guard against divide-by-zero when nobody's rated yet.
    //
    val average : Double
        get () = if (ratingCount == 0L) 0.0 else ratingSum.toDouble() / ratingCount

    //Converts average/ 5 into a percentage, matching the Figma design template
    val percentage : Int
        get() = ((average / 5.0) * 100 ).toInt()

}

