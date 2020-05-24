package com.example.ecupickers.ui.gestionarLocal


import android.annotation.SuppressLint

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.recyclerview.widget.LinearLayoutManager


import kotlinx.android.synthetic.main.fondo_local.view.*


import androidx.recyclerview.widget.RecyclerView

import androidx.viewpager2.widget.ViewPager2


import com.example.ecupickers.R
import com.example.ecupickers.adapters.CategoriaAdapter
import com.example.ecupickers.adapters.MiembrosMenusViewPagerAdapter
import com.example.ecupickers.clases.Locales
import com.example.ecupickers.constantes.EnumCamposDB
import com.example.ecupickers.constantes.EnumCategoria
import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.constantes.EnumTipoLocal
import com.example.ecupickers.factory.DbReference
import com.example.ecupickers.modelos.Local
import com.example.ecupickers.modelos.User
import com.example.ecupickers.ui.inicio.FragmentInicio
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.*
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fondo_local.*
import kotlinx.android.synthetic.main.fondo_local.view.txtHoraInicio


class FragmentLocal : Fragment() {
    private lateinit var uid: String
    private lateinit var idLocal: String
    private lateinit var ciudad: String
    private lateinit var editarBtn: Button
    private lateinit var cancelarBtn: Button
    private lateinit var guardarBtn: Button
    private lateinit var nombrelocal: EditText
    private lateinit var qryUser: Query
    private lateinit var listenerUser: ChildEventListener
    private lateinit var ref: DatabaseReference
    private lateinit var rvCategorias: RecyclerView
    private lateinit var categorias: HashMap<String, Boolean>
    private lateinit var titulos: HashMap<String, Boolean>
    private lateinit var comboCategorias: Spinner
    private lateinit var titulosMenu: TabLayout
    private lateinit var horaInicio:EditText
    private lateinit var horaCierre:EditText
    private lateinit var pager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid = it.getString("idUser")!!
        }
    }


    @SuppressLint("ResourceAsColor")
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
        editarBtn = root.buttonEditarLocal
        cancelarBtn = root.buttonCancelarCambiosLocal
        guardarBtn = root.buttonGuardarCambiosLocal
        horaInicio=root.txtHoraInicio
        horaCierre=root.txthoraCierre
        guardarBtn.isEnabled = false
        cancelarBtn.isEnabled = false
        nombrelocal = root.findViewById(R.id.editText4)
        comboCategorias = root.spinnerCategoriasLocal
        titulosMenu = root.titulosMenuHorizontal
        qryUser = ref.child(EnumReferenciasDB.USERS.rutaDB()).orderByKey().equalTo(uid)
        rvCategorias = root.recyclerView
        categorias = HashMap()
        titulos = HashMap()
        pager = root.contenidoMenuHorizontalLocal
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        comboCategorias?.let {
            val adaptador: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
                view.context,
                R.array.opciones_categorias, android.R.layout.simple_spinner_item
            )
            it.adapter = adaptador
        }

    }

    override fun onStart() {
        super.onStart()
        listenerUser = traerUser()
        qryUser.addChildEventListener(listenerUser)
        comboCategorias.onItemSelectedListener = agregarCategoria()
        editarBtn.setOnClickListener { habilitarEdicion(true) }

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
        horaCierre: String ,
        horaInicio: String
    ) {
        var nuevoNombre = nombrelocal.text.toString()
        var local = Locales()
        if (nombrelocal.text.isBlank()) {
            Toasty.error(
                requireContext(),
                "El nombre de tu local no puede estar vacio",
                Toast.LENGTH_SHORT, true
            ).show()
            nombrelocal.requestFocus()
        } else {
            if (nombreLocal != nuevoNombre) {
                Toasty.success(
                    requireContext(), "${idLocal}!${nuevoNombre}!${ciudad}",
                    Toast.LENGTH_SHORT, true
                ).show()
                var completado = local.gestionarCampo(
                    nuevoNombre, idLocal, EnumTipoLocal.RESTAURANTE,
                    ciudad, EnumCamposDB.NOMBRE
                )
                if (completado) {
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

            } else {
                Toasty.error(
                    requireContext(),
                    "Los datos no han sido modificados", Toast.LENGTH_SHORT, true
                ).show()
            }
        }

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
        fun agregar(categorias: HashMap<String, Boolean>): ArrayList<String> {
            var nCategoria = ArrayList<String>()
            for (categoria in categorias) {
                nCategoria.add(categoria.key)
            }
            return nCategoria
        }

        fun añadirDb(tag: EnumCategoria, local: Locales) {
            var categoriaCategorizada = ArrayList<EnumCategoria>()
            var categoriaTag = tag
            categoriaCategorizada.add(categoriaTag)
            categorias.put("${categoriaTag.getCategoria()}", true)
            var nCategoria = agregar(categorias)
            local.gestionarCategoriaLocal(
                false, categoriaCategorizada, idLocal, ciudad
            )
            llenarCategorias(nCategoria)
        }

        var listener = object : AdapterView.OnItemSelectedListener {


            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

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

        }
        return listener
    }

    fun traerUser(): ChildEventListener {
        var user = User()
        val listener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                user = p0.getValue(User::class.java)!!

                traerLocal(p0.key.toString(), user.ciudad)

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("Not yet implemented")
            }
        }
        return listener

    }

    fun traerLocal(idUser: String, ciudad: String) {
        this@FragmentLocal.ciudad = String()
        this@FragmentLocal.ciudad = ciudad
        val qry = ref.child(
            "${EnumReferenciasDB.MIEMBROSVENDEDORES.rutaDB()}" +
                    "/${ciudad}/${idUser}"
        )
        val listener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                pintarDatosLocal(p0.key.toString())
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
        val qry =
            ref.child("${EnumReferenciasDB.LOCALES.rutaDB()}").orderByKey().equalTo(idLocal)
        var local = Local()
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
                local = p0.getValue(Local::class.java)!!
                this@FragmentLocal.idLocal = String()
                this@FragmentLocal.idLocal = p0.key.toString()
                for (categoria in local.miembrosCategorias) {
                    categorias.add(categoria.key)
                }

                for (menu in local.miembrosMenus) {
                    for (nombre in menu.value) {
                        menusTittles.add(nombre.value)
                    }
                    menusId.add(menu.key)
                }
                llenarMenus(menusTittles, menusId)
                llenarCategorias(categorias)
                nombrelocal.setText(local.nombre)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val categorias = ArrayList<String>()
                val menusTittles = ArrayList<String>()
                val menusId = ArrayList<String>()
                local = p0.getValue(Local::class.java)!!
                this@FragmentLocal.idLocal = String()
                this@FragmentLocal.idLocal = p0.key.toString()
                for (categoria in local.miembrosCategorias) {
                    categorias.add(categoria.key)
                }

                for (menu in local.miembrosMenus) {
                    for (nombre in menu.value) {
                        menusTittles.add(nombre.value)
                    }
                    menusId.add(menu.key)
                }
                llenarMenus(menusTittles, menusId)
                llenarCategorias(categorias)
                nombrelocal.setText(local.nombre)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                //ojo revisar el codigo para que se elimine las categorias
            }

        }
        qry.addChildEventListener(listener)
    }

    fun llenarCategorias(categoriasL: ArrayList<String>) {
        for (categoria in categoriasL) {
            categorias.put(categoria, true)
        }
        var categoriasN = ArrayList<String>()
        for (categoria in categorias.keys) {
            categoriasN.add(categoria)
        }
        rvCategorias.apply {
            this.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL, false
            )
            this.adapter = CategoriaAdapter(categoriasN)
        }
    }

    fun llenarMenus(menusTittles: ArrayList<String>, menusId: ArrayList<String>) {
        for (titulo in menusTittles) {
            titulos.put(titulo, true)
        }
        var adapter = MiembrosMenusViewPagerAdapter(this, menusId)
        pager.adapter = adapter
        val tabLayoutMediator = TabLayoutMediator(titulosMenu, pager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = menusTittles[position]
            }
        ).attach()
    }
}
