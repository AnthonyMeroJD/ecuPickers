package com.example.ecupickers.ui.gestionarLocal


import android.app.TimePickerDialog

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.ecupickers.R
import com.example.ecupickers.adapters.CategoriaAdapter
import com.example.ecupickers.adapters.MiembrosMenusViewPagerAdapter
import com.example.ecupickers.clases.Locales
import com.example.ecupickers.clases.Productos
import com.example.ecupickers.constantes.EnumCamposDB
import com.example.ecupickers.constantes.EnumCategoria
import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.constantes.EnumTipoLocal
import com.example.ecupickers.factory.DbReference
import com.example.ecupickers.modelos.Local
import com.example.ecupickers.modelos.Producto


import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.*
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fondo_local2.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


import kotlinx.android.synthetic.main.carta_agregar_producto.view.*
import kotlinx.android.synthetic.main.fondo_local2.*
import kotlinx.android.synthetic.main.fondo_local2.view.txtHoraInicio
import kotlinx.android.synthetic.main.fondo_local2.view.txthoraCierre


class FragmentLocal : Fragment() {
    private lateinit var uid: String
    private lateinit var idLocal: String
    private lateinit var ciudad: String
    private lateinit var editarBtn: Button
    private lateinit var cancelarBtn: Button
    private lateinit var guardarBtn: Button
    private lateinit var nombrelocal: EditText
    private lateinit var qryUser: Query
    private lateinit var ref: DatabaseReference
    private lateinit var rvCategorias: RecyclerView
    private lateinit var categorias: HashMap<String, Boolean>
    private lateinit var titulos: HashMap<String, Boolean>
    private lateinit var comboCategorias: Spinner
    private lateinit var titulosMenu: TabLayout
    private lateinit var horaInicio: EditText
    private lateinit var horaCierre: EditText
    private lateinit var pager: ViewPager2
    private lateinit var añadirProductoBtn: Button
    private lateinit var nombresMenus: HashMap<String, String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid = it.getString("idUser")!!
            ciudad= it.getString("ciudad")!!
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_local, container, false)

        Toasty.warning(
            root.context,
            "Espera mientras cargan los datos de tu local.", Toast.LENGTH_LONG, true
        ).show()
        ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        //editarBtn = root.buttonEditarLocal
        //cancelarBtn = root.buttonCancelarCambiosLocal
        //guardarBtn = root.buttonGuardarCambiosLocal
        horaInicio = root.txtHoraInicio
        horaCierre = root.txthoraCierre
       // guardarBtn.isEnabled = false
        //cancelarBtn.isEnabled = false
        horaInicio.isEnabled = true
        horaCierre.isEnabled = true
        nombrelocal = root.editText4
        comboCategorias = root.spinnerCategoriasLocal
        titulosMenu = root.titulosMenuHorizontal
        qryUser = ref.child(EnumReferenciasDB.USERS.rutaDB()).orderByKey().equalTo(uid)
        rvCategorias = root.recyclerView
        categorias = HashMap()
        titulos = HashMap()
        //borrar este toast
        Toasty.warning(
            root.context,
            "${ciudad}/${uid}" , Toast.LENGTH_LONG, true
        ).show()
        pager = root.contenidoMenuHorizontalLocal
        añadirProductoBtn = root.buttonAgregarNuevoProducto
        nombresMenus = HashMap()

        return root
    }

    override fun onStart() {
        super.onStart()
        //esta funcion consulta y dentro llama a la funcion que pinta los datos del local
        traerLocal(uid,ciudad)
        //combo categorias
        comboCategorias?.let {
            val adaptador: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.opciones_categorias, android.R.layout.simple_expandable_list_item_1
            )
            it.adapter = adaptador
        }
        comboCategorias.onItemSelectedListener = agregarCategoria()
        //no abrir teclado del editText
        horaInicio.setRawInputType(InputType.TYPE_NULL)
        horaCierre.setRawInputType(InputType.TYPE_NULL)
        //abrir TimePicker al darle el primer click
        desplegarTimePicker(horaInicio)
        desplegarTimePicker(horaCierre)
        //boton que habilita la edicion
       // editarBtn.setOnClickListener { habilitarEdicion(true) }
        //mostrar carta de agregar productos
        mostrarProductoPopWindows(context, añadirProductoBtn)

    }

    fun manejarCartaAddProducto(window: PopupWindow) {
        val btnAgregar = window.contentView.btnCartaAgregarProductoCrear
        val nombreProducto = window.contentView.txtCartaProductoNombre
        val precioProducto = window.contentView.txtcartaProductoPrecio
        val descripcionProducto = window.contentView.txtcartaProductoDescripcion
        val tiempoEstimadoInicio = window.contentView.txtCartaProductoTiempoEstimadoInicio
        val timpoEstimadoFin = window.contentView.txtCartaProductoTiempoEstimadoFin
        val comboCategoriasProducto = window.contentView.spinnerCartaAgregarProductoCategoria
        val rvCategoriaProducto = window.contentView.rvCartaAgregarProductoCategoria
        val comboMenusProductos = window.contentView.spinnerCartaAgregarProductoMenu
        var rvMenusPreductos = window.contentView.rvCartaAgregarProductoMenu
        var categorias = HashMap<String, Boolean>()
        var menus = HashMap<String, Boolean>()
        var nombreMenu = ArrayList<String>()
        var idsMenus = ArrayList<String>()
        var menusIdElegido = HashMap<String, Boolean>()
        fun addCategoria(tag: EnumCategoria): ArrayList<String> {
            var categoriasL = ArrayList<String>()
            categorias.put(tag.getCategoria(), true)
            for (categoria in categorias) {
                categoriasL.add(categoria.key)
            }
            return categoriasL
        }

        fun comprobarCampos(arrayList: ArrayList<EditText>): Boolean {
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

        fun addMenu(tag: String, idMenu: String, eliminar: Boolean = false): ArrayList<String> {
            var menuL = ArrayList<String>()
            menus.put(tag, !eliminar)
            menusIdElegido.put(idMenu, true)
            for (menu in menus) {
                if (menu.value) {
                    menuL.add(menu.key)
                }
            }
            return menuL
        }
        nombreMenu.add(0, "seleccione el menu")
        for (nombre in nombresMenus.keys) {
            nombreMenu.add(nombre)
        }
        for (idMenu in nombresMenus.values) {
            idsMenus.add(idMenu)
        }
        comboCategoriasProducto?.let {
            val adaptador: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
                window.contentView.context,
                R.array.opciones_categorias,  android.R.layout.simple_expandable_list_item_1)

            it.adapter = adaptador
        }
        comboMenusProductos?.let {
            val adaptador: ArrayAdapter<String> = ArrayAdapter(
                window.contentView.context, android.R.layout.simple_spinner_item,
                nombreMenu
            )
            it.adapter = adaptador
        }

        var listenerCategoria = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                var categoriaTag: EnumCategoria
                when (position) {
                    1 -> {
                        categoriaTag = EnumCategoria.ALMUERZO
                        var categorias = addCategoria(categoriaTag)
                        llenarCategorias(categorias, rvCategoriaProducto, false)

                    }
                    2 -> {
                        categoriaTag = EnumCategoria.DESAYUNOS
                        var categorias = addCategoria(categoriaTag)
                        llenarCategorias(categorias, rvCategoriaProducto, false)
                    }
                    3 -> {
                        categoriaTag = EnumCategoria.MERIENDAS
                        var categorias = addCategoria(categoriaTag)
                        llenarCategorias(categorias, rvCategoriaProducto, false)
                    }
                    4 -> {
                        categoriaTag = EnumCategoria.POSTRES
                        var categorias = addCategoria(categoriaTag)
                        llenarCategorias(categorias, rvCategoriaProducto, false)
                    }
                    5 -> {
                        categoriaTag = EnumCategoria.BBQ
                        var categorias = addCategoria(categoriaTag)
                        llenarCategorias(categorias, rvCategoriaProducto, false)
                    }
                    6 -> {
                        categoriaTag = EnumCategoria.COMIDARAPIDA
                        var categorias = addCategoria(categoriaTag)
                        llenarCategorias(categorias, rvCategoriaProducto, false)
                    }
                    7 -> {
                        categoriaTag = EnumCategoria.MARISCOS
                        var categorias = addCategoria(categoriaTag)
                        llenarCategorias(categorias, rvCategoriaProducto, false)
                    }
                    8 -> {
                        categoriaTag = EnumCategoria.POLLOS
                        var categorias = addCategoria(categoriaTag)
                        llenarCategorias(categorias, rvCategoriaProducto, false)
                    }
                    9 -> {
                        categoriaTag = EnumCategoria.HELADOS
                        var categorias = addCategoria(categoriaTag)
                        llenarCategorias(categorias, rvCategoriaProducto, false)
                    }
                    10 -> {
                        categoriaTag = EnumCategoria.HAMBURGUESAS
                        var categorias = addCategoria(categoriaTag)
                        llenarCategorias(categorias, rvCategoriaProducto, false)
                    }
                    11 -> {
                        categoriaTag = EnumCategoria.PIZZAS
                        var categorias = addCategoria(categoriaTag)
                        llenarCategorias(categorias, rvCategoriaProducto, false)
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        comboCategoriasProducto.onItemSelectedListener = listenerCategoria

        var listenerMenu = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    var menusL = addMenu(nombreMenu[position], idsMenus[position - 1])
                    llenarCategorias(menusL, rvMenusPreductos, false)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        comboMenusProductos.onItemSelectedListener = listenerMenu
        btnAgregar.setOnClickListener {
            var campos = arrayListOf<EditText>(
                nombreProducto,
                descripcionProducto,
                tiempoEstimadoInicio,
                timpoEstimadoFin, precioProducto
            )

            if (comprobarCampos(campos)) {

                var nombreProducto = nombreProducto.text.toString()
                var precio = precioProducto.text.toString()
                var descripcion = descripcionProducto.text.toString()
                var local = nombrelocal.text.toString()
                var tiempoEstimado =
                    "${tiempoEstimadoInicio.text}-${timpoEstimadoFin.text}"
                var productoManager = Productos()
                var producto = Producto()
                producto.apply {
                    this.nombre = nombreProducto
                    this.precio = precio
                    this.descripcion = descripcion
                    this.atendidos = "0"
                    this.calificacion = "0"
                    this.local = local
                }
                var idProducto = productoManager.crearProducto(producto)
                var alimento = productoManager.crearAlimento(
                    idProducto,
                    producto,
                    tiempoEstimado,
                    menusIdElegido,
                    categorias,
                    idLocal
                )
                var idsMenus = ArrayList<String>()
                for (id in menusIdElegido) {
                    idsMenus.add(id.key)
                }
                productoManager.añadirProductoAMenu(idsMenus, idLocal, idProducto, alimento)
                Toasty.success(
                    window.contentView.context,
                    "Se añadido con exito el producto", Toast.LENGTH_SHORT, true
                ).show()
            }
        }


    }

    fun mostrarProductoPopWindows(context: Context?, btnAccion: Button) {
        val window = PopupWindow(context)
        val v = layoutInflater.inflate(R.layout.carta_agregar_producto, null)
        window.contentView = v
        window.isOutsideTouchable = true
        window.isFocusable = true
        window.animationStyle
        btnAccion.setOnClickListener {

            window.showAtLocation(btnAccion, Gravity.CENTER, 0, 0)
            manejarCartaAddProducto(window)
        }
    }


    fun desplegarTimePicker(editText: EditText) {

        editText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            v as EditText
            if (hasFocus) {
                val cal = Calendar.getInstance()
                val obtenerHora = TimePickerDialog.OnTimeSetListener { timePicker, hora, minuto ->
                    cal.set(Calendar.HOUR_OF_DAY, hora)
                    cal.set(Calendar.MINUTE, minuto)
                    v.setText(SimpleDateFormat("h:m a").format(cal.time))
                }
                TimePickerDialog(
                    context,
                    obtenerHora,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
                ).show()
            }
        }
        //abrir TimePicker al darle click nuevamente
        editText.setOnClickListener {
            it as EditText
            val cal = Calendar.getInstance()
            val obtenerHora = TimePickerDialog.OnTimeSetListener { timePicker, hora, minuto ->
                cal.set(Calendar.HOUR_OF_DAY, hora)
                cal.set(Calendar.MINUTE, minuto)
                it.setText(SimpleDateFormat("h:m a").format(cal.time))
            }
            TimePickerDialog(
                context,
                obtenerHora,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    fun habilitarEdicion(habilitar: Boolean) {
        if (habilitar) {
            nombrelocal.requestFocus()
        }
        nombrelocal.isEnabled = habilitar
        horaInicio.isEnabled = habilitar
        horaCierre.isEnabled = habilitar
        cancelarBtn.isEnabled = habilitar
        guardarBtn.isEnabled = habilitar
        var nombreLocal = nombrelocal.text.toString()
        var horaCierre = horaCierre.text.toString()
        var horaInicio = horaInicio.text.toString()
        if (habilitar) {
            Toasty.info(
                requireContext(),
                "Ahora puedes editar los campos de tu Local.", Toast.LENGTH_SHORT, true
            ).show()
            cancelarBtn.setOnClickListener {
                Toasty.info(
                    requireContext(),
                    "Has cancelado la edicion de los Datos de tu local.",
                    Toast.LENGTH_SHORT, true
                ).show()
                recuperarDatosAnteriores(
                    nombreLocal,
                    horaCierre,
                    horaInicio
                )
            }
            guardarBtn.setOnClickListener {
                guardarDatosLocalDB(
                    nombreLocal,
                    horaCierre,
                    horaInicio
                )
            }
        }

    }

    fun guardarDatosLocalDB(
        nombreLocal: String,
        horaCierreT: String,
        horaInicioT: String
    ) {
        var nuevoNombre = nombrelocal.text.toString()
        var horaCierreNuevo = horaCierre.text.toString()
        var horaInicioNuevo = horaInicio.text.toString()
        var local = Locales()
        if (nombrelocal.text.isBlank()) {
            Toasty.error(
                requireContext(),
                "El nombre de tu local no puede estar vacio",
                Toast.LENGTH_SHORT, true
            ).show()
            nombrelocal.requestFocus()
        } else {
            if (nombreLocal != nuevoNombre || horaCierreT != horaCierreNuevo || horaInicioT != horaInicioNuevo) {
                var tag: EnumCamposDB
                fun actualizar(tag: EnumCamposDB, campo: String) {
                    local.gestionarCampo(
                        campo, idLocal, EnumTipoLocal.RESTAURANTE,
                        ciudad, tag
                    )
                }
                if (nombreLocal != nuevoNombre) {
                    tag = EnumCamposDB.NOMBRE
                    actualizar(tag, nuevoNombre)
                    Toasty.success(
                        requireContext(), "Se a cambaido con exito el nombre!",
                        Toast.LENGTH_SHORT, true
                    ).show()
                }
                if (horaCierreT != horaCierreNuevo) {
                    tag = EnumCamposDB.HORACIERRE
                    actualizar(tag, horaCierreNuevo)
                    Toasty.success(
                        requireContext(), "Se a cambaido con exito la hora cierre!",
                        Toast.LENGTH_SHORT, true
                    ).show()
                }
                if (horaInicioT != horaInicioNuevo) {
                    tag = EnumCamposDB.HORAINICIO
                    actualizar(tag, horaInicioNuevo)
                    Toasty.success(
                        requireContext(), "Se a cambaido con exito la hora Inicio!",
                        Toast.LENGTH_SHORT, true
                    ).show()
                }
                Toasty.success(
                    requireContext(), "Se a cambaido con exito los datos!",
                    Toast.LENGTH_SHORT, true
                ).show()

            } else {
                Toasty.error(
                    requireContext(),
                    "Los datos no han sido modificados", Toast.LENGTH_SHORT, true
                ).show()
            }
        }
        habilitarEdicion(false)
    }

    fun recuperarDatosAnteriores(
        nombreLocal: String,
        horaCierre: String,
        horaInicio: String
    ) {
        nombrelocal.setText("${nombreLocal}")
        txtHoraInicio.setText("${horaInicio}")
        txthoraCierre.setText("${horaCierre}")
        habilitarEdicion(habilitar = false)
    }

    fun agregarCategoria(): AdapterView.OnItemSelectedListener {
        /*
        * esta funcion interna llena un aList de strings, que contiendra
        * una categoria y no se podra repetir ya que el hashmap toma como key la categoria
        * */
        fun agregar(categorias: HashMap<String, Boolean>): ArrayList<String> {
            var nCategoria = ArrayList<String>()
            for (categoria in categorias) {
                nCategoria.add(categoria.key)
            }
            return nCategoria
        }

        /*
        * esta funcion añade a la base de datos la categoria,
        * y agrega a la variable global de categorias la categoria que se selecciona en el
        * spinner,ojo estas categorias en la variable glogal CATEGORIAS son la key
        * */
        fun añadirDb(tag: EnumCategoria, local: Locales) {
            var categoriaCategorizada = ArrayList<EnumCategoria>()
            var categoriaTag = tag
            categoriaCategorizada.add(categoriaTag)
            //agrego la categoria a la variable global
            categorias.put("${categoriaTag.getCategoria()}", true)
            //paso la categoria global a la funcion que me retorna un array conlas categorias
            // sin repeticiones
            var nCategoria = agregar(categorias)
            //aqui llamo a la funcion que inserta la categoria a la base de datos
            local.gestionarCategoriaLocal(
                false, categoriaCategorizada, idLocal, ciudad
            )
            //
            llenarCategorias(nCategoria, rvCategorias, true)
        }

        var listener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var local = Locales()

                var categoriaTag: EnumCategoria
                when (position) {
                    1 -> {
                        categoriaTag = EnumCategoria.ALMUERZO

                        añadirDb(categoriaTag, local)
                    }
                    2 -> {
                        categoriaTag = EnumCategoria.DESAYUNOS
                        añadirDb(categoriaTag, local)
                    }
                    3 -> {
                        categoriaTag = EnumCategoria.MERIENDAS
                        añadirDb(categoriaTag, local)
                    }
                    4 -> {
                        categoriaTag = EnumCategoria.POSTRES
                        añadirDb(categoriaTag, local)
                    }
                    5 -> {
                        categoriaTag = EnumCategoria.BBQ
                        añadirDb(categoriaTag, local)
                    }
                    6 -> {
                        categoriaTag = EnumCategoria.COMIDARAPIDA
                        añadirDb(categoriaTag, local)
                    }
                    7 -> {
                        categoriaTag = EnumCategoria.MARISCOS
                        añadirDb(categoriaTag, local)
                    }
                    8 -> {
                        categoriaTag = EnumCategoria.POLLOS
                        añadirDb(categoriaTag, local)
                    }
                    9 -> {
                        categoriaTag = EnumCategoria.HELADOS
                        añadirDb(categoriaTag, local)
                    }
                    10 -> {
                        categoriaTag = EnumCategoria.HAMBURGUESAS
                        añadirDb(categoriaTag, local)
                    }
                    11 -> {
                        categoriaTag = EnumCategoria.PIZZAS
                        añadirDb(categoriaTag, local)
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        return listener
    }
    //esta funcion me sirve para traer los datos del local con una consulta al nodo usuario


    //esta funcion me consulta el local en el nodo MiembrosVendedores
    fun traerLocal(idUser: String, ciudad: String) {
        val qry = ref.child(
            "${EnumReferenciasDB.MIEMBROSVENDEDORES.rutaDB()}" +
                    "/${ciudad}/${idUser}"
        )
        val listener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                pintarDatosLocal(p0.key.toString())
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                pintarDatosLocal(p0.key.toString())

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                pintarDatosLocal(p0.key.toString())
            }

        }
        qry.addChildEventListener(listener)
    }

    fun pintarDatosLocal(idLocal: String) {
        fun atachCampos(nombre:String,horaInicioS: String,horaCierreS: String){
            nombrelocal.setText(nombre)
            horaInicio.setText(horaInicioS)
            horaCierre.setText(horaCierreS)
        }
        val qry =
            ref.child("${EnumReferenciasDB.LOCALES.rutaDB()}").orderByKey().equalTo(idLocal)
        var local :Local
        val listener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val categorias = ArrayList<String>()
                val menusTittles = ArrayList<String>()
                val menusId = ArrayList<String>()
                this@FragmentLocal.idLocal = String()
                this@FragmentLocal.idLocal = p0.key.toString()
                local = p0.getValue(Local::class.java)!!
                atachCampos(local.nombre,local.horaInicio,local.horaCierre)
                for (categoria in local.miembroscategorias) {
                    categorias.add(categoria.key)
                }

                for (menu in local.miembrosMenus) {
                    for (nombre in menu.value) {
                        menusTittles.add(nombre.value)
                        nombresMenus.put(nombre.value, menu.key)
                    }
                    menusId.add(menu.key)
                }

               // llenarCategorias(categorias, rvCategorias, true)
                //llenarMenus(menusTittles, menusId)

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val categorias = ArrayList<String>()
                val menusTittles = ArrayList<String>()
                val menusId = ArrayList<String>()
                this@FragmentLocal.idLocal = String()
                this@FragmentLocal.idLocal = p0.key.toString()
                local = p0.getValue(Local::class.java)!!
                atachCampos(local.nombre,local.horaInicio,local.horaCierre)
                for (categoria in local.miembroscategorias) {
                    categorias.add(categoria.key)
                }
                for (menu in local.miembrosMenus) {
                    for (nombre in menu.value) {
                        menusTittles.add(nombre.value)
                        nombresMenus.put(nombre.value, menu.key)
                    }
                    menusId.add(menu.key)
                }

                llenarCategorias(categorias, rvCategorias, true)
                llenarMenus(menusTittles, menusId)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                //ojo revisar el codigo para que se elimine las categorias
            }

        }

        qry.addChildEventListener(listener)
    }

    /*
    * esta funcion se encarga de llenar el rv con el arreglo de categorias que se le pasa
    * */
    fun llenarCategorias(categoriasL: ArrayList<String>, rv: RecyclerView, global: Boolean) {
        //aqui lleno por primera vez mi variable global de categorias
        var categoriasN = ArrayList<String>()
        if (global) {
            for (categoria in categoriasL) {
                categorias.put(categoria, true)
            }
            //lleno el array categoriasN con las keys de mi variable global
            for (categoria in categorias.keys) {
                categoriasN.add(categoria)
            }
        } else {
            categoriasN = categoriasL
        }
        //seteo mi rv y lo lleno
        rv.apply {
            this.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL, false
            )
            this.adapter = CategoriaAdapter(categoriasN)
        }
    }

    fun llenarMenus(menusTittles: ArrayList<String>, menusId: ArrayList<String>) {
        if (menusTittles.size > 0) {
            for (titulo in menusTittles) {
                titulos.put(titulo, true)
            }
            var adapter = MiembrosMenusViewPagerAdapter(this, menusId,idLocal)
            pager.adapter = adapter
            val tabLayoutMediator = TabLayoutMediator(titulosMenu, pager,
                TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                    tab.text = menusTittles[position]
                }
            ).attach()
        }
    }
}
