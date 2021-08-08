package com.example.sditsattendance.costume

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import com.example.sditsattendance.R
import me.dm7.barcodescanner.core.IViewFinder
import me.dm7.barcodescanner.core.ViewFinderView
import me.dm7.barcodescanner.zxing.ZXingScannerView

/*
 * Created by farras on 11/1/2020
*/class CostumeScannerView (context: Context):ZXingScannerView (context) {

    override fun createViewFinderView(context: Context): IViewFinder {
        var viewFinderView:CostumeViewFinder = CostumeViewFinder(context)

        viewFinderView.setLaserColor(context.getColor(R.color.colorPrimary))
        viewFinderView.setBorderCornerRounded(true)
        viewFinderView.setBorderCornerRadius(40)
        viewFinderView.setBorderColor(context.getColor(R.color.colorPrimary))
        viewFinderView.setBorderLineLength(80)
        viewFinderView.setBorderStrokeWidth(15)
//        viewFinderView.playSoundEffect(R.)
        viewFinderView.setMaskColor(context.getColor(R.color.transparant))

        viewFinderView.setLaserEnabled(true)
        return viewFinderView
    }

    override fun setResultHandler(resultHandler: ResultHandler?) {
        super.setResultHandler(resultHandler)
    }


}