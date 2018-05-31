package romao.matheus.dataglove.scenes.table

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

        tableList.add(mcfLittleTXT)
        tableList.add(ifpLittleTXT)
        tableList.add(ifdLittleTXT)

        tableList.add(mcfRingTXT)
        tableList.add(ifpRingTXT)
        tableList.add(ifdRingTXT)

        tableList.add(mcfMiddleTXT)
        tableList.add(ifpMiddleTXT)
        tableList.add(ifdMiddleTXT)

        tableList.add(mcfIndexTXT)
        tableList.add(ifpIndexTXT)
        tableList.add(ifdIndexTXT)

        tableList.add(cmcThumbTXT)
        tableList.add(mcfThumbTXT)
        tableList.add(ifThumbTXT)
    }
}