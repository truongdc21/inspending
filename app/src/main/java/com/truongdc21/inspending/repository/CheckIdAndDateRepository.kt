package com.truongdc21.inspending.repository

import androidx.lifecycle.MutableLiveData
import java.time.LocalDate

class CheckIdAndDateRepository {
    val CheckDateLive  = MutableLiveData<LocalDate>()
    var mDate = LocalDate.now()
}