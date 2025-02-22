package com.example.ecupickers.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ecupickers.fragmentos.ContenidMenu



class MiembrosMenusViewPagerAdapter(
    fm: Fragment,
    idMenus:ArrayList<String>,idlocal:String)
    : FragmentStateAdapter(fm) {
    private var idMenus=idMenus
    private var idLocal=idlocal
    private lateinit var fragment: ContenidMenu
    override fun getItemCount(): Int =idMenus.size

    override fun createFragment(position: Int): Fragment {
        fragment=ContenidMenu()
        val buddle=Bundle()
        buddle.putString("idMenu",idMenus[position])
        buddle.putString("idLocal",idLocal)
        fragment.arguments=buddle
        return fragment
    }


}