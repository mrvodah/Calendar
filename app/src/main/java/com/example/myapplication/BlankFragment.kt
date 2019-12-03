package com.example.myapplication

import android.app.DatePickerDialog
import android.graphics.RectF
import android.os.Bundle
import android.os.Handler
import android.text.format.DateUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.alamkanak.weekview.*
import com.example.myapplication.dialog.DialogUtils
import com.example.myapplication.network.NetworkModule
import com.example.myapplication.network.models.LiveStreamResponse
import com.example.myapplication.network.models.Schedule
import com.example.myapplication.network.models.ScheduleRequest
import com.example.myapplication.network.models.ScheduleResponse
import kotlinx.android.synthetic.main.fragment_blank.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class BlankFragment : Fragment(), WeekView.EventClickListener, MonthLoader.MonthChangeListener,
        WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener, WeekView.EmptyViewClickListener,
        WeekView.AddEventClickListener, WeekView.ScrollListener {

    override fun onEventClick(event: WeekViewEvent, eventRect: RectF) {
        editEvents.add(event)
        events.remove(event)
        weekView.notifyDataSetChanged()
    }

    override fun onMonthChange(newYear: Int, newMonth: Int): MutableList<out WeekViewEvent>? {
        if (!calledNetwork) {
            rqGetSchedules("vn_69ff9abb-e0e4-4aac-97b7-4e2449212d2f", "viet@edulive.net")
            calledNetwork = true
        }

        // Return only the events that matches newYear and newMonth.
        val matchedEvents = ArrayList<WeekViewEvent>()
        for (event in events)
            if (eventMatches(event, newYear, newMonth))
                matchedEvents.add(event)
        return matchedEvents
    }

    override fun onEventLongPress(event: WeekViewEvent, eventRect: RectF) {
        
    }

    override fun onEmptyViewClicked(date: Calendar) {
        var startTime = date
        startTime.set(Calendar.MINUTE, 0)
        var endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR, 1)
        var event = WeekViewEvent(UUID.randomUUID().toString(), getEventTitle(startTime, endTime), startTime, endTime)
        event.color = ResourcesCompat.getColor(resources, R.color.event_color_01, null)
        editEvents.add(event)
        events.add(event)
        weekView.notifyDataSetChanged()
    }

    override fun onEmptyViewLongPress(date: Calendar) {
        var startTime = date
        startTime.set(Calendar.MINUTE, 0)

        val start1 = DateUtils.formatDateTime(context, startTime.timeInMillis, DateUtils.FORMAT_SHOW_TIME)
        val end1 = DateUtils.formatDateTime(context, startTime.timeInMillis + 1800000, DateUtils.FORMAT_SHOW_TIME)

        val start2 = DateUtils.formatDateTime(context, startTime.timeInMillis + 1800000, DateUtils.FORMAT_SHOW_TIME)
        val end2 = DateUtils.formatDateTime(context, startTime.timeInMillis + 3600000, DateUtils.FORMAT_SHOW_TIME)
        DialogUtils.showDialog(context, start1 + " - " + end1, start2 + " - " + end2) {
            when(it){
                0 -> {
                    addItemLongClick(startTime, true)
                }
                1 -> {
                    addItemLongClick(startTime, false)
                }
            }
        }
    }

    override fun onFirstVisibleDayChanged(newFirstVisibleDay: Calendar, oldFirstVisibleDay: Calendar?) {
        nowCalendar = newFirstVisibleDay
    }

    override fun onAddEventClicked(startTime: Calendar, endTime: Calendar) {
    }

    private val events = ArrayList<WeekViewEvent>()
    private var nowCalendar = Calendar.getInstance()

    private var calledNetwork = false

    private val WEEK_TO_MILISECOND = 604800000L

    val editEvents = mutableListOf<WeekViewEvent>()

    private lateinit var shortDateFormat: DateFormat
    private lateinit var timeFormat: DateFormat

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shortDateFormat = WeekViewUtil.getWeekdayWithNumericDayAndMonthFormat(context!!, true)
        timeFormat = android.text.format.DateFormat.getTimeFormat(context) ?: SimpleDateFormat("HH:mm", Locale.getDefault())

        weekView.scrollListener = this

        // Get a reference for the week view in the layout.

        // Show a toast message about the touched event.
        weekView.eventClickListener = this

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        weekView.monthChangeListener = this

        // Set long press listener for events.
        weekView.eventLongPressListener = this

        // Set long press listener for empty view
        weekView.emptyViewLongPressListener = this

        // Set EmptyView Click Listener
        weekView.emptyViewClickListener = this

        // Set AddEvent Click Listener
        weekView.addEventClickListener = this

        setupDateTimeInterpreter(false)

        setupButtonClick()

    }

    private fun setupButtonClick() {
        btn_next_week.setOnClickListener {
            val startTime = nowCalendar
            startTime.set(Calendar.HOUR_OF_DAY, 0) // ! clear would not reset the hour of day !
            startTime.clear(Calendar.MINUTE)
            startTime.clear(Calendar.SECOND)
            startTime.clear(Calendar.MILLISECOND)

            startTime.set(Calendar.DAY_OF_WEEK, startTime.firstDayOfWeek)

            var endTime = startTime.clone() as Calendar
            endTime.add(Calendar.WEEK_OF_YEAR, 1)

            val eventsInWeek = getEventsInWeek(startTime, endTime)
            clearEvent(startTime, endTime)
            cloneNextWeek(eventsInWeek)
        }

        btn_to_date.setOnClickListener {
            val year = nowCalendar.get(Calendar.YEAR)
            val month = nowCalendar.get(Calendar.MONTH)
            val day = nowCalendar.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                cloneToDate(calendar)
            }, year, month, day)

            dpd.show()
        }

        btn_send.setOnClickListener {
            val clone = mutableListOf<WeekViewEvent>()
            clone.addAll(events)

            var sortedList = clone.sortedWith(compareBy { it.startTime.timeInMillis }).toMutableList()

            var listEvents = mutableListOf<Schedule>()
            sortedList.forEach {

                listEvents.add(Schedule(it.startTime.timeInMillis / 1000, it.endTime.timeInMillis / 1000))
            }

            var index = 0
            val listEditEvents = mutableListOf<Schedule>()
            editEvents.forEach {
                val edits = getEventsAtDate(it.startTime)
                if(edits.isEmpty()){
                    listEditEvents.add(Schedule(0, 0))
                }
                else{
                    edits.forEach {
                        if(!listEditEvents.contains(Schedule(it.startTime.timeInMillis / 1000, it.endTime.timeInMillis / 1000))){
                            listEditEvents.add(Schedule(it.startTime.timeInMillis / 1000, it.endTime.timeInMillis / 1000))
                        }
                    }
                }
            }

            if(listEditEvents.isEmpty()){
                if(listEvents.isNotEmpty()){

                    val startDate = listEvents[0].startTime
                    val endDate = listEvents[listEvents.size - 1].endTime

                    while (index != listEvents.size - 1){

                        var value1 = listEvents.get(index)
                        var value2 = listEvents.get(index + 1)

                        if(value1.endTime == value2.startTime){
                            listEvents.get(index).endTime = value2.endTime
                            listEvents.removeAt(index + 1)
                        }
                        else{
                            index++
                        }
                    }

                    postSchedules(55, listEvents, startDate, endDate, listEditEvents)

                }
            }
            else{
                var startDate = 0L
                var endDate = 0L

                var sortedEditList = editEvents.sortedWith(compareBy { it.startTime.timeInMillis }).toMutableList()

                if(listEvents.isEmpty()){
                    startDate = sortedEditList[0].startTime.timeInMillis
                    endDate = sortedEditList[sortedEditList.size - 1].endTime.timeInMillis
                }
                else{
                    startDate = listEvents[0].startTime
                    endDate = listEvents[listEvents.size - 1].endTime
                }

                postSchedules(55, listEvents, startDate, endDate, listEditEvents)
            }

            editEvents.clear()
        }

        var isDoubleTap = false
        var count = 0
        btn_next_date.setOnClickListener {

            if(isDoubleTap){
                weekView.goToDate(Calendar.getInstance())
            }

            count++
            isDoubleTap = true

            Handler().postDelayed(Runnable {
                if(count == 1){
                    val year = nowCalendar.get(Calendar.YEAR)
                    val month = nowCalendar.get(Calendar.MONTH)
                    val day = nowCalendar.get(Calendar.DAY_OF_MONTH)

                    val dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, month)
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                        weekView.goToDate(calendar)
                    }, year, month, day)

                    dpd.show()
                }

                count = 0
                isDoubleTap = false
            }, 500)

        }
    }

    private fun getEventsAtDate(startTime: Calendar): MutableList<WeekViewEvent> {
        val dateEvents = mutableListOf<WeekViewEvent>()

        events.forEach {
            if(isTheSameDate(it.startTime, startTime)){
                dateEvents.add(it)
            }
        }

        return dateEvents
    }

    private fun isTheSameDate(normal: Calendar, compare: Calendar): Boolean {
        return normal.get(Calendar.DAY_OF_YEAR) == compare.get(Calendar.DAY_OF_YEAR) &&
                normal.get(Calendar.YEAR) == compare.get(Calendar.YEAR)
    }

    private fun postSchedules(teacherId: Int, listEvents: MutableList<Schedule>, startDate: Long?, endDate: Long?, listEditEvents: MutableList<Schedule>) {
        val request = ScheduleRequest(teacherId.toString(), listEditEvents, listEvents, startDate, endDate)
        Log.d("Request", request.toString())
        NetworkModule.getService().postSchedules(request)
                .enqueue(object : Callback<LiveStreamResponse> {
                    override fun onFailure(call: Call<LiveStreamResponse>, t: Throwable) {
                        t.printStackTrace()
                    }

                    override fun onResponse(call: Call<LiveStreamResponse>, response: Response<LiveStreamResponse>) {
                        Log.d("Response", response.body().toString())
                        if(response.body()!!.status == 200){
                            Log.d("TAG", "Đăng ký lịch thành công!")
                        }
                    }

                })
    }

    private fun cloneToDate(calendar: Calendar) {
        clearEventToDate(calendar)

        val startTime = nowCalendar
        startTime.set(Calendar.HOUR_OF_DAY, 0) // ! clear would not reset the hour of day !
        startTime.clear(Calendar.MINUTE)
        startTime.clear(Calendar.SECOND)
        startTime.clear(Calendar.MILLISECOND)

        startTime.set(Calendar.DAY_OF_WEEK, startTime.firstDayOfWeek)

        var endTime = startTime.clone() as Calendar
        endTime.add(Calendar.WEEK_OF_YEAR, 1)

        val eventsInWeek = getEventsInWeek(startTime, endTime)

        eventsInWeek.forEach {
            var y = 1L
            while(it.startTime.timeInMillis + y * WEEK_TO_MILISECOND <= calendar.timeInMillis){

                Log.d("TAG", "y = $y - startTime: " + (it.startTime.timeInMillis + y * WEEK_TO_MILISECOND) + " calendarTime: ${calendar.timeInMillis}")
                var startTime = Calendar.getInstance()
                var endTime = Calendar.getInstance()
                startTime.timeInMillis = it.startTime.timeInMillis + y * WEEK_TO_MILISECOND
                endTime.timeInMillis = it.endTime.timeInMillis + y * WEEK_TO_MILISECOND
                events.add(WeekViewEvent(UUID.randomUUID().toString(), it.name, startTime, endTime))
                y++
            }
        }
        weekView.notifyDataSetChanged()
    }

    private fun clearEventToDate(calendar: Calendar) {
        val startTime = nowCalendar.clone() as Calendar

        startTime.add(Calendar.WEEK_OF_YEAR, 1)
        val matchedEvents = mutableListOf<WeekViewEvent>()
        events.forEach {
            if(it.startTime > startTime && it.startTime < calendar)
                matchedEvents.add(it)
        }
        events.removeAll(matchedEvents)
    }

    private fun clearEvent(startTime: Calendar, endTime: Calendar) {
        startTime.add(Calendar.WEEK_OF_YEAR, 1)
        endTime.add(Calendar.WEEK_OF_YEAR, 1)

        val matchedEvents = mutableListOf<WeekViewEvent>()
        events.forEach {
            if(it.startTime > startTime && it.endTime < endTime)
                matchedEvents.add(it)
        }
        events.removeAll(matchedEvents)
    }

    private fun cloneNextWeek(eventsInWeek: MutableList<WeekViewEvent>) {
        eventsInWeek.forEach {
            var startTime = it.startTime.clone() as Calendar
            var endTime = it.endTime.clone() as Calendar
            startTime.add(Calendar.DAY_OF_MONTH, 7)
            endTime.add(Calendar.DAY_OF_MONTH, 7)
            events.add(WeekViewEvent(UUID.randomUUID().toString(), it.name, startTime, endTime))
        }
        weekView.notifyDataSetChanged()
    }

    private fun getEventsInWeek(startTime: Calendar, endTime: Calendar): MutableList<WeekViewEvent> {
        val matchedEvents = mutableListOf<WeekViewEvent>()

        events.forEach {
            if(it.startTime > startTime && it.endTime < endTime)
                matchedEvents.add(it)
        }

        return matchedEvents
    }

    private fun addItemLongClick(startTime: Calendar, isFirstPeriod: Boolean) {

        var endTime = startTime.clone() as Calendar
        if(isFirstPeriod){
            endTime.add(Calendar.MINUTE, 30)
        }
        else{
            startTime.set(Calendar.MINUTE, 30)
            endTime.add(Calendar.HOUR, 1)
        }

        var event = WeekViewEvent(UUID.randomUUID().toString(), getEventTitle(startTime, endTime), startTime, endTime)
        event.color = ResourcesCompat.getColor(resources, R.color.event_color_01, null)
        editEvents.add(event)
        events.add(event)
        weekView.notifyDataSetChanged()
    }

    protected open fun setupDateTimeInterpreter(shortDate: Boolean) {
        val locale = Locale.getDefault()
        weekView.weekDaySubtitleInterpreter = object : WeekDaySubtitleInterpreter {
            val dateFormatTitle = SimpleDateFormat("d", locale)

            override fun getFormattedWeekDaySubtitle(date: Calendar): String = dateFormatTitle.format(date.time)
        }
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val dateFormatTitle = SimpleDateFormat("EEE", locale)
        weekView.dateTimeInterpreter = object : DateTimeInterpreter {
            override fun getFormattedTimeOfDay(hour: Int, minutes: Int): String {
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minutes)
                return timeFormat.format(calendar.time)
            }

            override fun getFormattedWeekDayTitle(date: Calendar): String {
                return dateFormatTitle.format(date.time)
            }
        }
    }

    private fun rqGetSchedules(token: String, email: String) {
        NetworkModule.getService().getSchedules(token, email)
                .enqueue(object : Callback<ScheduleResponse> {
                    override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                        t.printStackTrace()
                    }

                    override fun onResponse(call: Call<ScheduleResponse>, response: Response<ScheduleResponse>) {
                        loadSchedules(response.body())
                    }

                })
    }

    private fun loadSchedules(response: ScheduleResponse?) {

        Log.d("TAG", response.toString())
        response?.schedules?.let { it ->

            it.forEach {value ->
                Log.d("TAG", "startTime: ${value.startTime * 1000} - endTime: ${value.endTime * 1000}")
                var startTime = Calendar.getInstance()
                var endTime = Calendar.getInstance()
                startTime.timeInMillis = value.startTime * 1000
                endTime.timeInMillis = value.endTime * 1000
                events.add(WeekViewEvent(UUID.randomUUID().toString(), getEventTitle(startTime, endTime), startTime, endTime))
            }

            weekView.notifyDataSetChanged()
        }
    }

    private fun getEventTitle(startTime: Calendar, endTime: Calendar): String? {
        return ""
    }

    /**
     * Checks if an event falls into a specific year and month.
     *
     * @param event The event to check for.
     * @param year  The year.
     * @param month The month.
     * @return True if the event matches the year and month.
     */
    private fun eventMatches(event: WeekViewEvent, year: Int, month: Int): Boolean {
        return event.startTime.get(Calendar.YEAR) == year && event.startTime.get(Calendar.MONTH) == month - 1 || event.endTime.get(Calendar.YEAR) == year && event.endTime.get(Calendar.MONTH) == month - 1
    }

}
