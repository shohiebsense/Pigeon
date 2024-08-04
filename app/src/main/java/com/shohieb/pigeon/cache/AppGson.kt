package com.shohieb.pigeon.cache

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val appGson: Gson = GsonBuilder()
    .registerTypeAdapter(LocalDate::class.java, LocalDateSerializer())
    .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
    .create()

class LocalDateSerializer : com.google.gson.JsonSerializer<LocalDate> {
    override fun serialize(src: LocalDate, typeOfSrc: java.lang.reflect.Type, context: com.google.gson.JsonSerializationContext): com.google.gson.JsonElement {
        return com.google.gson.JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }
}

class LocalDateDeserializer : com.google.gson.JsonDeserializer<LocalDate> {
    override fun deserialize(json: com.google.gson.JsonElement, typeOfT: java.lang.reflect.Type, context: com.google.gson.JsonDeserializationContext): LocalDate {
        return LocalDate.parse(json.asString, DateTimeFormatter.ISO_LOCAL_DATE)
    }
}
