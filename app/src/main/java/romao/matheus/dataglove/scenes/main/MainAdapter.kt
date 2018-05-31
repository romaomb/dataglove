package romao.matheus.dataglove.scenes.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import romao.matheus.dataglove.scenes.simulation.SimulationFragment
import romao.matheus.dataglove.scenes.table.TableFragment

class MainAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
    private val fragmentList = ArrayList<Fragment>()

    init {
        fragmentList.add(SimulationFragment())
        fragmentList.add(TableFragment())
    }

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size
}