package com.truongdc21.inspending.fragment.categories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.truongdc21.inspending.R
import com.truongdc21.inspending.adapter.AdapterVPGCategories
import com.truongdc21.inspending.databinding.FragmentCategoriesBinding
import com.truongdc21.inspending.databinding.FragmentCategoriesVPGHomeFragementBinding
import com.truongdc21.inspending.model.DateTime
import com.truongdc21.inspending.util.Date
import com.truongdc21.inspending.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesVPGHomeFragement : Fragment() {
    private lateinit var MainVMD : MainViewModel
    private lateinit var binding: FragmentCategoriesVPGHomeFragementBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
       binding = FragmentCategoriesVPGHomeFragementBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        MainVMD = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            MainVMD.getDateTime.observe(viewLifecycleOwner, Observer {
                for (i in it){
                    viewPageCategories.isUserInputEnabled = i.TypeDateTime != 7
                    // viewpage sẽ ko stroll nếu i.typeDatetime = 7
                }
            })
            viewPageCategories.apply {
                adapter = AdapterVPGCategories(childFragmentManager , lifecycle)
                    setCurrentItem(5000, false)
                    var a : Int = 0
                    a = 5000
                    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            lifecycleScope.launch {
                                var mListDateTime : List<DateTime>? = null
                                MainVMD.getDateTime.observe(viewLifecycleOwner, Observer { mDateTime ->
                                    mListDateTime = mDateTime
                                })
                                if (position < a) {
                                    a = position
                                    MainVMD.PreviousDate(mListDateTime!! )
                                }else if (position > a) {
                                    a = position
                                    MainVMD.NextDate(mListDateTime!! )
                                }

                            }
                        }
                    })
            }
        }

    }



}