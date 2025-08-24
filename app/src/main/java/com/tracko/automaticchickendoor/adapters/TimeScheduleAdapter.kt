package com.tracko.automaticchickendoor.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tracko.automaticchickendoor.R
import com.tracko.automaticchickendoor.databinding.ItemScheduleBinding
import com.tracko.automaticchickendoor.models.local.TimeSchedule
import java.text.SimpleDateFormat
import java.util.*

class TimeScheduleAdapter(
    private val timeSchedules: MutableList<TimeSchedule>,
    private val onDayClick: (String, Int) -> Unit,
    private val onSwitchChange: (Boolean, Int) -> Unit
) : RecyclerView.Adapter<TimeScheduleAdapter.TimeScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeScheduleViewHolder {
        val binding = ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeScheduleViewHolder, position: Int) {
        val schedule = timeSchedules[position]
        holder.bind(schedule, position)
    }

    override fun getItemCount(): Int = timeSchedules.size

    inner class TimeScheduleViewHolder(private val binding: ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root) {

        private fun adjustTimeBy15Minutes(currentTime: String, increment: Boolean): String {
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val calendar = Calendar.getInstance()

            try {
                calendar.time = timeFormat.parse(currentTime)
                if (increment) {
                    calendar.add(Calendar.MINUTE, 15)
                } else {
                    calendar.add(Calendar.MINUTE, -1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return timeFormat.format(calendar.time)
        }

        fun bind(schedule: TimeSchedule, position: Int) {
            binding.tvEndTime.text = schedule.endTime

            val selectedDays = schedule.selectedDays
            val days = mapOf(
                "Mon" to binding.tvMonday,
                "Tue" to binding.tvTuesday,
                "Wed" to binding.tvWednesday,
                "Thu" to binding.tvThursday,
                "Fri" to binding.tvFriday,
                "Sat" to binding.tvSaturday,
                "Sun" to binding.tvSunday
            )
            days.forEach { (day, textView) ->
                textView.setBackgroundResource(
                    if (selectedDays.contains(day)) R.drawable.selected_day_bg
                    else android.R.color.transparent
                )
                textView.setTextColor(
                    if (selectedDays.contains(day))
                        ContextCompat.getColor(textView.context, R.color.white)
                    else
                        ContextCompat.getColor(textView.context, R.color.dark_text_color)
                )
                textView.setOnClickListener {
                    val newSelectedDays = selectedDays.toMutableSet()
                    if (newSelectedDays.contains(day)) {
                        newSelectedDays.remove(day)
                    } else {
                        newSelectedDays.add(day)
                    }
                    timeSchedules[position] = schedule.copy(selectedDays = newSelectedDays)
                    notifyItemChanged(position)
                    onDayClick(day, position)
                }
            }

            binding.switchSchedule.isChecked = schedule.scheduleSwitchEnable
            binding.switchSchedule.setOnCheckedChangeListener { _, isChecked ->
                onSwitchChange(isChecked, position)
            }

            binding.ivUp.setOnClickListener {
                val newEndTime = adjustTimeBy15Minutes(schedule.endTime, increment = true)
                timeSchedules[position] = schedule.copy(endTime = newEndTime)
                notifyItemChanged(position)
            }

            binding.ivDown.setOnClickListener {
                val newEndTime = adjustTimeBy15Minutes(schedule.endTime, increment = false)
                timeSchedules[position] = schedule.copy(endTime = newEndTime)
                notifyItemChanged(position)
            }
        }
    }
}
