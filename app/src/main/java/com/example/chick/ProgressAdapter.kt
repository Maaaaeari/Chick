package com.example.chick

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.TypedArrayUtils.getDrawable
import androidx.core.content.res.TypedArrayUtils.getResourceId
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class ProgressAdapter(val druglist:ArrayList<ProDrugAll>) : RecyclerView.Adapter<ProgressAdapter.ProViewHolder>(){

    override fun getItemCount(): Int {
        return druglist.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_progresslist, parent, false)
        return ProViewHolder(view)
    }

    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(holder: ProViewHolder, position: Int) {
        //리사이클러뷰 약 이름 데이터 출력
        holder.txtProName.text = druglist!![position].medName

        //리사이클러뷰 주기 및 요일, 시간 데이터 출력
        if(druglist!![position].daysOfWeek=="월화수목금토일"){
            holder.txtProRotation.text = "매일" + "  1회 "+druglist!![position].eatNumber+"정"
        }else{
            holder.txtProRotation.text = druglist!![position].daysOfWeek + "  1회 "+druglist!![position].eatNumber+"정"
        }

        //리사이클러뷰 복용 시간 데이터 출력
        if(druglist!![position].alarmMin!! < 10 ){
            var time = druglist!![position].ampm + " "+druglist!![position].alarmHour+":"+ "0" + druglist!![position].alarmMin
            holder.txtProTime.text = time
        }
        else{
            var time = druglist!![position].ampm + " "+druglist!![position].alarmHour+":"+ druglist!![position].alarmMin
            holder.txtProTime.text = time
        }

        //리사이클러뷰 복용 퍼센트 출력
        var cNumber  = druglist!![position].currentNumber.toString().toDouble()
        var tNumber  = druglist!![position].totalNumber.toString().toDouble()
        var percent  = ((cNumber!! / tNumber!!) * 100).toInt()
        if(percent > 100){
            percent = 100
        }
        else if(percent <= 100){
            percent
        }
        holder.txtProPercent.text = percent.toString() + "%"

        //리사이클러뷰 프로그레스바 출력
        holder.prbbar.progress = percent

        //리사이클러뷰 복용 개수 데이터 출력
        holder.txtProCount.text = "" + druglist!![position].currentNumber + "/" + druglist!![position].totalNumber

        // 약 아이콘 변경
        var img = druglist!![position].medIcon
        when(img){
            1 -> {
                holder.icProDrug.setImageResource(R.drawable.ic_drugs_1)
            }
            2 -> {
                holder.icProDrug.setImageResource(R.drawable.ic_drugs_2)
            }
            3 -> {
                holder.icProDrug.setImageResource(R.drawable.ic_drugs_3)
            }
            4 -> {
                holder.icProDrug.setImageResource(R.drawable.ic_drugs_4)
            }
            5 -> {
                holder.icProDrug.setImageResource(R.drawable.ic_drugs_5)
            }
            6 -> {
                holder.icProDrug.setImageResource(R.drawable.ic_drugs_6)
            }
            7 -> {
                holder.icProDrug.setImageResource(R.drawable.ic_drugs_7)
            }
            8 -> {
                holder.icProDrug.setImageResource(R.drawable.ic_drugs_8)
            }
        }

        // 100%일 경우 1.카드 및 프로그레스바 색 변화 2. 제일 하단으로 이동
        if(percent == 100) {
            //druglist!![position].goalDone
            holder.cardPro.setBackgroundResource(R.drawable.shape_round_darkyellow) //카드 색 변화

            //아이템 순서 변경
        }
        else{

        }

    }


    //복용률_뷰홀더
    class ProViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtProName : TextView = itemView.findViewById(R.id.txtProDrugName)    // 약 이름
        val txtProRotation : TextView = itemView.findViewById(R.id.txtProDrugRotation)    // 복용주기
        val txtProTime : TextView = itemView.findViewById(R.id.txtProDrugTime)    // 복용시간
        val icProDrug : ImageView = itemView.findViewById(R.id.Promedicon1)     // 약 아이콘
        val txtProPercent : TextView = itemView.findViewById(R.id.txtProPercent)   // 약 퍼센트
        val prbbar : ProgressBar = itemView.findViewById(R.id.MedprogressBar)    // 약 프로그레스바
        val txtProCount : TextView = itemView.findViewById(R.id.txtProCount)    // 약 복용 개수(20/100정)
        val cardPro : RelativeLayout = itemView.findViewById(R.id.cardPro)  // 카드 색

    }
}
