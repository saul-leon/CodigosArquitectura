package fuzzer.com.codigosarquitectura.restAPI.endpoints;



import java.util.ArrayList;

import fuzzer.com.codigosarquitectura.restAPI.constantes.ConstantesRestAPI;

import fuzzer.com.codigosarquitectura.restAPI.models.Codigos;
import fuzzer.com.codigosarquitectura.restAPI.models.FirebaseRequestNotification;
import fuzzer.com.codigosarquitectura.restAPI.models.Respuesta;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by omar on 31/01/17.
 */

public interface Endpoints {
//    @FormUrlEncoded
//    @POST(ConstantesRestAPI.ENDPOINT)
//    Call<Respuesta> obtenerDatosTransaccion(@Field("pa_FchInicio") String pa_FchInicio, @Field("pa_FchFin") String pa_FchFin, @Field("destinatario") String destinatario, @Field("fecha") String fechaActual, @Field("idTelefono") String idTelefono);


    @Headers({
            "content-type': application/json",
            "Authorization: key=AAAAsISpCi0:APA91bG6Orm8wZmCDCgRMKLlkZSQgF5RjVJc0gLx6wDXCekzuXgM1QXurRinNIn0sotQhxMkpJByX5A5E201d87fYpr91vAh5qY8p_nWf9kCeh583JJPBxiGtjUDWcxOtjtCyW6_gmRu"
    })
    @POST(ConstantesRestAPI.ENDPOINT)
    Call<Respuesta> obtenerDatosTransaccion(@Body FirebaseRequestNotification solicitud);
}
