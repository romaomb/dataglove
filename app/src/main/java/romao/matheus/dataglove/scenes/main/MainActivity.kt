package romao.matheus.dataglove.scenes.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import romao.matheus.dataglove.R

class MainActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)
        setTabs()
    }

    private fun setTabs() {
        tabsVP.setSwipeable(false)
        tabsVP.adapter = MainAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(tabsVP)

        for (index in 0 until tabLayout.tabCount) {
            when (index) {
                0 -> tabLayout.getTabAt(index)!!.text = getString(R.string.tab_label_simulation)
                1 -> tabLayout.getTabAt(index)!!.text = getString(R.string.tab_label_table)
            }
        }

    }
}
