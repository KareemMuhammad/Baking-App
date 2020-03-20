package com.oikm.a100.bakingapp.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {
@GET ("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Bakery>> getBakery();
}
