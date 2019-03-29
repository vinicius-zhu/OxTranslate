package br.edu.ifsp.scl.oxtranslate.volley

import br.edu.ifsp.scl.oxtranslate.model.Translation
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

// Desserializador personalizado. Complexide O(N^2)
class TranslationListDeserializer : JsonDeserializer<List<Translation>> {
    // Função que desserializa o Json e retorna uma lista de objetos Translation
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<Translation> {
        // Recuperando o JsonArray “results” do Objeto completo Json
        var results: JsonArray? = json?.asJsonObject?.getAsJsonArray("results")
        // Juntando os JsonArrays “lexicalEntries” de todos “results” num só JsonArray, O(N^2)
        val lexicalEntries: JsonArray = JsonArray()
        results?.forEach {
            it?.let {
                lexicalEntries.addAll(it.asJsonObject?.getAsJsonArray("lexicalEntries"))
            }
        }
        // Juntando os JsonArrays “entries” de todos “lexicalEntries” num só JsonArray, O(N^2)
        val entries: JsonArray = JsonArray()
        lexicalEntries?.forEach {
            it?.let {
                entries.addAll(it.asJsonObject?.getAsJsonArray("entries"))
            }
        }
        // Juntando todos os JsonArrays “senses” de todos “entries” num só JsonArray, O(N^2)
        val senses: JsonArray = JsonArray()
        entries.forEach {
            it?.let {
                senses.addAll(it.asJsonObject?.getAsJsonArray("senses"))
            }
        }
        /* Juntando todos os JsonArrays “translations” de todos “senses” num só JsonArray,
        O(N^2) */
        val translations: JsonArray = JsonArray()
        senses.forEach {
            it?.let {
                translations.addAll(it.asJsonObject?.getAsJsonArray("translations"))
            }
        }
        // Extraindo os campos, criando os objetos Translation e colocando na lista de retorno
        val listaTranslations: MutableList<Translation> = mutableListOf()
        translations.forEach {
            it?.let {
                val translation: Translation = Translation()
                translation.language = it.asJsonObject?.get("language").toString()
                translation.text = it.asJsonObject?.get("text").toString()
                listaTranslations.add(translation)
            }
        }

        return listaTranslations
    }
}