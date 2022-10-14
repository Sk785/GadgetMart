package com.gadgetmart.data.domain

import com.gadgetmart.data.base.ApiListResponse
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.ui.auth._common.AuthResult
import com.gadgetmart.ui.auth._common.OrderConfirmation
import com.gadgetmart.ui.auth.phone_number_verification.PhoneVerificationRequest
import com.gadgetmart.ui.auth.registeration.RegistrationRequest
import com.gadgetmart.ui.cart_bag.model.CartModel
import com.gadgetmart.ui.checkout.model.CheckOutDataModel
import com.gadgetmart.ui.dashboard.DashboardResult
import com.gadgetmart.ui.home.model.SettingModel
import com.gadgetmart.ui.my_address.AddressRequest
import com.gadgetmart.ui.my_address.AddressResult
import com.gadgetmart.ui.my_address.Country
import com.gadgetmart.ui.my_address.State
import com.gadgetmart.ui.my_review.ReviewData
import com.gadgetmart.ui.my_review.ReviewModelList
import com.gadgetmart.ui.order.model.MyOrderResult
import com.gadgetmart.ui.order.model.TrackListModel
import com.gadgetmart.ui.product_details.ProductDetailResult
import com.gadgetmart.ui.product_details.productmodel.CartData
import com.gadgetmart.ui.products.PopularSectionProductData
import com.gadgetmart.ui.products.SectionProductData
import com.gadgetmart.ui.products_of_sub_category.ProductsOfSubResult
import com.gadgetmart.ui.search.SearchProduct
import com.gadgetmart.ui.splash.WelcomeRequest
import com.gadgetmart.ui.subcategory.SubCategoryResult
import com.gadgetmart.ui.wishlist.WishListRemovedResult
import com.gadgetmart.ui.wishlist.WishListResult
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.*

/**
 * ApiClient lists our communication to server.
 * <p>
 * What do we talk about?
 */
interface ApiClient {


    @POST("login")
    fun verifyPhoneNumber(
        @Body phoneVerificationRequest: PhoneVerificationRequest
    ): Call<ApiResponse<AuthResult>>

    @Multipart
    @POST("register")
    fun registerUser(
        @Part photo: MultipartBody.Part?,
        @Part("country_code") country_code:RequestBody,
        @Part("phone")phone: RequestBody,
        @Part("email")email: RequestBody,
        @Part("name")name: RequestBody,
        @Part("gender")gender: RequestBody,
        @Part("device_token")device_token: RequestBody



        // @Body registrationRequest: RegistrationRequest
    ): Call<ApiResponse<AuthResult>>
    @Multipart
    @POST("register")
    fun registerWithOutImageUser(
        @Part("country_code") country_code:RequestBody ,
        @Part("phone")phone: RequestBody,
        @Part("email")email: RequestBody,
        @Part("name")name: RequestBody
        ,
        @Part("gender")gender: RequestBody,
        @Part("device_token")device_token: RequestBody



        // @Body registrationRequest: RegistrationRequest
    ): Call<ApiResponse<AuthResult>>

    @Headers("Content-Type: application/json")
    @POST("dashboard2")
    fun fetchDashboardData(
        @Header("Authorization") authToken: String
    ): Call<ApiResponse<DashboardResult>>

    @POST("dashboard")
    fun getHomeData(
        @Header("Authorization") authToken: String
    ): Call<ResponseBody>

    @POST("userProfile")
    fun fetchUserProfile(
        @Header("Authorization") authToken: String
    ): Call<ApiResponse<AuthResult>>


    @Multipart
    @POST("updateProfile")
    fun editProfile(
        @Header("Authorization") authToken: String,
        @Part photo: MultipartBody.Part?,
        @Part("country_code") country_code:RequestBody,
        @Part("phone")phone: RequestBody,
        @Part("email")email: RequestBody,
        @Part("name")name: RequestBody,
        @Part("gender")gender: RequestBody

    ): Call<ApiResponse<AuthResult>>

    @GET("subCategoriesOfCategory/{category_id}")
    fun getSubcategory(
        @Path("category_id") categoryId: String,
        @Header("Authorization") authToken: String
    ): Call<ApiResponse<SubCategoryResult>>

    @POST("productsOnSale")
    fun getPopularProducts(
        @Query("page") pageNumber: Int?,
        @Header("Authorization") authToken: String
    ): Call<ApiResponse<ProductsOfSubResult>>

    @POST("popularProducts")
    fun getOfferProducts(
        @Query("page") pageNumber: Int?,
        @Header("Authorization") authToken: String
    ): Call<ApiResponse<ProductsOfSubResult>>



  //  @FormUrlEncoded
    @POST("productsOfSubCategory/{category_id}")
    fun getProductsOfSubCategory(
        @Path("category_id") categoryId: String,
        @Query("page") pageNumber: Int?,

        @Header("Authorization") authToken: String,
      //  @Field("sort_type") sort_type: String,
        @Body filter: JsonObject


    ): Call<ApiResponse<ProductsOfSubResult>>

    @FormUrlEncoded
    @POST("getFilters")
    fun getFilters(

        @Field("cat_id") cat_id: String,

        @Header("Authorization") authToken: String
    ): Call<ResponseBody?>

    @FormUrlEncoded
    @POST("addToMyWishlist")
    fun addToWishList(
        @Header("Authorization") authToken: String,
        @Field("variation_id") variationId: String
    ): Call<ApiResponse<WishListResult>>

    @FormUrlEncoded
    @POST("searchProducts")
    fun searchProducts(
        @Query("page") pageNumber: Int?,

        @Header("Authorization") authToken: String,
        @Field("keyword") keyword: String
    ): Call<ApiResponse<SearchProduct>>
    @FormUrlEncoded
    @POST("removeFromMyWishList")
    fun removeFromWishList(
        @Header("Authorization") authToken: String,
        @Field("wishlist_id") wishListId: String
    ): Call<ApiResponse<WishListRemovedResult>>

    @FormUrlEncoded
    @POST("addToRecentViewedProduct")
    fun addToRecentViewedProduct(
        @Header("Authorization") authToken: String,
        @Field("variation_id") variation_id: String
    ): Call<ResponseBody>



    @GET("myWishList")
    fun getMyWishList(@Header("Authorization") authToken: String): Call<ApiResponse<WishListRemovedResult>>

    @POST("saveMyAddress")
    fun saveMyAddress(
        @Body addressRequest: AddressRequest,
        @Header("Authorization") authToken: String
    ): Call<ApiResponse<AddressResult>>

    @POST("updateMyAddress")
    fun updateMyAddress(
        @Body addressRequest: AddressRequest,
        @Header("Authorization") authToken: String
    ): Call<ApiResponse<AddressResult>>

    @GET("myAddresses")
    fun getAddresses(@Header("Authorization") authToken: String): Call<ApiResponse<AddressResult>>

    @GET("getCountries")
    fun getCountries(@Header("Authorization") authToken: String): Call<ApiListResponse<Country>?>

    @GET("getCountryStates/{id}")
    fun getStates(
        @Path("id") countryId: String,
        @Header("Authorization") authToken: String
    ): Call<ApiResponse<Country>>

    @GET("getStateCities/{id}")
    fun getCities(
        @Path("id") stateId: String,
        @Header("Authorization") authToken: String
    ): Call<ApiResponse<State>>

    @POST("deleteMyAddress/{id}")
    fun removeMyAddress(
        @Path("id") addressId: String,
        @Header("Authorization") authToken: String
    ): Call<ApiResponse<AddressResult>>

    @POST("makeMyAddressDefault/{id}")
    fun setAddressAsDefault(
        @Path("id") addressId: String,
        @Header("Authorization") authToken: String
    ): Call<ApiResponse<AddressResult>>

    @GET("productDetails/{product_id}")
    fun getProductDetail(
        @Path("product_id") productId: String,
        @Header("Authorization") authToken: String
    ): Call<ApiResponse<ProductDetailResult>>

    /*@POST("social_login")
    fun socilLogin(
        @Body registrationRequest: RequestBody): Call<ResponseBody>*/

    @POST("social_login")
    fun socialLogin(
        @Body phoneVerificationRequest: WelcomeRequest
    ): Call<ApiResponse<AuthResult>>

    @FormUrlEncoded
    @POST("checkPincode")
    fun checkPincode(
        @Header("Authorization") auth: String?,
        @Field("pin_code") variationId: String
    ): Call<ResponseBody?>


    @FormUrlEncoded
    @POST("sendOTP")
    fun sendOTP(
        @Field("phone") variationId: String,
        @Field("type") type: String,
        @Field("country_code") countryCode:String

    ): Call<ResponseBody?>

    @FormUrlEncoded
    @POST("sendOTP")
    fun resendOTP(
        @Field("phone") variationId: String,
        @Field("type") type: String,
        @Field("isResend") isResend: String,
        @Field("country_code") countryCode:String

    ): Call<ResponseBody?>

    @FormUrlEncoded
    @POST("verifyOTP")
    fun verifyOTP(
        @Field("otp") variationId: String,
        @Field("phone") phone: String

    ): Call<ResponseBody?>


    @FormUrlEncoded
    @POST("addOrUpdateCart")
    fun addOrUpdatecart(
        @Header("Authorization") auth: String?,
        @Field("variation_id") variationId: String,
        @Field("quantity") quantity: Int
    ): Call<ApiResponse<CartData>>

    @GET("myCart")
    fun getMyCart(@Header("Authorization") authToken: String): Call<CartModel>

    @FormUrlEncoded
    @POST("removeFromMyCart")
    fun removeFromMyCart(
        @Header("Authorization") authToken: String,
        @Field("cart_id") cartId: Int
    ): Call<ApiResponse<CartData>>


    @POST("placeMyOrder")
    fun placeOrder(
        @Body obj: CheckOutDataModel,
        @Header("Authorization") authToken: String
    ): Call<ApiResponse<OrderConfirmation>>


//    @Headers( "Content-Type: application/json" )
//    @POST("updatePaymentStatus")
//    fun updatePaymentStatus(
//        @Body obj: JsonObject,
//        @Header("Authorization") authToken: String
//    ): Call<ResponseBody?>


    @FormUrlEncoded
    @POST("updatePaymentStatus")
    fun updatePaymentStatus(
        // @Body obj: JsonObject,
        @Field("razorpay_payment_id") razorpay_payment_id: String,
        @Field("gadget_order_id") order_id: String,

        @Header("Authorization") authToken: String
    ): Call<ResponseBody?>

    @FormUrlEncoded
    @POST("getDeliveryCharges")
    fun getDeliveryPrice(
        @Header("Authorization") authToken: String,
        @Field("user_pincode") pincode: String,
        @Field("weight") weight: String

    ): Call<ResponseBody?>

    @FormUrlEncoded
    @POST("updateFailedPaymentStatus")
    fun fetchAllSectionProducts(
        @Header("Authorization") authToken: String,
        @Field("order_id") order_id: String,
        @Field("is_buy_now") is_buy_now: String

    ): Call<ResponseBody?>


    @Headers( "Content-Type: application/json" )
    @POST("getVariants")
    fun getVariants(
        @Body obj: JsonObject,
        @Header("Authorization") authToken: String
    ): Call<ApiResponse<ProductDetailResult>?>



    @GET("myOrders")
    fun getMyOrders(
        @Query("page") pageNumber: Int?,
        @Header("Authorization") authToken: String
    ): Call<MyOrderResult>

    @GET("getSettings")
    fun getSettings(): Call<SettingModel>

    @FormUrlEncoded
    @POST("sectionProducts")
    fun fetchAllSectionProducts(
        @Query("page") pageNumber: Int?,
        @Header("Authorization") authToken: String,
        @Field("section_id") sectionId: String
    ): Call<ApiResponse<SectionProductData>?>


    @FormUrlEncoded
    @POST("bannerProducts")
    fun fetchBannerSectionProducts(
        @Query("page") pageNumber: Int?,
        @Header("Authorization") authToken: String,
        @Field("banner_id") sectionId: String
    ): Call<ApiResponse<ProductsOfSubResult>?>

    @FormUrlEncoded
    @POST("sectionProducts")
    fun getOfferDetail(
        @Query("page") pageNumber: Int?,
        @Header("Authorization") authToken: String,
        @Field("section_id") sectionId: String
    ): Call<ApiResponse<SectionProductData>?>



    @FormUrlEncoded
    @POST("sectionProducts")
    fun fetchAllSectionProductsNew(
        @Query("page") pageNumber: Int?,
        @Header("Authorization") authToken: String,
        @Field("section_id") sectionId: String
    ): Call<ApiResponse<PopularSectionProductData>?>

    @FormUrlEncoded
    @POST("bannerProducts")
    fun fetchAllBannerProducts(
        @Query("page") pageNumber: Int?,
        @Header("Authorization") authToken: String,
        @Field("banner_id") bannerId: String
    ): Call<ApiResponse<SectionProductData>?>

    @Headers( "Content-Type: application/json" )
    @POST("changeNotificationStatus")
    fun changeNotificationStatus(

        @Header("Authorization") authToken: String
    ): Call<ResponseBody?>


    @FormUrlEncoded
    @POST("getCouponDetail")
    fun getOfferDetail(
        @Field("coupon_code") coupon_code: String,
        @Field("variation_id") variation_id: String,




        @Header("Authorization") authToken: String
    ): Call<ResponseBody?>

    @FormUrlEncoded
    @POST("addOrUpdateOrderReview")
    fun addOrUpdateOrderReview(
        @Field("variation_id") variation_id: String,
        @Field("description") description: String,
        @Field("title") title: String,
        @Field("rating") rating: String,





        @Header("Authorization") authToken: String
    ): Call<ResponseBody?>

    @GET("myReviews")
    fun myReviews(
        @Header("Authorization") authToken: String
    ): Call<ApiResponse<ReviewData?>>

    @FormUrlEncoded
    @POST("removeReview")
    fun removeReview(
        @Field("review_id") review_id: String,
        @Header("Authorization") authToken: String
    ): Call<ResponseBody?>

    @Multipart
    @POST("cancelOrderProduct")
    fun cancelOrder(
        @Part("order_id") orderId : RequestBody ,
        @Part("order_product_id") order_product_id : RequestBody ,
        @Part("cancel_reason") cancel_reason : RequestBody,
        @Part file:ArrayList<MultipartBody.Part>?,
        @Header("Authorization") authToken: String

    ):Call<ResponseBody?>

    @Multipart
    @POST("cancelOrder")
    fun cancelCompleteOrder(
        @Part file:ArrayList<MultipartBody.Part>?,
        @Part("cancel_reason") cancel_reason:RequestBody,
        @Part("order_id")order_id: RequestBody,
        @Header("Authorization") authToken: String

    ):Call<ResponseBody?>

    @Multipart
    @POST("refundOrReplaceOrderProduct")
    fun replaceAndRetrunOrder(
        @Part("order_id") orderId : RequestBody ,
        @Part("order_product_id") order_product_id : RequestBody ,
        @Part("request_type") request_type : RequestBody ,
        @Part("reason") reason : RequestBody,
        @Part file:ArrayList<MultipartBody.Part>?,
        @Header("Authorization") authToken: String

    ):Call<ResponseBody?>

    @FormUrlEncoded
    @POST("orderStatuses")
    fun truckOrder(
        @Field("order_product_id") order_product_id : String ,

        @Header("Authorization") authToken: String
    ):Call<ApiResponse<TrackListModel?>>

}