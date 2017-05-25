package fuzzer.com.codigosarquitectura.restAPI.endpoints;



import fuzzer.com.codigosarquitectura.restAPI.constantes.ConstantesRestAPI;
import fuzzer.com.codigosarquitectura.restAPI.models.DatosTransaccionResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by omar on 31/01/17.
 */

public interface Endpoints {
    @FormUrlEncoded
    @POST(ConstantesRestAPI.ENDPOINT)
    Call<DatosTransaccionResponse> obtenerDatosTransaccion(@Field("pa_FchInicio") String pa_FchInicio, @Field("pa_FchFin") String pa_FchFin);

}
