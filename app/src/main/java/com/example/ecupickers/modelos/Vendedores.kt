package com.example.ecupickers.modelos


import kotlin.collections.HashMap

data class Vendedores(var locales:HashMap<String,Boolean>) {
    public constructor() : this(HashMap<String,Boolean>())
}