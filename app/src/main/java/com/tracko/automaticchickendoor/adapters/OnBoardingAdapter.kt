package com.tracko.automaticchickendoor.adapters

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.tracko.automaticchickendoor.databinding.ItemOnboardingScreenBinding
import com.tracko.automaticchickendoor.models.local.OnBoardingItem

class OnBoardingAdapter(
    private val items: List<OnBoardingItem>,
    private val onAutoScroll: (Int) -> Unit
) : RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {
    
    inner class OnBoardingViewHolder(
        private val binding: ItemOnboardingScreenBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        private var animator: ValueAnimator? = null
        
        fun bind(item: OnBoardingItem, position: Int) {
            binding.apply {
                ivOnboarding.setImageResource(item.imageRes)
                tvTitle.text = item.title
                tvDescription.text = item.description
                
                animator?.cancel()
                arrowButton.progressCircular.progress = 0
                
                animator = animateProgress(arrowButton.progressCircular, 10_000) {
                    onAutoScroll(position)
                }
                
                arrowButton.arrowProgressButton.setOnClickListener {
                    arrowButton.progressCircular.progress = 100
                    onAutoScroll(position)
                }
            }
        }
        
        private fun animateProgress(
            progressBar: CircularProgressIndicator,
            duration: Long,
            onComplete: () -> Unit
        ): ValueAnimator {
            return ValueAnimator.ofInt(0, 100).apply {
                this.duration = duration
                addUpdateListener { animation ->
                    val progress = animation.animatedValue as Int
                    progressBar.progress = progress
                    if (progress == 100) {
                        onComplete()
                    }
                }
                start()
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val binding = ItemOnboardingScreenBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OnBoardingViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.bind(items[position], position)
    }
    
    override fun getItemCount() = items.size
}
