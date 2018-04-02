package romao.matheus.dataglove.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_table.*
import romao.matheus.dataglove.R

class TableFragment : Fragment() {

    companion object {
        var tableList = ArrayList<TextView>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_table, container, false) as LinearLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tableList.add(textView15)
        tableList.add(textView14)
        tableList.add(textView13)
        tableList.add(textView12)
        tableList.add(textView11)
        tableList.add(textView10)
        tableList.add(textView9)
        tableList.add(textView8)
        tableList.add(textView7)
        tableList.add(textView6)
        tableList.add(textView5)
        tableList.add(textView4)
        tableList.add(textView3)
        tableList.add(textView2)
        tableList.add(textView1)
    }
}