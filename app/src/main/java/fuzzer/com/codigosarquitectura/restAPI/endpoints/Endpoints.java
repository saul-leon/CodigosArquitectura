package fuzzer.com.codigosarquitectura.restAPI.endpoints;

import fuzzer.com.codigosarquitectura.restAPI.constantes.ConstantesRestAPI;
import fuzzer.com.codigosarquitectura.restAPI.models.FirebaseRequestNotification;
import fuzzer.com.codigosarquitectura.restAPI.models.Respuesta;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Endpoints {

    @Headers({
        "content-type': application/json",
        "Authorization: key=AAAAsISpCi0:APA91bG6Orm8wZmCDCgRMKLlkZSQgF5RjVJc0gLx6wDXCekzuXgM1QXurRinNIn0sotQhxMkpJByX5A5E201d87fYpr91vAh5qY8p_nWf9kCeh583JJPBxiGtjUDWcxOtjtCyW6_gmRu"
    })
    @POST(ConstantesRestAPI.ENDPOINT)
    Call<Respuesta> obtenerDatosTransaccion(@Body FirebaseRequestNotification solicitud);
}
