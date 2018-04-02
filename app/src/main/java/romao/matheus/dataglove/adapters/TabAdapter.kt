package romao.matheus.dataglove.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import romao.matheus.dataglove.fragments.SimulationFragment
import romao.matheus.dataglove.fragments.TableFragment

class TabAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
    private val fragmentList = ArrayList<Fragment>()

    init {
        fragmentList.add(SimulationFragment())
        fragmentList.add(TableFragment())
    }

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size
}