package com.example.kurly_project.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.model.Product
import com.example.kurly_project.databinding.ItemProductBinding
import com.example.kurly_project.databinding.ItemProductVerticalBinding
import timber.log.Timber
import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.kurly_project.R
import com.example.kurly_project.data.WishlistManager
import com.example.kurly_project.model.ProductUiModel

class SectionProductAdapter(
    private val context: Context,
    private val items: MutableList<ProductUiModel>,
    private val viewType: ViewType
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val wishlistManager = WishlistManager(context)

    override fun getItemViewType(position: Int): Int {
        return viewType.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ViewType.entries[viewType]) {
            ViewType.GRID, ViewType.HORIZONTAL -> {
                val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HorizontalViewHolder(binding)
            }
            else -> {
                val binding = ItemProductVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VerticalViewHolder(binding)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = items[position]
        when (holder) {
            is HorizontalViewHolder -> holder.bind(product)
            is VerticalViewHolder -> holder.bind(product)
        }
    }

    override fun getItemCount(): Int = items.size


    inner class HorizontalViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductUiModel) {
            binding.apply {
                Timber.d("[ESES##] product.image = ${product.image}")
                textTitle.text = product.name
                Glide.with(binding.root)
                    .load(product.image)
                    .listener(object : RequestListener<Drawable> {
                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            Timber.d("[ESES##] 이미지 로드 성공: $model")
                            return false
                        }

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            Timber.e(e, "[ESES##] 이미지 로드 실패: $model")
                            return false
                        }
                    })
                    .into(binding.imageProduct)


                if (product.discountedPrice != null) {
                    textDiscountRate.visibility = View.VISIBLE
                    val discountRate = 100 - (product.discountedPrice!! * 100 / product.originalPrice)
                    textDiscountRate.text = "$discountRate%"
                    textSalePrice.text = "${product.discountedPrice}원"
                    textOriginalPrice.apply {
                        visibility = View.VISIBLE
                        text = "${product.originalPrice}원"
                        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }
                } else {
                    textDiscountRate.visibility = View.GONE
                    textOriginalPrice.visibility = View.GONE
                    binding.textSalePrice.text = "${product.originalPrice}원"
                }

                textWishlist.setBackgroundResource(
                    if (product.isWished) R.drawable.ic_btn_heart_on
                    else R.drawable.ic_btn_heart_off
                )

                textWishlist.setOnClickListener {
                    val position = absoluteAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val product = items[position]
                        wishlistManager.toggleWished(product.id)
                        product.isWished = wishlistManager.isWished(product.id)
                        notifyItemChanged(position)
                    }
                }
            }
        }
    }

    inner class VerticalViewHolder(private val binding: ItemProductVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductUiModel) {
            binding.textTitle.text = product.name
            Glide.with(binding.root).load(product.image).into(binding.imageProduct)

            binding.apply {

                if (product.discountedPrice != null) {
                    textDiscountRate.visibility = View.VISIBLE
                    val discountRate = 100 - (product.discountedPrice!! * 100 / product.originalPrice)
                    textDiscountRate.text = "$discountRate%"
                    textSalePrice.text = "${product.discountedPrice}원"
                    textOriginalPrice.apply {
                        visibility = View.VISIBLE
                        text = "${product.originalPrice}원"
                        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }
                } else {
                    textDiscountRate.visibility = View.GONE
                    textOriginalPrice.visibility = View.GONE
                    binding.textSalePrice.text = "${product.originalPrice}원"
                }

                textWishlist.setBackgroundResource(
                    if (product.isWished) R.drawable.ic_btn_heart_on
                    else R.drawable.ic_btn_heart_off
                )

                textWishlist.setOnClickListener {
                    val position = absoluteAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val product = items[position]
                        wishlistManager.toggleWished(product.id)
                        product.isWished = wishlistManager.isWished(product.id)
                        notifyItemChanged(position)
                    }
                }

            }
        }
    }
}