package com.example.smarthealthsystem

import android.app.Activity
import android.app.Instrumentation
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.smarthealthsystem.databinding.FragmentDetectionBinding
import com.example.smarthealthsystem.ml.Skin
import com.example.smarthealthsystem.ml.Skin.Outputs
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.TransformToGrayscaleOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class FragmentDetection : Fragment() {

    private var _binding: FragmentDetectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var detectionBinding: FragmentDetectionBinding
    private lateinit var imageView: ImageView
    private lateinit var buttonUpload: Button
    private lateinit var  buttonCamera: Button
    private lateinit var buttonDetect: Button
    private lateinit var tvOutputs: TextView
    private lateinit var bitmap: Bitmap
    private val GALLERY_REQUEST_CODE = 123


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetectionBinding.inflate(inflater, container, false)
        detectionBinding = FragmentDetectionBinding.inflate(inflater)
        imageView = binding.skinCancerImage
        buttonUpload = binding.btnUploadImage
        buttonCamera = binding.btnOpenCamera
        buttonDetect = binding.btnDetect
        tvOutputs = binding.outputResultText

        var labels = requireContext().applicationContext.assets.open("labels.txt").bufferedReader().readLines()

        //Image Processor
        var imageProcessor = ImageProcessor.Builder()
//            .add(NormalizeOp(0.0f, 255.0f))
//            .add(TransformToGrayscaleOp())
            .add(ResizeOp(28, 28, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        buttonUpload.setOnClickListener {
            var intent: Intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 100)
        }

        buttonDetect.setOnClickListener {

            var tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)

            tensorImage = imageProcessor.process(tensorImage)

            val model = Skin.newInstance(requireContext())

            // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 28, 28, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(tensorImage.buffer)

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

            var maxIdx = 0
            outputFeature0.forEachIndexed { index, fl ->
                if(outputFeature0[maxIdx] < fl){
                    maxIdx = index
                }
            }

            tvOutputs.setText(labels[maxIdx])

            // Releases model resources if no longer used.
            model.close()
        }


        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100){
            var uri = data?.data
            bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            imageView.setImageBitmap(bitmap)
        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
