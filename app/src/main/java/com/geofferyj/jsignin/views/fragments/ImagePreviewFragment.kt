package com.geofferyj.jsignin.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.geofferyj.jsignin.R
import com.geofferyj.jsignin.databinding.FragmentImagePreviewBinding


class ImagePreviewFragment : Fragment() {
    private val args: ImagePreviewFragmentArgs by navArgs()
   private  lateinit var binding: FragmentImagePreviewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagePreviewBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fullImage.setImageBitmap(args.image.fullImage)
//        binding.textView.text = "${args.image.face}"
    }

}