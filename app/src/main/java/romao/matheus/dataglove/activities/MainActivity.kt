package romao.matheus.dataglove.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import romao.matheus.dataglove.R
import romao.matheus.dataglove.adapters.TabAdapter
import romao.matheus.dataglove.fragments.SimulationFragment

class MainActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)
        setTabs()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ativado -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    if (!SimulationFragment.keepTransmission) {
                        SimulationFragment.keepTransmission = true
                        val fragment = supportFragmentManager.fragments[0] as SimulationFragment
                        fragment.startRetrofitCall()
                    }
                }
                return true
            }
            R.id.pausado -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    SimulationFragment.keepTransmission = false
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setTabs() {
        tabsVP.setSwipeable(false)
        tabsVP.adapter = TabAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(tabsVP)

        for (index in 0 until tabLayout.tabCount) {
            when (index) {
                0 -> tabLayout.getTabAt(index)!!.text = getString(R.string.tab_label_simulation)
                1 -> tabLayout.getTabAt(index)!!.text = getString(R.string.tab_label_table)
            }
        }

    }
}
