package com.anzhari.hrmipnetmobile.network;

import android.util.ArrayMap;

import com.anzhari.hrmipnetmobile.model.Attachment;
import com.anzhari.hrmipnetmobile.model.ResponseAttachment;
import com.anzhari.hrmipnetmobile.model.ResponseAttachments;
import com.anzhari.hrmipnetmobile.model.ResponseBase;
import com.anzhari.hrmipnetmobile.model.ResponseLogin;
import com.anzhari.hrmipnetmobile.model.ResponseNationality;
import com.anzhari.hrmipnetmobile.model.ResponsePersonalDetail;
import com.anzhari.hrmipnetmobile.model.ResponseReligion;
import com.anzhari.hrmipnetmobile.model.ResponseWorkshift;
import com.google.gson.JsonObject;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiServices {

    @POST("login")
    Single<ResponseLogin> login(@Body JsonObject object);

    @GET("myinfo/personaldetail")
    Single<ResponsePersonalDetail> getPersonalDetail(@HeaderMap ArrayMap<String, String> headers);

    @PUT("myinfo/personaldetail")
    Single<ResponseBase> updatePersonalDetail(@HeaderMap ArrayMap<String, String> headers,
                                              @Body JsonObject object);

    @GET("myinfo/personaldetail/attachment")
    Single<ResponseAttachments> getAttachments(@HeaderMap ArrayMap<String, String> headers,
                                               @Query("file_id") String fileId);

    @GET("myinfo/personaldetail/attachment")
    Single<ResponseAttachment> getAttachment(@HeaderMap ArrayMap<String, String> headers,
                                             @Query("file_id") String fileId);

    @POST("myinfo/personaldetail/attachment")
    Single<ResponseBase> uploadAttachment(@HeaderMap ArrayMap<String, String> headers,
                                          @Body JsonObject object);

    @PUT("myinfo/personaldetail/attachment")
    Single<ResponseBase> updateAttachment(@HeaderMap ArrayMap<String, String> headers,
                                          @Body JsonObject object);

    @DELETE("myinfo/personaldetail/attachment")
    Single<ResponseBase> deleteAttachment(@HeaderMap ArrayMap<String, String> headers,
                                          @Body JsonObject object);

    @GET("nationality")
    Single<ResponseNationality> getNationality(@HeaderMap ArrayMap<String, String> headers);

    @GET("workshift")
    Single<ResponseWorkshift> getWorkshift(@HeaderMap ArrayMap<String, String> headers);

    @GET("religion")
    Single<ResponseReligion> getReligion(@HeaderMap ArrayMap<String, String> headers);

}
