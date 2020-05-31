package com.example.ecupickers.modelos


import kotlin.collections.HashMap

data class Vendedores(var locales:HashMap<String,Boolean>,var id:String) {
    public constructor() : this(HashMap<String,Boolean>(),"def")
}