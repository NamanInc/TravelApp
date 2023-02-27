package com.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA595KiFg:APA91bHa88SbyC6w2Ktvc3PFEbD3iusB1k2hjjYTDXvx3_TwuwcuYFuIqyd6_xlkixwrwDZRDFJJowqX1t1WrcbrsmXO9xs9pGhRI6ADj832ACnbKaGbaZuN2FztSoxV_l2ph527At5r"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);

}
