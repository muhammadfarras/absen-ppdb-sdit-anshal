package com.example.sditsattendance

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import androidx.core.widget.NestedScrollView
import com.example.sditsattendance.api.Response
import com.example.sditsattendance.data.Access
import com.example.sditsattendance.network.DataRepository
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback


var isFabOpen:Boolean = false
var isBackClick:Boolean = false
const val DURATION_OF_ME = 300
lateinit var mainView:View;
lateinit var myContext: Context
class MainActivity : AppCompatActivity(), View.OnClickListener,
    SeekBar.OnSeekBarChangeListener,OnChartValueSelectedListener,
    NestedScrollView.OnScrollChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        myContext = this


        // Ambil data dari APi
        getResultPeserta (this)

        mainView = findViewById<View>(R.id.layout);

        fab.setOnClickListener {
            if (!isFabOpen){
                fabShow()
            }
            else {
                fabHide()
            }
        }



        fab1.setOnClickListener {
            startActivity (Intent (this,FindActivity::class.java))
            fabHide()
        }

        fab2.setOnClickListener {
            var hasPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA);

            // check apakah memiliki akses atu tidak
            if (hasPermission == PackageManager.PERMISSION_GRANTED){
                var intent = Intent(this,ScanActivity::class.java)
                startActivity(intent)
                // Hide fab setiap intent
                fabHide()
            }
            else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),Access.camera);
            }

        }


        // swap for refresh the data
        swap_refresh.setOnRefreshListener {
            Log.d("tag","Data di perbaharui")
            getResultPesertaRefresh (this)
        }


        card_ikhwan.setOnClickListener(this)
        card_akhwat.setOnClickListener(this)

        nested_scroll_view.setOnScrollChangeListener(this)



    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode){
            Access.camera -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    true
                }
                else {
                    // Check apakah always deneid atau tidak
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.CAMERA)){
                        Snackbar.make(mainView,R.string.pesan_butuh_akses,Snackbar.LENGTH_LONG)
                            .show()

                    }
                    else {
                        Snackbar.make(mainView,R.string.pesan_butuh_akses,Snackbar.LENGTH_LONG)
                            .setAction("Pengaturan") {

                                val intent = Intent()
                                intent.action  = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                val uri = Uri.fromParts("package",this.packageName,null)
                                intent.data = uri
                                startActivity(intent)

                            }
                            .show()
                    }
                }
            }
        }
    }

    private fun fabShow ():Unit{
        isFabOpen = true
        fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.baseline_clear_black_18dp))

//        set translation X
        cardfab.animate().translationX(-resources.getDimension(R.dimen.standar_80))

        val animation = object : Animation () {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {

                //Change dp to px
                val myScale = resources.displayMetrics.density
                var myPx = (60*interpolatedTime * myScale + 0.5f).toInt()

                //set left margin
                val paramsMargin: FrameLayout.LayoutParams = fab2.layoutParams as FrameLayout.LayoutParams
                paramsMargin.setMargins(myPx,0,0,0)
                fab2.layoutParams = paramsMargin
            }
        }
        animation.duration = DURATION_OF_ME.toLong()
        fab.startAnimation(animation)




    }

    private fun fabHide ():Unit {
        isFabOpen = false
        fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.baseline_search_black_18dp))


        val animationClose = object : Animation () {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                //animasi translation x
                var currentTranslationX = cardfab.translationX - (cardfab.translationX * interpolatedTime)
                cardfab.animate().translationX(currentTranslationX)

                //set left margin to 0
                val paramsMargin: FrameLayout.LayoutParams = fab2.layoutParams as FrameLayout.LayoutParams
                // get curent margin left and reduce as iterpolated time
                var currentMargin = paramsMargin.leftMargin - (paramsMargin.leftMargin * interpolatedTime).toInt()
                paramsMargin.setMargins(currentMargin,0,0,0)
                fab2.layoutParams = paramsMargin
                Log.d("fabShow","${currentMargin}")
            }
        }

        animationClose.duration = DURATION_OF_ME.toLong()
        fab.startAnimation(animationClose)
    }

    override fun onBackPressed() {
        Handler().postDelayed({
            isBackClick = false
        },1000)
        if (!isBackClick){
            Toast.makeText(this,R.string.keluar,Toast.LENGTH_SHORT).show()
            isBackClick = true
        }else{
            super.onBackPressed()
        }

    }

    override fun onClick(v: View?) {
        var value = "";
        when (v?.id){
            R.id.card_ikhwan -> {
                value = "ikhwan"
            }
            R.id.card_akhwat ->{
                value = "akhwat"
            }
            R.id.card_1 -> {
                value = "kelompok 1"
            }
            R.id.card_2 -> {
                value = "kelompok 2"
            }
            R.id.card_3 -> {
                value = "kelompok 3"
            }
            R.id.card_4 -> {
                value = "kelompok 4"
            }
            R.id.card_5 -> {
                value = "kelompok 5"
            }
            R.id.card_6 -> {
                value = "kelompok 6"
            }
            R.id.card_7 -> {
                value = "kelompok 7"
            }
            R.id.card_8 -> {
                value = "kelompok 8"
            }
            R.id.card_9 -> {
                value = "kelompok 9"
            }
            R.id.card_10 -> {
                value = "kelompok 10"
            }

        }

        val intent = Intent(applicationContext,ListPesertaActivity::class.java)
        intent.putExtra("information",value)
        startActivity(intent)
    }

    private fun getResultPeserta (context:Context) {
        val responeService = DataRepository.create()
        responeService.getResponse().enqueue(object : Callback<List<Response>>{
            override fun onResponse(
                call: Call<List<Response>>,
                response: retrofit2.Response<List<Response>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
//                    Log.d("tag", "responsennya ${data?.size}")

                    data?.map {
                        var nilaiIkhwan = it.ikhwan?.toFloat()
                        var nilaiAkhwat = it.akhwat?.toFloat()
                        createPieChart (nilaiIkhwan ,nilaiAkhwat)
                        jml_pendaftar.setText("${it.hasil}")
                        jml_pendaftar_ikhwan.setText("${it.ikhwan}")
                        jml_pendaftar_akhwat.setText("${it.akhwat}")
                        jml_pendaftar_I.setText("${it.kelSatu}")
                        jml_pendaftar_II.setText("${it.kelDua}")
                        jml_pendaftar_III.setText("${it.kelTiga}")
                        jml_pendaftar_IV.setText("${it.kelEmpat}")
                        jml_pendaftar_V.setText("${it.kelLima}")
                        jml_pendaftar_VI.setText("${it.kelEnam}")
                        jml_pendaftar_VII.setText("${it.kelTujuh}")
                        jml_pendaftar_VIII.setText("${it.kelDelapan}")
                        jml_pendaftar_IX.setText("${it.kelSembilan}")
                        jml_pendaftar_XI.setText("${it.kelSepuluh}")

//                        Log.d("tag", "datanya ${it.hasil}")
                    }
                }
            }

            override fun onFailure(call: Call<List<Response>>, t: Throwable) {
                Toast.makeText(context,"Gagal terhubung ke server",Toast.LENGTH_SHORT).show()
//                Log.d("tag", "datanya ${t.localizedMessage}")
            }
        })
    }

    private fun getResultPesertaRefresh (context: Context) {
        val responeService = DataRepository.create()
        responeService.getResponse().enqueue(object : Callback<List<Response>>{
            override fun onResponse(
                call: Call<List<Response>>,
                response: retrofit2.Response<List<Response>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
//                    Log.d("tag", "responsennya ${data?.size}")

                    data?.map {
                        var nilaiIkhwan = it.ikhwan?.toFloat()
                        var nilaiAkhwat = it.akhwat?.toFloat()
                        createPieChart (nilaiIkhwan ,nilaiAkhwat)
                        jml_pendaftar.setText("${it.hasil}")
                        jml_pendaftar_ikhwan.setText("${it.ikhwan}")
                        jml_pendaftar_akhwat.setText("${it.akhwat}")
                        jml_pendaftar_I.setText("${it.kelSatu}")
                        jml_pendaftar_II.setText("${it.kelDua}")
                        jml_pendaftar_III.setText("${it.kelTiga}")
                        jml_pendaftar_IV.setText("${it.kelEmpat}")
                        jml_pendaftar_V.setText("${it.kelLima}")
                        jml_pendaftar_VI.setText("${it.kelEnam}")
                        jml_pendaftar_VII.setText("${it.kelTujuh}")
                        jml_pendaftar_VIII.setText("${it.kelDelapan}")
                        jml_pendaftar_IX.setText("${it.kelSembilan}")
                        jml_pendaftar_XI.setText("${it.kelSepuluh}")
//                        Log.d("tag", "datanya ${it.hasil}")

                        swap_refresh.isRefreshing = false
                    }
                }
            }

            override fun onFailure(call: Call<List<Response>>, t: Throwable) {
//                Log.d("tag", "datanya ${t.localizedMessage}")
                swap_refresh.isRefreshing = false
                Toast.makeText(context,"Gagal terhubung ke server",Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    private fun generateCenterSpannableText(): SpannableString? {
        val s = SpannableString("SistemPPDB\ndeveloped by Abu Faris")
        s.setSpan(RelativeSizeSpan(1.2f), 0, 10, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 0, 10, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 10, s.length - 9, 0)
        s.setSpan(RelativeSizeSpan(.8f), 10, s.length - 9, 0)
        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 9, s.length, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length - 9, s.length, 0)
        return s
    }

    private fun createPieChart ( intIkhwan:Float?, intAkhwat:Float?) {

        Log.d("PIE","Nilai ikhwan ${intIkhwan} \n Akhwat ${intAkhwat}")
        pie_chart_gender.setUsePercentValues(true)
        pie_chart_gender.description.isEnabled = false;
        pie_chart_gender.setExtraOffsets(5f, 10f, 5f, 5f);
        pie_chart_gender.setDragDecelerationFrictionCoef(0.95f);

        pie_chart_gender.centerText = generateCenterSpannableText()

//        Deskripsi dari pie chart
        var description:Description = pie_chart_gender.description
        description.text = "Prosentase jenis kelamin terhadap jumlah pendaftar PPDB SDIT Anak Shalih Bogor"
        pie_chart_gender.holeRadius = 70f
        pie_chart_gender.transparentCircleRadius = 61f

        pie_chart_gender.setDrawCenterText(true)

        pie_chart_gender.rotationAngle = 0f
        pie_chart_gender.isRotationEnabled = false
        val entry = ArrayList<PieEntry>()

        // Masukan nilai ke chart
        intIkhwan?.let { PieEntry(it) }?.let { entry.add(it) }
        intAkhwat?.let { PieEntry(it) }?.let { entry.add(it) }

        var dataSet = PieDataSet(entry,getString(R.string.info_jenis_kelamin))

        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f,40f)
        dataSet.selectionShift = 5f

        // Mauskan warna
        var arrayListColor = ArrayList<Int>()
        arrayListColor.add(Color.rgb(33, 147, 176))
        arrayListColor.add(Color.rgb(238, 156, 167))

        dataSet.colors = arrayListColor

        var data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(pie_chart_gender))

        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        pie_chart_gender.data = data

        pie_chart_gender.highlightValue(null)
    }

    @SuppressLint("RestrictedApi")
    override fun onScrollChange(
        v: NestedScrollView?,
        p1: Int,
        p2: Int,
        p3: Int,
        p4: Int
    ) {
        var alph:Double = 0.0
        val maxChildHeight = v?.getChildAt(0)?.measuredHeight?.toDouble()
        val maxNestedHeight = v?.measuredHeight?.toDouble()



        // jika sudah melewati 3/4 dari yang sudah ada
        if (maxChildHeight != null) {

            if (p2  > (0.65 * ( maxChildHeight - maxNestedHeight!!))) {

                fab.animate().alpha(0f).setDuration(300).translationX(v.width.toFloat())
                cardfab.animate().alpha(0f).setDuration(200).translationY((v.height.toFloat()))



            }
            else {

                fab.animate().alpha(1f).setDuration(200).translationX(0f)

                cardfab.animate().alpha(1f).setDuration(300).translationY(0f)



            }
        }
    }
}

