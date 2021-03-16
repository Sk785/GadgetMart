package com.gadgetmart.ui.my_review

import com.gadgetmart.ui.search.SearchProductData
import com.google.gson.annotations.SerializedName

data class ReviewModelList (
    @SerializedName("data") val data: SearchProductData?

)