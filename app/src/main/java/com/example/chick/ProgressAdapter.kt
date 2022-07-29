package com.example.chick

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class ProgressAdapter(val druglist:ArrayList<ProDrugAll>) : RecyclerView.Adapter<ProgressAdapter.ProViewHolder>(){

    override fun getItemCount(): Int {
        return druglist.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_progresslist, parent, false)
        return ProViewHolder(view)
    }

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
        holder.txtProPercent.text = percent.toString() + "%"

        //리사이클러뷰 프로그레스바 출력
        holder.prbbar.progress = percent

        //리사이클러뷰 복용 개수 데이터 출력
        holder.txtProCount.text = "" + druglist!![position].currentNumber + "/" + druglist!![position].totalNumber + "정 복용"

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

    }

    //복용률_뷰홀더
    class ProViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtProName : TextView = itemView.findViewById(R.id.txtProDrugName)    // 약이름
        val txtProRotation : TextView = itemView.findViewById(R.id.txtProDrugRotation)    // 복용주기
        val txtProTime : TextView = itemView.findViewById(R.id.txtProDrugTime)    // 복용시간
        val icProDrug : ImageView = itemView.findViewById(R.id.Promedicon1)     // 약 아이콘
        val txtProPercent : TextView = itemView.findViewById(R.id.txtProPercent)   // 약 퍼센트
        val prbbar : ProgressBar = itemView.findViewById(R.id.MedprogressBar)    // 약 프로그레스바
        val txtProCount : TextView = itemView.findViewById(R.id.txtProCount)    // 약 복용 개수(20/100정)

    }
}