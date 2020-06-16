package com.example.ecupickers.ui.gestionarLocal


import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.ecupickers.R
import com.example.ecupickers.adapters.CategoriaLocalAdapter
import com.example.ecupickers.adapters.MiembrosMenusViewPagerAdapter
import com.example.ecupickers.clases.Locales
import com.example.ecupickers.constantes.EnumCamposDB
import com.example.ecupickers.constantes.EnumCategoria
import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.constantes.EnumTipoLocal
import com.example.ecupickers.dialogs.DialogoCartaAgregarProducto
import com.example.ecupickers.dialogs.DialogoCrearMenu
import com.example.ecupickers.factory.DbReference
import com.example.ecupickers.modelos.Local
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.*
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fondo_local2.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlinx.android.synthetic.main.fondo_local2.*
import kotlinx.android.synthetic.main.fondo_local2.view.txtHoraInicio
import kotlinx.android.synthetic.main.fondo_local2.view.txthoraCierre
import kotlinx.android.synthetic.main.fragment_local.view.*


class FragmentLocal : Fragment() {
    private lateinit var uid: String
    private lateinit var idLocal: String
    private lateinit var ciudad: String
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
    private lateinit var anadirProductoBtn: Button
    private lateinit var nombresMenus: HashMap<String, String>
    private lateinit var toolbar: Toolbar
    private lateinit var editar: MenuItem
    private lateinit var cancelar: MenuItem
    private lateinit var guardar: MenuItem
    private lateinit var btnAgregarMenu: FloatingActionButton
    private lateinit var menu: Menu
    private lateinit var adapterCategorias:CategoriaLocalAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid = it.getString("idUser")!!
            ciudad = it.getString("ciudad")!!
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
            "Espera mientras cargan los datos de tu local.", Toast.LENGTH_SHORT, true
        ).show()
        btnAgregarMenu = root.btnAgregarNuevoMenu
        ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        horaInicio = root.txtHoraInicio
        horaCierre = root.txthoraCierre
        horaInicio.isEnabled = false
        horaCierre.isEnabled = false
        nombrelocal = root.editText4
        nombrelocal.isEnabled = false
        comboCategorias = root.spinnerCategoriasLocal
        comboCategorias.isEnabled=false
        titulosMenu = root.titulosMenuHorizontal
        qryUser = ref.child(EnumReferenciasDB.USERS.rutaDB()).orderByKey().equalTo(uid)
        rvCategorias = root.recyclerView
        categorias = HashMap()
        titulos = HashMap()
        pager = root.contenidoMenuHorizontalLocal
        anadirProductoBtn = root.buttonAgregarNuevoProducto
        nombresMenus = HashMap()
        toolbar = root.toolbarLocal
        menu = toolbar.menu
        return root
    }

    override fun onStart() {
        super.onStart()
        //esta funcion consulta y dentro llama a la funcion que pinta los datos del local
        traerLocal(uid, ciudad)
        //combo categorias
        comboCategorias.let {
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
        mostrarProductoPopWindows(anadirProductoBtn)
        mostrarPopNuevoMenu(btnAgregarMenu)
        toolbar.overflowIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_edit)
        toolbar.setOnMenuItemClickListener(menuToolbar())
    }

    private fun mostrarPopNuevoMenu(btnAccion: FloatingActionButton) {
        val dialog = DialogoCrearMenu(false)
        val bundle = Bundle()
        btnAccion.setOnClickListener {
            bundle.putString("idLocal", idLocal)
            dialog.arguments = bundle
            dialog.show(childFragmentManager, "CrearNuevoMenu")
        }
    }

    private fun mostrarProductoPopWindows(btnAccion: Button) {
        fun getTitulosMenusLocal(): ArrayList<String> {
            val menusTitulos = ArrayList<String>()
            for (titulo in nombresMenus.keys) {
                menusTitulos.add(titulo)
            }
            return menusTitulos
        }

        fun getidsMenusLocal(): ArrayList<String> {
            val idsMenus = ArrayList<String>()
            for (titulo in nombresMenus.values) {
                idsMenus.add(titulo)
            }
            return idsMenus
        }

        val dialog = DialogoCartaAgregarProducto(false)
        val bundle = Bundle()
        btnAccion.setOnClickListener {
            bundle.putStringArrayList("menusTitulos", getTitulosMenusLocal())
            bundle.putStringArrayList("idsMenus", getidsMenusLocal())
            bundle.putString("idLocal", idLocal)
            bundle.putString("nombreLocal", nombrelocal.text.toString())
            dialog.arguments = bundle
            dialog.show(childFragmentManager, "DialogoCartaAgregarProucto")

        }
    }

    private fun menuToolbar(): Toolbar.OnMenuItemClickListener {
        return Toolbar.OnMenuItemClickListener {

            when (it.title.toString()) {
                "Editar" -> {
                    editar = it
                    menu.removeItem(it.itemId)
                    guardar = menu.add(R.string.guardar)
                    cancelar = menu.add(R.string.cancelar)
                    guardar.setIcon(R.drawable.ic_edit)

                    habilitarEdicion(true)
                }
            }
            true
        }


    }

    private fun desplegarTimePicker(editText: EditText) {

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

    private fun habilitarEdicion(habilitar: Boolean) {
        if (habilitar) {
            nombrelocal.requestFocus()
        }
        nombrelocal.isEnabled = habilitar
        horaInicio.isEnabled = habilitar
        horaCierre.isEnabled = habilitar
        comboCategorias.isEnabled=habilitar
        val nombreLocal = nombrelocal.text.toString()
        val horaCierre = horaCierre.text.toString()
        val horaInicio = horaInicio.text.toString()

        if (habilitar) {
            Toasty.info(
                requireContext(),
                "Ahora puedes editar los campos de tu Local.", Toast.LENGTH_SHORT, true
            ).show()
            val objecto = Toolbar.OnMenuItemClickListener {
                when (it.title.toString()) {
                    "Editar" -> {
                        editar = it
                        menu.removeItem(it.itemId)
                        guardar = menu.add(R.string.guardar)
                        cancelar = menu.add(R.string.cancelar)
                        habilitarEdicion(true)
                    }
                    "Cancelar" -> {
                        recuperarDatosAnteriores(nombreLocal, horaCierre, horaInicio)
                        menu.removeItem(cancelar.itemId)
                        menu.removeItem(it.itemId)
                        menu.add(editar.title)
                        Toasty.warning(
                            requireContext(),
                            "Se a cancelado la edicion de datos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    "Guardar" -> {
                        menu.removeItem(cancelar.itemId)
                        menu.removeItem(it.itemId)
                        menu.add(editar.title)
                        guardarDatosLocalDB(
                            nombreLocal,
                            horaCierre,
                            horaInicio
                        )

                    }
                }
                true
            }
            toolbar.setOnMenuItemClickListener(objecto)
        }

    }


    private fun guardarDatosLocalDB(
        nombreLocal: String,
        horaCierreT: String,
        horaInicioT: String
    ) {
        val nuevoNombre = nombrelocal.text.toString()
        val horaCierreNuevo = horaCierre.text.toString()
        val horaInicioNuevo = horaInicio.text.toString()
        val local = Locales()
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
                        requireContext(), "Se a cambio con exito el nombre!",
                        Toast.LENGTH_SHORT, true
                    ).show()
                }
                if (horaCierreT != horaCierreNuevo) {
                    tag = EnumCamposDB.HORACIERRE
                    actualizar(tag, horaCierreNuevo)
                    Toasty.success(
                        requireContext(), "Se a cambio con exito la hora cierre!",
                        Toast.LENGTH_SHORT, true
                    ).show()
                }
                if (horaInicioT != horaInicioNuevo) {
                    tag = EnumCamposDB.HORAINICIO
                    actualizar(tag, horaInicioNuevo)
                    Toasty.success(
                        requireContext(), "Se a cambio con exito la hora Inicio!",
                        Toast.LENGTH_SHORT, true
                    ).show()
                }
                Toasty.success(
                    requireContext(), "Se a cambio con exito los datos!",
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

    private fun recuperarDatosAnteriores(
        nombreLocal: String,
        horaCierre: String,
        horaInicio: String
    ) {
        nombrelocal.setText(nombreLocal)
        txtHoraInicio.setText(horaInicio)
        txthoraCierre.setText(horaCierre)
        habilitarEdicion(habilitar = false)
    }

    private fun agregarCategoria(): AdapterView.OnItemSelectedListener {
        /*
        * esta funcion interna llena un aList de strings, que contendra
        * una categoria y no se podra repetir ya que el hashmap toma como key la categoria
        * */
        fun agregar(categorias: HashMap<String, Boolean>): ArrayList<String> {
            val nCategoria = ArrayList<String>()
            for (categoria in categorias) {
                if (categoria.value) {
                    nCategoria.add(categoria.key)
                }
            }
            return nCategoria
        }

        /*
        * esta funcion añade a la base de datos la categoria,
        * y agrega a la variable global de categorias la categoria que se selecciona en el
        * spinner,ojo estas categorias en la variable glogal CATEGORIAS son la key
        * */
        fun anadirDb(tag: EnumCategoria, local: Locales) {
            val categoriaCategorizada = ArrayList<EnumCategoria>()
            categoriaCategorizada.add(tag)
            //agrego la categoria a la variable global
            categorias[tag.getCategoria()] = true
            //paso la categoria global a la funcion que me retorna un array conlas categorias
            // sin repeticiones
            val nCategoria = agregar(categorias)
            //aqui llamo a la funcion que inserta la categoria a la base de datos
            local.gestionarCategoriaLocal(
                false, categoriaCategorizada, idLocal, ciudad
            )
            llenarCategorias(nCategoria, rvCategorias)
            Toasty.success(requireContext(),"Se añadio la categoria $tag a tu local",
                Toast.LENGTH_SHORT).show()
        }

        return object : AdapterView.OnItemSelectedListener {
            fun anadirCategoriaPocion(position: Int) {
                val local = Locales()
                val categoriaTag: EnumCategoria
                when (position) {
                    1 -> {
                        categoriaTag = EnumCategoria.ALMUERZO
                        anadirDb(categoriaTag, local)
                    }
                    2 -> {
                        categoriaTag = EnumCategoria.DESAYUNOS
                        anadirDb(categoriaTag, local)
                    }
                    3 -> {

                        categoriaTag = EnumCategoria.MERIENDAS
                        anadirDb(categoriaTag, local)
                    }
                    4 -> {
                        categoriaTag = EnumCategoria.POSTRES
                        anadirDb(categoriaTag, local)
                    }
                    5 -> {

                        categoriaTag = EnumCategoria.BBQ
                        anadirDb(categoriaTag, local)
                    }
                    6 -> {
                        categoriaTag = EnumCategoria.COMIDARAPIDA
                        anadirDb(categoriaTag, local)
                    }
                    7 -> {
                        categoriaTag = EnumCategoria.MARISCOS
                        anadirDb(categoriaTag, local)
                    }
                    8 -> {
                        categoriaTag = EnumCategoria.POLLOS
                        anadirDb(categoriaTag, local)
                    }
                    9 -> {
                        categoriaTag = EnumCategoria.HELADOS
                        anadirDb(categoriaTag, local)
                    }
                    10 -> {
                        categoriaTag = EnumCategoria.HAMBURGUESAS
                        anadirDb(categoriaTag, local)
                    }
                    11 -> {
                        categoriaTag = EnumCategoria.PIZZAS
                        anadirDb(categoriaTag, local)
                    }
                }
                comboCategorias.setSelection(0)
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                anadirCategoriaPocion(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }
    //esta funcion me sirve para traer los datos del local con una consulta al nodo usuario


    //esta funcion me consulta el local en el nodo MiembrosVendedores
    private fun traerLocal(idUser: String, ciudad: String) {
        val qry = ref.child(
            EnumReferenciasDB.MIEMBROSVENDEDORES.rutaDB() +
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

    private fun pintarDatosLocal(idLocal: String) {
        fun atachCampos(nombre: String, horaInicioS: String, horaCierreS: String) {
            nombrelocal.setText(nombre)
            horaInicio.setText(horaInicioS)
            horaCierre.setText(horaCierreS)
        }

        val qry =
            ref.child(EnumReferenciasDB.LOCALES.rutaDB()).orderByKey().equalTo(idLocal)
        var local: Local
        val listener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val categorias = ArrayList<String>()
                val menusTittles = ArrayList<String>()
                val menusId = ArrayList<String>()
                this@FragmentLocal.idLocal = String()
                this@FragmentLocal.idLocal = p0.key.toString()
                local = p0.getValue(Local::class.java)!!
                atachCampos(local.nombre, local.horaInicio, local.horaCierre)
                for (categoria in local.miembroscategorias) {
                    if (categoria.value) {
                        categorias.add(categoria.key)
                    }
                }

                for (menu in local.miembrosMenus) {
                    for (nombre in menu.value) {
                        menusTittles.add(nombre.value)
                        nombresMenus[nombre.value] = menu.key
                    }
                    menusId.add(menu.key)
                }

                llenarCategorias(categorias, rvCategorias)
                llenarMenus(menusTittles, menusId)

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val categorias = ArrayList<String>()
                val menusTittles = ArrayList<String>()
                val menusId = ArrayList<String>()
                this@FragmentLocal.idLocal = String()
                this@FragmentLocal.idLocal = p0.key.toString()
                local = p0.getValue(Local::class.java)!!
                atachCampos(local.nombre, local.horaInicio, local.horaCierre)
                for (categoria in local.miembroscategorias) {
                    if (categoria.value) {
                        categorias.add(categoria.key)
                    }
                }
                for (menu in local.miembrosMenus) {
                    for (nombre in menu.value) {
                        menusTittles.add(nombre.value)
                        nombresMenus[nombre.value] = menu.key
                    }
                    menusId.add(menu.key)
                }

                llenarCategorias(categorias, rvCategorias)
                llenarMenus(menusTittles, menusId)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                //ojo revisar el codigo para que se elimine las categorias
                val categorias = ArrayList<String>()
                val menusTittles = ArrayList<String>()
                val menusId = ArrayList<String>()
                this@FragmentLocal.idLocal = String()
                this@FragmentLocal.idLocal = p0.key.toString()
                local = p0.getValue(Local::class.java)!!
                atachCampos(local.nombre, local.horaInicio, local.horaCierre)
                for (categoria in local.miembroscategorias) {
                    if (categoria.value) {
                        categorias.add(categoria.key)
                    }
                }
                for (menu in local.miembrosMenus) {
                    for (nombre in menu.value) {
                        menusTittles.add(nombre.value)
                        nombresMenus[nombre.value] = menu.key
                    }
                    menusId.add(menu.key)
                }

                llenarCategorias(categorias, rvCategorias)
            }

        }

        qry.addChildEventListener(listener)
    }

    /*
    * esta funcion se encarga de llenar el rv con el arreglo de categorias que se le pasa
    * */
    private fun llenarCategorias(
        categoriasL: ArrayList<String>,
        rv: RecyclerView
    ) {
        //aqui lleno por primera vez mi variable global de categorias
        var categoriasN = ArrayList<String>()
        val categoriasNuevaGlobal = HashMap<String, Boolean>()
        if (isAdded) {
            if (categoriasL.size > 0) {
                for (categoria in categoriasL) {
                    categoriasNuevaGlobal[categoria] = true
                }
                categorias = categoriasNuevaGlobal
                //lleno el array categoriasN con las keys de mi variable global
                for (categoria in categorias.keys) {
                    categoriasN.add(categoria)
                }
                //seteo mi rv y lo lleno
                rv.apply {
                    this.layoutManager = GridLayoutManager(context, 3)
                    val ids = HashMap<String, String>()
                    ids["ciudad"] = ciudad
                    ids["idLocal"] = idLocal
                    val adapter=  CategoriaLocalAdapter(
                        categoriasN,
                        ids
                    )
                    this.adapter =adapter

                    adapterCategorias=adapter
                }

            }
        }
    }

    private fun llenarMenus(menusTittles: ArrayList<String>, menusId: ArrayList<String>) {
        if (isAdded) {
            if (menusTittles.size > 0) {
                for (titulo in menusTittles) {
                    titulos[titulo] = true
                }
                //este adaptador maneja el adaptador de la pag
                val adapter = MiembrosMenusViewPagerAdapter(this, menusId, idLocal)
                pager.adapter = adapter

                TabLayoutMediator(titulosMenu, pager,
                    TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                        tab.text = menusTittles[position]
                        tab.setIcon(R.drawable.ic_restaurant_menu)
                    }
                ).attach()
            }
        }
    }
}
