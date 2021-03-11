package com.geofferyj.jsignin.models

import android.graphics.Bitmap
import com.google.mlkit.vision.face.Face
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ImageData(var fullImage: Bitmap, val face: @RawValue Face): Parcelable
