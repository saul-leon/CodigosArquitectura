package fuzzer.com.codigosarquitectura.restAPI.adapter;


import fuzzer.com.codigosarquitectura.restAPI.constantes.ConstantesRestAPI;
import fuzzer.com.codigosarquitectura.restAPI.endpoints.Endpoints;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by omar on 31/01/17.
 */
public class RestApiAdapter {
    public Endpoints establecerConexionRestAPI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantesRestAPI.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(Endpoints.class);
    }
}
