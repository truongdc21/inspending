package com.truongdc21.inspending.fragment.BottomSheet

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.truongdc21.inspending.R
import com.truongdc21.inspending.databinding.LayoutBottomSheetSelectDateBinding
import com.truongdc21.inspending.model.DateTime
import com.truongdc21.inspending.util.Date
import com.truongdc21.inspending.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class BottomSheetSelectDateFragment : BottomSheetDialogFragment(){

    private lateinit var binding : LayoutBottomSheetSelectDateBinding
    private lateinit var MainVMD : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog =  super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        MainVMD = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding = LayoutBottomSheetSelectDateBinding.inflate(layoutInflater)
        //val viewDialog = LayoutInflater.from(context).inflate(R.layout.layout_bottom_sheet_select_date , null)
        bottomSheetDialog.setContentView(binding.root)

        binding.apply {
            vgrDateDays.setOnClickListener {
                val datetime = DateTime(1,1 , "" , "")
                MainVMD.updateDateTime(datetime)
                Date.mDate = Date.mDateNow
                MainVMD.CheckDateLiveData.value = Date.mDateNow
                bottomSheetDialog.dismiss()
            }
            vgrDateWeeks.setOnClickListener {
                val datetime = DateTime(1,2 , "" , "")
                MainVMD.updateDateTime(datetime)
                Date.mDate = Date.mDateNow
                MainVMD.CheckDateLiveData.value = Date.mDateNow
                bottomSheetDialog.dismiss()

            }
            vgrDateMonths.setOnClickListener {
                val datetime = DateTime(1,3 , "" , "")
                MainVMD.updateDateTime(datetime)
                Date.mDate = Date.mDateNow
                MainVMD.CheckDateLiveData.value = Date.mDateNow
                bottomSheetDialog.dismiss()
            }
            vgrDateYeahs.setOnClickListener {
                val datetime = DateTime(1,4 , "" , "")
                MainVMD.updateDateTime(datetime)
                Date.mDate = Date.mDateNow
                MainVMD.CheckDateLiveData.value = Date.mDateNow
                bottomSheetDialog.dismiss()
            }
            vgrDateSelectDays.setOnClickListener {
                // 5 Select
                MaterialDatePiecker(bottomSheetDialog)
            }
            vgrDateRange.setOnClickListener {
                // 6 Range
                MaterialDateRangePicker(bottomSheetDialog)
            }
            vgrDateAll.setOnClickListener {
                val datetime = DateTime(1,7 , "" , "")
                MainVMD.updateDateTime(datetime)
                bottomSheetDialog.dismiss()
            }
        }

        return bottomSheetDialog
    }

    private fun MaterialDateRangePicker(BottomSheetDialog: BottomSheetDialog){
        val datePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTitleText("Select Date")
            .build()
        datePicker.show(childFragmentManager,"Date_Range_Picker")
        datePicker.addOnPositiveButtonClickListener { DateRange ->
            val firstDate = covertLongtoDate(DateRange.first)
            val endDate = covertLongtoDate(DateRange.second)

            val datetime = DateTime(1,6 , firstDate , endDate)
            MainVMD.updateDateTime(datetime)
            Date.RangeDateFirst = LocalDate.parse(firstDate)
            Date.RangeDateEnd = LocalDate.parse(endDate)
            BottomSheetDialog.dismiss()
        }
    }
    private fun MaterialDatePiecker(BottomSheetDialog : BottomSheetDialog){
        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("Date Select")
            .build()
        datePicker.show(childFragmentManager, "date_picker")
        datePicker.addOnPositiveButtonClickListener {
            val DateMaterial = datePicker.selection!!.toLong()

            val datetime = DateTime(1,5 , "" , "")
            MainVMD.updateDateTime(datetime)
            Date.mDate = LocalDate.parse(covertLongtoDate(DateMaterial))
            MainVMD.CheckDateLiveData.value = Date.mDate
            BottomSheetDialog.dismiss()
        }

    }

    private fun covertLongtoDate(time : Long):String{
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd" , Locale.getDefault())
        return format.format(date)
    }
}