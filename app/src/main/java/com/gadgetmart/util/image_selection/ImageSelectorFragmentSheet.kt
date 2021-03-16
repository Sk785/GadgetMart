package com.gadgetmart.util.image_selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gadgetmart.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_select_file.*


class ImageSelectorFragmentSheet : BottomSheetDialogFragment(), View.OnClickListener {

    companion object {
        val SELECTED_FILE_CAMERA = 1111
        val SELECTED_FILE_GALLERY = 1112

        fun newInstance(): ImageSelectorFragmentSheet {
            return ImageSelectorFragmentSheet()
        }
    }
    var listener: Callback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return LayoutInflater.from(activity)
            .inflate(R.layout.layout_select_file, container, false)
    }

    override fun onClick(v: View?) {

        if (listener == null) {
            throw NullPointerException(
                Callback::class.java.simpleName + " is not set on " +
                        ImageSelectorFragmentSheet::class.java.simpleName
            )
        }

        when (v!!.id) {

            R.id.linear_camera -> {
                listener!!.onImageSourceSelected(SELECTED_FILE_CAMERA)
                dismiss()
            }

            R.id.linear_gallery -> {
                listener!!.onImageSourceSelected(SELECTED_FILE_GALLERY)
                dismiss()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        linear_camera.setOnClickListener(this)
        linear_gallery.setOnClickListener(this)
    }


    fun setSelectionListener(listener: Callback): ImageSelectorFragmentSheet {
        this.listener = listener
        return this
    }


    interface Callback {

        fun onImageSourceSelected(optionId: Int)
    }
}