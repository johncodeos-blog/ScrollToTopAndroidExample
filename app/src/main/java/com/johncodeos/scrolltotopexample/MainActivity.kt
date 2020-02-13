package com.johncodeos.scrolltotopexample

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.johncodeos.scrolltotopexample.model.Car
import com.johncodeos.scrolltotopexample.model.CarElement
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    lateinit var carsCells: Car
    lateinit var adapter: Cars_RVAdapter

    var animatedHide = false
    var animatedShow = false
    var findLastVisibleItemPositionValue = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mLayoutManager = LinearLayoutManager(cars_rv.context)
        cars_rv.layoutManager = mLayoutManager
        cars_rv.setHasFixedSize(true)


        toolbartitle.ellipsize = TextUtils.TruncateAt.MARQUEE
        toolbartitle.isSingleLine = true
        toolbartitle.isSelected = true


        toolbartitle.setOnClickListener { cars_rv.smoothScrollToPosition(0) }

        toolbartitle.text = "ScrollToTopExample"

        loadData()

        // Scroll To Top Arrow(ImageView) Customization
        scroll_to_top_arrow.borderColor = Color.WHITE
        scroll_to_top_arrow.circleBackgroundColor =
            ContextCompat.getColor(this, R.color.colorPrimaryDark)
        scroll_to_top_arrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)


        // Positions for the arrow when is hidden and visible
        val whenVisibleMargin = convertDpToPixel(15f, cars_rv.context)
        val whenHideMargin = convertDpToPixel(-85f, cars_rv.context)

        // Hide the arrow at the beginning when the screen starts
        scroll_to_top_arrow.visibility = View.GONE

        cars_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (mLayoutManager.findLastVisibleItemPosition() >= findLastVisibleItemPositionValue) {
                    if (!animatedShow) {
                        scroll_to_top_arrow.visibility = View.VISIBLE
                        val params = scroll_to_top_arrow.layoutParams as RelativeLayout.LayoutParams
                        val animator =
                            ValueAnimator.ofInt(params.rightMargin, whenVisibleMargin.toInt())
                        animator.addUpdateListener { valueAnimator ->
                            params.rightMargin = valueAnimator.animatedValue as Int
                            scroll_to_top_arrow.requestLayout()
                        }
                        animator.duration = 300
                        animator.start()
                        animatedShow = true
                        animatedHide = false
                    }
                } else {
                    if (!animatedHide) {
                        scroll_to_top_arrow.visibility = View.VISIBLE
                        val params = scroll_to_top_arrow.layoutParams as RelativeLayout.LayoutParams
                        val animator =
                            ValueAnimator.ofInt(params.rightMargin, whenHideMargin.toInt())
                        animator.addUpdateListener { valueAnimator ->
                            params.rightMargin = valueAnimator.animatedValue as Int
                            scroll_to_top_arrow.requestLayout()
                        }
                        animator.duration = 300
                        animator.start()
                        animatedHide = true
                        animatedShow = false
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        // Scroll to the top when you press the arrow
        scroll_to_top_arrow.setOnClickListener {
            cars_rv.post {
                cars_rv.smoothScrollToPosition(0)
            }
        }
    }


    private fun loadData() {
        carsCells = Car()
        try {
            val jsonLocation = loadJSONFromAsset()
            val jsonarray = JSONArray(jsonLocation ?: "")
            for (i in 0 until jsonarray.length()) {
                val jsonobject = jsonarray.get(i) as JSONObject
                val carMake = jsonobject.getString("car_make")
                val carModel = jsonobject.getString("car_model")
                val carModelYear = jsonobject.getInt("car_model_year")
                carsCells.add(CarElement(carMake, carModel, carModelYear))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        adapter = Cars_RVAdapter(carsCells)

        cars_rv.adapter = adapter
    }


    private fun loadJSONFromAsset(): String? {
        val json: String?
        try {
            val `is` = this.assets.open("CarsDemoData.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }

    private fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}
