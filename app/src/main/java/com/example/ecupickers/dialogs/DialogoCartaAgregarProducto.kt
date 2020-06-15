package com.example.ecupickers.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecupickers.R
import com.example.ecupickers.adapters.CategoriasAdapter
import com.example.ecupickers.adapters.MenusAdapter
import com.example.ecupickers.clases.Productos
import com.example.ecupickers.constantes.EnumCamposDB
import com.example.ecupickers.constantes.EnumCategoria
import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.factory.DbReference
import com.example.ecupickers.modelos.Alimento
import com.example.ecupickers.modelos.Producto
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.carta_agregar_producto.view.*

class DialogoCartaAgregarProducto(editar: Boolean?) : BottomSheetDialogFragment() {
    private lateinit var menuTituloText: TextView
    private lateinit var btnAgregar: Button
    private lateinit var nombreProducto: EditText
    private lateinit var precioProducto: EditText
    private lateinit var descripcionProducto: EditText
    private lateinit var tiempoEstimadoInicio: EditText
    private lateinit var timpoEstimadoFin: EditText
    private lateinit var comboCategoriasProducto: Spinner
    private lateinit var comboMenusProductos: Spinner
    private lateinit var rvCategoriaProducto: RecyclerView
    private lateinit var rvMenusPreductos: RecyclerView
    private lateinit var titulosMenus: ArrayList<String>
    private lateinit var idsMenus: ArrayList<String>
    private lateinit var idLocal: String
    private lateinit var nombreLocal: String
    private lateinit var adapterCategorias: CategoriasAdapter
    private lateinit var adapterMenus: MenusAdapter
    private lateinit var idProducto: String
    private lateinit var camposValorProducto: HashMap<EnumCamposDB, Any>
    private var listaCategoriasTag = HashMap<Int, EnumCategoria>()
    private var categoriasElegidasL = ArrayList<String>()
    private var categoriasElegidasMap = HashMap<String, Boolean>()
    private var menuElegidosM = HashMap<String, Boolean>()
    private var menusElegidosL = ArrayList<String>()
    private var menusElegidosIdL = ArrayList<String>()
    private var menusElegidosIdM = HashMap<String, Boolean>()
    private var editar = editar

    companion object {
        var MENUSTITULOS: String = "menusTitulos"
        var IDSMENUS: String = "idsMenus"
        var IDLOCAL: String = "idLocal"
        var NOMBRELOCAL: String = "nombreLocal"
        var ADAPTERMENU: String = "adapterMenus"
        var ADAPTERCATEGORIA: String = "adapterCategorias"
        var IDPRODUCTO: String = "idProducto"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(R.layout.carta_agregar_producto, container, false)
        btnAgregar = root.btnCartaAgregarProductoCrear
        nombreProducto = root.txtCartaProductoNombre
        precioProducto = root.txtcartaProductoPrecio
        descripcionProducto = root.txtcartaProductoDescripcion
        tiempoEstimadoInicio = root.txtCartaProductoTiempoEstimadoInicio
        timpoEstimadoFin = root.txtCartaProductoTiempoEstimadoFin
        comboCategoriasProducto = root.spinnerCartaAgregarProductoCategoria
        rvCategoriaProducto = root.rvCartaAgregarProductoCategoria
        comboMenusProductos = root.spinnerCartaAgregarProductoMenu
        rvMenusPreductos = root.rvCartaAgregarProductoMenu
        menuTituloText = root.txtTituloMenuCartaAgregarProducto
        return root
    }

    override fun onStart() {
        super.onStart()
        llenarCategoriasTags()
        arguments?.let {
            if (it.containsKey(MENUSTITULOS)) {
                this.titulosMenus = it.getStringArrayList(MENUSTITULOS)!!
            }
            if (it.containsKey(IDSMENUS)) {
                idsMenus = it.getStringArrayList(IDSMENUS)!!
            }
            if (it.containsKey(IDLOCAL)) {
                idLocal = it.getString(IDLOCAL)!!
            }
            if (it.containsKey(NOMBRELOCAL)) {
                nombreLocal = it.getString(NOMBRELOCAL)!!
            }
            if (it.containsKey(IDPRODUCTO)) {
                idProducto = it.getString(IDPRODUCTO)!!
            }
        }
        if (checkLateInitsVar()) {
            titulosMenus?.let { titulos ->
                idsMenus?.let { ids ->
                    //  setMenus(titulos,ids)
                    comboMenusProductos.let {
                        titulos.add(0, "Elige un menu")
                        val adaptador: ArrayAdapter<String> = ArrayAdapter(
                            requireContext(), android.R.layout.simple_expandable_list_item_1,
                            titulos
                        )
                        it.adapter = adaptador
                        it.onItemSelectedListener = gestionarSeleccionComboMenus()
                    }
                }
            }
        }
        comboCategoriasProducto?.let {

            val adaptador: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.opciones_categorias, android.R.layout.simple_expandable_list_item_1
            )
            it.adapter = adaptador
            it.onItemSelectedListener = gestionarSeleccionComboCategoria()
        }
        if (categoriasElegidasMap.size > 0) {
            llenarRecyclerCategorias(categoriasElegidasMap)
        }
        if (menuElegidosM.size > 0) {
            llenarRecyclerMenus(menuElegidosM)
        }

        editar?.let {
            if (it && checkvarNecesariasParaEditar()) {
                modoEditar()
                btnAgregar.text = "Actualizar"
                colocarValoresProductoEditar()
            } else {
                btnAgregar.setOnClickListener { agregarMenuDB() }
            }
        }
    }

    private fun actualizarDatosProducto(idProducto: String, idsMenus: HashMap<String, Boolean>) {
        var campos = getValoresFormActualizar()
        var filds = arrayListOf(
            nombreProducto,
            precioProducto,
            descripcionProducto,
            tiempoEstimadoInicio,
            timpoEstimadoFin
        )
        if (comprobarCampos(filds)) {
            val productoManager = Productos()
            productoManager.gestionarCampo(campos, idLocal, idProducto, idsMenus)
            Toasty.success(
                requireContext(),
                "Se actualizo con exito los datos del producto:${nombreProducto.text.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getValoresFormActualizar(): HashMap<EnumCamposDB, Any> {
        val camposValor = HashMap<EnumCamposDB, Any>()
        camposValor[EnumCamposDB.NOMBRE] = nombreProducto.text.toString()
        camposValor[EnumCamposDB.PRECIO] = precioProducto.text.toString()
        camposValor[EnumCamposDB.DESCRIPCION] = descripcionProducto.text.toString()
        actualizarCategoriasElegidasConAdapter()
        camposValor[EnumCamposDB.MIEMBROSCATEGORIAS] = categoriasElegidasMap
        camposValor[EnumCamposDB.TIEMPOESTIMADO] =
            "${tiempoEstimadoInicio.text}-${timpoEstimadoFin.text}"
        return camposValor
    }

    private fun actualizarCategoriasElegidasConAdapter() {
        if (checkAdapterCategorias()) {
            for (categoria in categoriasElegidasMap) {
                for (cat in adapterCategorias.getCategorias()) {
                    var equeal = categoria.key == cat
                    categoriasElegidasMap[categoria.key] = equeal
                    if (equeal) {
                        break
                    }
                }
                if (adapterCategorias.getCategorias().size == 0) {
                    categoriasElegidasMap[categoria.key] = false
                }
            }
        }
    }

    private fun traerProductoPorId(qry: Query) {
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    var alimento = data.getValue(Alimento::class.java)
                    nombreProducto.setText(alimento!!.nombre!!)
                    precioProducto.setText(alimento!!.precio)
                    descripcionProducto.setText(alimento!!.descripcion)
                    categoriasElegidasMap = alimento.miembroscategorias
                    llenarRecyclerCategorias(categoriasElegidasMap)
                    var hora = alimento.tiempoEstimado.split("-")
                    tiempoEstimadoInicio.setText(hora[0])
                    timpoEstimadoFin.setText(hora[1])
                    btnAgregar.setOnClickListener {
                        actualizarDatosProducto(
                            idProducto,
                            alimento.miembrosMenus
                        )
                    }
                }
            }

        }
        qry.addValueEventListener(listener)
    }

    private fun colocarValoresProductoEditar() {
        var referencia = DbReference.getRef(EnumReferenciasDB.ROOT)
        var qry = referencia.child("${EnumReferenciasDB.MIEMBROSALIMENTOS.rutaDB()}/${idLocal}")
            .orderByKey().equalTo(idProducto)
        traerProductoPorId(qry)
    }

    private fun modoEditar() {
        menuTituloText.isVisible = false
        comboMenusProductos.isVisible = false
    }

    private fun checkvarNecesariasParaEditar(): Boolean {
        return this::idProducto.isInitialized && this::idLocal.isInitialized
    }

    private fun actualizarLista(tag: String) {

        when (tag) {
            ADAPTERCATEGORIA -> {
                if (checkAdapterCategorias()) {
                    for (categoria in categoriasElegidasMap) {
                        for (cat in adapterCategorias.getCategorias()) {
                            var equeal = categoria.key == cat
                            categoriasElegidasMap[categoria.key] = equeal
                            if (equeal) {
                                break
                            }
                        }
                        if (adapterCategorias.getCategorias().size == 0) {
                            categoriasElegidasMap[categoria.key] = false
                        }
                    }
                }
            }
            ADAPTERMENU -> {
                if (checkAdapterMenus()) {
                    for (menu in menuElegidosM) {
                        for (men in adapterMenus.getMenus()) {
                            //menu.key==men retorna true o false y asigna a la clave si
                            //corresponde a la mismo titulo
                            menuElegidosM[menu.key] = menu.key == men
                        }
                        if (adapterMenus.getMenus().size == 0) {
                            menuElegidosM[menu.key] = false

                        }
                    }
                }
            }

        }

    }

    private fun agregarMenuDB() {
        val campos = arrayListOf(
            nombreProducto,
            descripcionProducto,
            tiempoEstimadoInicio,
            timpoEstimadoFin, precioProducto
        )
        if (comprobarCampos(campos)) {

            val nombreProducto = nombreProducto.text.toString()
            val precio = precioProducto.text.toString()
            val descripcion = descripcionProducto.text.toString()
            val local = nombreLocal
            val tiempoEstimado =
                "${tiempoEstimadoInicio.text}-${timpoEstimadoFin.text}"
            var productoManager = Productos()
            val producto = Producto()
            producto.apply {
                this.nombre = nombreProducto
                this.precio = precio
                this.descripcion = descripcion
                this.atendidos = "0"
                this.calificacion = "0"
                this.local = local
            }
            val idProducto = productoManager.crearProducto(producto)

            for (menu in menuElegidosM.keys) {
                if (titulosMenus.contains(menu)) {
                    menusElegidosIdM[idsMenus[titulosMenus.indexOf(menu) - 1]] = true
                }
            }
            var alimento = productoManager.crearAlimento(
                idProducto,
                producto,
                tiempoEstimado,
                menusElegidosIdM,
                categoriasElegidasMap,
                idLocal
            )
            for (menu in menusElegidosIdM) {
                if (menu.value) {
                    menusElegidosIdL.add(menu.key)
                }
            }

            productoManager.añadirProductoAMenu(menusElegidosIdL, idLocal, idProducto, alimento)
            //limpio las listas para que la windows se actualice y quede limpio los recyclers
            menusElegidosIdL.clear()
            menusElegidosL.clear()
            menuElegidosM.clear()
            menusElegidosIdM.clear()
            categoriasElegidasL.clear()
            categoriasElegidasMap.clear()
            llenarRecyclerMenus(menusElegidosIdM)
            llenarRecyclerCategorias(categoriasElegidasMap)
            Toasty.success(
                requireContext(),
                "Se añadido con exito el producto", Toast.LENGTH_SHORT, true
            ).show()
        }
    }

    private fun llenarCategoriasTags() {
        listaCategoriasTag[0] = EnumCategoria.ALMUERZO
        listaCategoriasTag[1] = EnumCategoria.DESAYUNOS
        listaCategoriasTag[2] = EnumCategoria.MERIENDAS
        listaCategoriasTag[3] = EnumCategoria.POSTRES
        listaCategoriasTag[4] = EnumCategoria.BBQ
        listaCategoriasTag[5] = EnumCategoria.COMIDARAPIDA
        listaCategoriasTag[6] = EnumCategoria.MARISCOS
        listaCategoriasTag[7] = EnumCategoria.POLLOS
        listaCategoriasTag[8] = EnumCategoria.HELADOS
        listaCategoriasTag[9] = EnumCategoria.HAMBURGUESAS
        listaCategoriasTag[10] = EnumCategoria.PIZZAS


    }

    private fun gestionarSeleccionComboCategoria(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    actualizarLista(ADAPTERCATEGORIA)
                    categoriasElegidasMap[listaCategoriasTag[position - 1]!!.getCategoria()] = true
                    llenarRecyclerCategorias(categoriasElegidasMap)
                    comboCategoriasProducto.setSelection(0, true)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun gestionarSeleccionComboMenus(): AdapterView.OnItemSelectedListener {

        return object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) {

                    actualizarLista(ADAPTERMENU)
                    menuElegidosM[titulosMenus[position]] = true
                    llenarRecyclerMenus(menuElegidosM)
                    comboMenusProductos.setSelection(0, true)
                }
            }


        }
    }


    private fun llenarRecyclerCategorias(categoriasElegidas: HashMap<String, Boolean>) {


        for (categoriaElegida in categoriasElegidas) {
            if (categoriaElegida.value && !categoriasElegidasL.contains(categoriaElegida.key)) {
                categoriasElegidasL.add(categoriaElegida.key)
            }
        }


        rvCategoriaProducto.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapterCategorias = CategoriasAdapter(categoriasElegidasL)
            adapter = adapterCategorias
        }
    }

    private fun llenarRecyclerMenus(menusElegidos: HashMap<String, Boolean>) {
        for (menuElegido in menusElegidos) {
            if (menuElegido.value && !menusElegidosL.contains(menuElegido.key)) {
                menusElegidosL.add(menuElegido.key)
            }
        }
        rvMenusPreductos.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapterMenus = MenusAdapter(menusElegidosL)
            adapter = adapterMenus
        }

    }

    private fun comprobarCampos(arrayList: ArrayList<EditText>): Boolean {
        var validar = true
        for (campo in arrayList) {
            if (campo.text.isBlank()) {
                campo.error = "compruebe que el campo este lleno correctamente"
                validar = false
            }
            if ((campo == tiempoEstimadoInicio ||
                        campo == timpoEstimadoFin) && campo.text.toString().length > 3
            ) {
                campo.error = "el campo solo puede contener maximo 3 digitos"
                validar = false
            }
        }
        return validar
    }

    private fun checkLateInitsVar(): Boolean {
        return this::idsMenus.isInitialized && this::idLocal.isInitialized &&
                this::titulosMenus.isInitialized && this::nombreLocal.isInitialized
    }

    private fun checkAdapterCategorias(): Boolean {
        return this::adapterCategorias.isInitialized
    }

    private fun checkAdapterMenus(): Boolean {
        return this::adapterMenus.isInitialized
    }


}