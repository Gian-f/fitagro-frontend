package com.br.fitagro_frontend.domain

import android.graphics.Bitmap
import com.br.fitagro_frontend.data.model.Classification

interface FruitClassifier {
    fun classify(bitmap: Bitmap, rotation: Int): List<Classification>
}