package com.geofferyj.jsignin.views.fragments

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.geofferyj.jsignin.R
import com.geofferyj.jsignin.databinding.FragmentCameraBinding
import com.geofferyj.jsignin.models.ImageData
import com.geofferyj.jsignin.models.Repository
import com.geofferyj.jsignin.utils.*
import com.geofferyj.jsignin.utils.camerax.CameraManager
import com.geofferyj.jsignin.viewmodels.CameraViewModel
import com.geofferyj.jsignin.views.activities.MainActivity
import com.google.mlkit.vision.face.Face
import java.util.*

class CameraFragment : Fragment() {


    private lateinit var binding: FragmentCameraBinding
    private lateinit var viewModel: CameraViewModel
    private lateinit var cameraManager: CameraManager
    private val repository: Repository = Repository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        createCameraManager()
        binding.apply {
            lifecycleOwner = this@CameraFragment.viewLifecycleOwner
            viewModel = this@CameraFragment.viewModel
            initViewModel()
        }
        if (allPermissionsGranted()) {
            cameraManager.startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        Log.d(TAG, "In onCreateView")
        cameraManager.imageData.observe(viewLifecycleOwner) {
//            Log.d(TAG, "$it")

//            val action =
//                CameraFragmentDirections.actionCameraFragmentToImagePreviewFragment(it)
//            findNavController().navigate(action)

            takePicture(it)

        }
    }

    override fun onPause() {
        super.onPause()
        cameraManager.stopCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraManager.stopCamera()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraManager.startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                )
                    .show()
                requireActivity().finish()
            }
        }
    }

    private fun initViewModel() {
        viewModel.apply {
            onFabButtonEvent.observe(::getLifecycle) {
                it?.let {
                    binding.fabFinder.transform()
                    cameraManager.changeCameraSelector()
                }
            }

        }
    }


    private fun createCameraManager() {
        cameraManager = CameraManager(
            requireContext(),
            binding.previewViewFinder,
            this,
            binding.graphicOverlayFinder
        )
    }

    private fun takePicture(face: Face) {

        setOrientationEvent()

        cameraManager.imageCapture.takePicture(
            cameraManager.cameraExecutor,
            object : ImageCapture.OnImageCapturedCallback() {
                @SuppressLint("UnsafeExperimentalUsageError", "RestrictedApi")
                override fun onCaptureSuccess(image: ImageProxy) {
                    image.image?.let { img ->
                        Log.d(TAG, "${img.timestamp}")

                        img.imageToBitmap()
                            ?.rotateFlipImage(
                                cameraManager.rotation,
                                cameraManager.isFrontMode()
                            )
                            ?.let { bitmap ->

                                val faceBitmap = Bitmap.createBitmap(
                                    bitmap,
                                    (face.boundingBox.exactCenterX()).toInt(),
                                    (face.boundingBox.exactCenterY()).toInt(),
                                    face.boundingBox.width(),
                                    face.boundingBox.height()
                                )


                                if (findNavController().currentDestination?.id == R.id.camera_fragment) {
                                    val imageData = ImageData(bitmap, face)

                                    val action =
                                        CameraFragmentDirections.actionCameraFragmentToImagePreviewFragment(
                                            imageData
                                        )
                                    requireActivity().runOnUiThread {
                                        findNavController().navigate(action)

                                    }
                                }
                            }


                    }
                    super.onCaptureSuccess(image)
                }
            })
    }

    private fun imageToBitmapSaveGallery(image: Image) {
        image.imageToBitmap()
            ?.rotateFlipImage(
                cameraManager.rotation,
                cameraManager.isFrontMode()
            )
            ?.scaleImage(
                binding.previewViewFinder,
                cameraManager.isHorizontalMode()
            )
            ?.let { bitmap ->
                binding.graphicOverlayFinder.processCanvas.drawBitmap(
                    bitmap,
                    0f,
                    bitmap.getBaseYByView(
                        binding.graphicOverlayFinder,
                        cameraManager.isHorizontalMode()
                    ),
                    Paint().apply {
                        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
                    })
                binding.graphicOverlayFinder.processBitmap.saveToGallery(requireContext())
            }
    }

    private fun setOrientationEvent() {
        val orientationEventListener = object : OrientationEventListener(requireContext()) {
            override fun onOrientationChanged(orientation: Int) {
                val rotation: Float = when (orientation) {
                    in 45..134 -> 270f
                    in 135..224 -> 180f
                    in 225..314 -> 90f
                    else -> 0f
                }
                cameraManager.rotation = rotation
            }
        }
        orientationEventListener.enable()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }


    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val TAG = "CameraFragment"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
        private val REQUIRED_PERMISSIONS = arrayOf(
            android.Manifest.permission.CAMERA,
//            android.Manifest.permission.READ_EXTERNAL_STORAGE,
//            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )


    }
}

