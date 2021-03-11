package com.geofferyj.jsignin.utils

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class JAnalyzer(private var listener: (face: Face) -> Unit) : ImageAnalysis.Analyzer {

    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .build()

    private val detector = FaceDetection.getClient(options)

    var faceList = ArrayList<Pair<String, FloatArray>>()

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image

        Log.d("JAnalyzer_LOG", "mediaImage: $mediaImage")
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            Log.d("JAnalyzer_LOG", "image: $image")

            detector.process(image)
                .addOnSuccessListener { faces ->
                    for (face in faces) {
                        val bounds = face.boundingBox
                        val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                        val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees
                        Log.d("JAnalyzer_LOG", "bounds: $bounds face: $face")
                        listener(face)

                    }
                }
                .addOnFailureListener { e ->
                    Log.d("JAnalyzer_LOG", "error: $e")

                }
        }
    }


}