package com.tracko.automaticchickendoor.adapters

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.tracko.automaticchickendoor.R
import com.tracko.automaticchickendoor.databinding.ItemBleDeviceBinding

class BleDeviceAdapter(
    private val devices: List<BluetoothDevice>,
    private val onItemClick: (BluetoothDevice) -> Unit
) : RecyclerView.Adapter<BleDeviceAdapter.BleDeviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BleDeviceViewHolder {
        val binding = ItemBleDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BleDeviceViewHolder(binding)
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: BleDeviceViewHolder, position: Int) {
        val device = devices[position]

        holder.binding.apply {
            tvDeviceId.text = device.name ?: "Unknown Device"
            tvDeviceAddress.text = device.address
            tvDeviceStatus.text = "Online"
            tvDeviceStatus.background = ResourcesCompat.getDrawable(
                holder.itemView.context.resources,
                R.drawable.textview_filled_online_rounded_bg,
                null
            )
        }

        holder.itemView.setOnClickListener {
            onItemClick(device)
        }
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    class BleDeviceViewHolder(val binding: ItemBleDeviceBinding) : RecyclerView.ViewHolder(binding.root)
}
