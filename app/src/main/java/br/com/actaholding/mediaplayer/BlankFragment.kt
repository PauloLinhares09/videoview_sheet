package br.com.actaholding.mediaplayer


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import br.com.actaholding.mediaplayer.R
import kotlinx.android.synthetic.main.fragment_blank.view.*


class BlankFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mView = inflater.inflate(R.layout.fragment_blank, container, false)
        (activity as MainActivity).setSupportActionBar(mView.toolbar)
        return  mView
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_scrolling, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }





    companion object {
        @JvmStatic
        fun newInstance() =
            BlankFragment()
    }
}
