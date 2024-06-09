package com.br.fitagro_frontend.data.remote.response

import com.google.gson.annotations.SerializedName

data class SearchFruitResponse(
    @SerializedName("NR_REGISTRO")
    val nrRegistro: Int? = null,
    @SerializedName("MARCA_COMERCIAL")
    val marcaComercial: String? = null,
    @SerializedName("FORMULACAO")
    val formulacao: String? = null,
    @SerializedName("INGREDIENTE_ATIVO")
    val ingredienteAtivo: String? = null,
    @SerializedName("TITULAR_DE_REGISTRO")
    val titularRegistro: String? = null,
    @SerializedName("CLASSE")
    val classe: String? = null,
    @SerializedName("CULTURA")
    val cultura: String? = null,
    @SerializedName("PRAGA_NOME_CIENTIFICO")
    val pragaNomeCientifico: String? = null,
    @SerializedName("PRAGA_NOME_COMUM")
    val pragaNomeComum: String? = null,
    @SerializedName("EMPRESA_PAIS_TIPO")
    val empresaPaisTipo: String? = null,
    @SerializedName("CLASSE_TOXICOLOGICA")
    val classeToxicologica: String? = null,
    @SerializedName("CLASSE_AMBIENTAL")
    val classeAmbiental: String? = null,
    @SerializedName("ORGANICOS")
    val organicos: String? = null,
    @SerializedName("SITUACAO")
    val situacao: Boolean? = null,
)
