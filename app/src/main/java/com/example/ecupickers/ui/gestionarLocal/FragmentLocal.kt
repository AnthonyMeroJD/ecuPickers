package com.example.ecupickers.ui.gestionarLocal


import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.ecupickers.R
import com.example.ecupickers.adapters.CategoriaAdapter
import com.example.ecupickers.adapters.MiembrosMenusViewPagerAdapter
import com.example.ecupickers.clases.Locales
import com.example.ecupickers.constantes.EnumCategoria
import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.factory.DbReference
import com.example.ecupickers.modelos.Local
import com.example.ecupickers.modelos.User
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fondo_local.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class FragmentLocal : Fragment() {
    private lateinit var uid: String
    private lateinit var idLocal: String
    private lateinit var ciudad: String
    private lateinit var nombrelocal: EditText
    private lateinit var qryUser: Query
    private lateinit var listenerUser: ChildEventListener
    private lateinit var ref: DatabaseReference
    private lateinit var rvCategorias: RecyclerView
    private lateinit var categorias: HashMap<String, Boolean>
    private lateinit var titulos: HashMap<String, Boolean>
    private lateinit var comboCategorias: Spinner
    private lateinit var titulosMenu: TabLayout
    private lateinit var pager:ViewPager2
    private lateinit var horaInicio:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid = it.getString("idUser")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_local, container, false)
        ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        nombrelocal = root.findViewById(R.id.editText4)
        comboCategorias = root.spinnerCategoriasLocal
        titulosMenu = root.titulosMenuHorizontal
        qryUser = ref.child(EnumReferenciasDB.USERS.rutaDB()).orderByKey().equalTo(uid)
        rvCategorias = root.recyclerView
        categorias = HashMap()
        titulos = HashMap()
        pager =root.contenidoMenuHorizontalLocal
        horaInicio = root.txtHoraInicio

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
        //no abrir teclado del editText
        horaInicio.setRawInputType(InputType.TYPE_NULL)
        //abrir TimePicker al darle el primer click
        horaInicio.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val cal = Calendar.getInstance()
                val obtenerHora = TimePickerDialog.OnTimeSetListener{ timePicker, hora, minuto->
                    cal.set(Calendar.HOUR_OF_DAY,hora)
                    cal.set(Calendar.MINUTE,minuto)
                    horaInicio.setText( SimpleDateFormat("h:m a").format(cal.time))
                }
                TimePickerDialog(context, obtenerHora, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            } else {
                // Hide your calender here
            }
        }
        //abrir TimePicker al darle click nuevamente
        horaInicio.setOnClickListener {
            val cal = Calendar.getInstance()
            val obtenerHora = TimePickerDialog.OnTimeSetListener{timePicker, hora, minuto->
                cal.set(Calendar.HOUR_OF_DAY,hora)
                cal.set(Calendar.MINUTE,minuto)
                horaInicio.setText( SimpleDateFormat("h:m a").format(cal.time))
            }
            TimePickerDialog(context, obtenerHora, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }



        comboCategorias.onItemSelectedListener = agregarCategoria()
    }

    fun agregarCategoria(): AdapterView.OnItemSelectedListener {

        var listener = object : AdapterView.OnItemSelectedListener {
            fun agregar(categorias: HashMap<String, Boolean>): ArrayList<String> {
                var nCategoria = ArrayList<String>()
                for (categoria in categorias) {
                    nCategoria.add(categoria.key)
                }
                return nCategoria
            }

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
                var categoriaCategorizada = ArrayList<EnumCategoria>()
                var categoriaTag: EnumCategoria
                when (position) {
                    1 -> {
                        categoriaTag = EnumCategoria.ALMUERZO
                        categoriaCategorizada.add(categoriaTag)
                        categorias.put("${categoriaTag.getCategoria()}", true)
                        var nCategoria = agregar(categorias)
                        local.gestionarCategoriaLocal(
                            false, categoriaCategorizada, idLocal, ciudad
                        )
                        llenarCategorias(nCategoria)
                    }
                    2 -> {
                        categoriaTag = EnumCategoria.DESAYUNOS
                        categoriaCategorizada.add(categoriaTag)
                        categorias.put("${categoriaTag.getCategoria()}", true)
                        var nCategoria = agregar(categorias)
                        local.gestionarCategoriaLocal(
                            false, categoriaCategorizada, idLocal, ciudad
                        )
                        llenarCategorias(nCategoria)
                    }
                    3 -> {
                        categoriaTag = EnumCategoria.MERIENDAS
                        categoriaCategorizada.add(categoriaTag)
                        categorias.put("${categoriaTag.getCategoria()}", true)
                        var nCategoria = agregar(categorias)
                        local.gestionarCategoriaLocal(
                            false, categoriaCategorizada, idLocal, ciudad
                        )
                        llenarCategorias(nCategoria)
                    }
                    4 -> {
                        categoriaTag = EnumCategoria.POSTRES
                        categoriaCategorizada.add(categoriaTag)
                        categorias.put("${categoriaTag.getCategoria()}", true)
                        var nCategoria = agregar(categorias)
                        local.gestionarCategoriaLocal(
                            false, categoriaCategorizada, idLocal, ciudad
                        )
                        llenarCategorias(nCategoria)
                    }
                    5 -> {
                        categoriaTag = EnumCategoria.BBQ
                        categoriaCategorizada.add(categoriaTag)
                        categorias.put("${categoriaTag.getCategoria()}", true)
                        var nCategoria = agregar(categorias)
                        local.gestionarCategoriaLocal(
                            false, categoriaCategorizada, idLocal, ciudad
                        )
                        llenarCategorias(nCategoria)
                    }
                    6 -> {
                        categoriaTag = EnumCategoria.COMIDARAPIDA
                        categoriaCategorizada.add(categoriaTag)
                        categorias.put("${categoriaTag.getCategoria()}", true)
                        var nCategoria = agregar(categorias)
                        local.gestionarCategoriaLocal(
                            false, categoriaCategorizada, idLocal, ciudad
                        )
                        llenarCategorias(nCategoria)
                    }
                    7 -> {
                        categoriaTag = EnumCategoria.MARISCOS
                        categoriaCategorizada.add(categoriaTag)
                        categorias.put("${categoriaTag.getCategoria()}", true)
                        var nCategoria = agregar(categorias)
                        local.gestionarCategoriaLocal(
                            false, categoriaCategorizada, idLocal, ciudad
                        )
                        llenarCategorias(nCategoria)
                    }
                    8 -> {
                        categoriaTag = EnumCategoria.POLLOS
                        categoriaCategorizada.add(categoriaTag)
                        categorias.put("${categoriaTag.getCategoria()}", true)
                        var nCategoria = agregar(categorias)
                        local.gestionarCategoriaLocal(
                            false, categoriaCategorizada, idLocal, ciudad
                        )
                        llenarCategorias(nCategoria)
                    }
                    9 -> {
                        categoriaTag = EnumCategoria.HELADOS
                        categoriaCategorizada.add(categoriaTag)
                        categorias.put("${categoriaTag.getCategoria()}", true)
                        var nCategoria = agregar(categorias)
                        local.gestionarCategoriaLocal(
                            false, categoriaCategorizada, idLocal, ciudad
                        )
                        llenarCategorias(nCategoria)
                    }
                    10 -> {
                        categoriaTag = EnumCategoria.HAMBURGUESAS
                        categoriaCategorizada.add(categoriaTag)
                        categorias.put("${categoriaTag.getCategoria()}", true)
                        var nCategoria = agregar(categorias)
                        local.gestionarCategoriaLocal(
                            false, categoriaCategorizada, idLocal, ciudad
                        )
                        llenarCategorias(nCategoria)
                    }
                    11 -> {
                        categoriaTag = EnumCategoria.PIZZAS
                        categoriaCategorizada.add(categoriaTag)
                        categorias.put("${categoriaTag.getCategoria()}", true)
                        var nCategoria = agregar(categorias)
                        local.gestionarCategoriaLocal(
                            false, categoriaCategorizada, idLocal, ciudad
                        )
                        llenarCategorias(nCategoria)
                    }
                }
                Toast.makeText(view?.context, "${position}/${id}", Toast.LENGTH_LONG).show()
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
                val menusId=ArrayList<String>()
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
                llenarMenus(menusTittles,menusId)
                llenarCategorias(categorias)
                nombrelocal.setText(local.nombre)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val categorias = ArrayList<String>()
                val menusTittles = ArrayList<String>()
                val menusId=ArrayList<String>()
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
                llenarMenus(menusTittles,menusId)
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

    fun llenarMenus(menusTittles: ArrayList<String>,menusId: ArrayList<String>) {
        for (titulo in menusTittles) {
         titulos.put(titulo,true)
        }
        var adapter=MiembrosMenusViewPagerAdapter(this,menusId)
        pager.adapter=adapter
        val tabLayoutMediator = TabLayoutMediator(titulosMenu, pager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text=menusTittles[position]
            }
        ).attach()
    }
}
