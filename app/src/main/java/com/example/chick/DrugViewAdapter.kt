import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.*
import androidx.appcompat.view.menu.MenuView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.chick.DrugAll
import com.example.chick.R


// 뷰 어댑터
class DrugViewAdapter(val drugAllList: ArrayList<DrugAll>): RecyclerView.Adapter<DrugViewAdapter.DrugViewHolder>(){

    // 항목 개수를 판단
    override fun getItemCount(): Int {
        return drugAllList.size
    }

    // 뷰 홀더 준비
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugViewAdapter.DrugViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_new_druglist, parent, false)
        return DrugViewHolder(view).apply {
            // 편집 버튼
            btnEdit.setOnClickListener{

            }
            // 복용 버튼
            btnEat.setOnClickListener {

            }
        }
    }
    // 뷰 홀더의 뷰에 데이터 호출 (실제 데이터 출력)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: DrugViewAdapter.DrugViewHolder, position: Int) {

        // 뷰에 데이터 출력 (리사이클러 뷰 아이템 정보)
        holder.txtName.text = drugAllList!![position].medName       // 약 이름
        if(drugAllList!![position].daysOfWeek=="월화수목금토일"){      // 약 주기
            holder.txtRotation.text = "매일" + "  1회 "+drugAllList!![position].eatNumber+"정"
        }else{
            holder.txtRotation.text = drugAllList!![position].daysOfWeek + "  1회 "+drugAllList!![position].eatNumber+"정"
        }
        var time = drugAllList!![position].ampm + " "+drugAllList!![position].alarmHour+":"+drugAllList!![position].alarmMin
        holder.txtTime.text = time      // 약 복용시간

        // 복용 여부에 따른 카드뷰 색상 변경
        var eatDone = drugAllList!![position].eatDone
        if (eatDone == 0){
            holder.card.setBackgroundResource(R.drawable.bg_drug_gray)
            holder.btnEat.setBackgroundResource(R.drawable.ic_main_eatbtn1)
        }else{
            holder.card.setBackgroundResource(R.drawable.bg_drug_blue)
            holder.btnEat.setBackgroundResource(R.drawable.ic_main_eatbtn2)
        }

        // 약 아이콘 변경
        var img = drugAllList!![position].medIcon
        when(img){
            1 -> {
                holder.icDrug.setImageResource(R.drawable.ic_drugs_1)
            }
            2 -> {
                holder.icDrug.setImageResource(R.drawable.ic_drugs_2)
            }
            3 -> {
                holder.icDrug.setImageResource(R.drawable.ic_drugs_3)
            }
            4 -> {
                holder.icDrug.setImageResource(R.drawable.ic_drugs_4)
            }
            5 -> {
                holder.icDrug.setImageResource(R.drawable.ic_drugs_5)
            }
            6 -> {
                holder.icDrug.setImageResource(R.drawable.ic_drugs_6)
            }
            7 -> {
                holder.icDrug.setImageResource(R.drawable.ic_drugs_7)
            }
            8 -> {
                holder.icDrug.setImageResource(R.drawable.ic_drugs_8)
            }
        }
    }

    // 뷰 홀더
    class DrugViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val card : RelativeLayout = itemView.findViewById(R.id.mainCardLayout)      // 카드뷰
        val txtName : TextView = itemView.findViewById(R.id.txtMainDrugName)    // 약이름
        val txtRotation : TextView = itemView.findViewById(R.id.txtMainDrugRotation)    // 복용주기
        val txtTime : TextView = itemView.findViewById(R.id.txtMainDrugTime)    // 복용시간
        val icDrug : ImageView = itemView.findViewById(R.id.icMainDrug)     // 약 아이콘
        val btnEdit : ImageButton = itemView.findViewById(R.id.btnMainEdit)     // 알람 수정 버튼
        val btnEat : AppCompatButton = itemView.findViewById(R.id.btnMainEat)        // 복용 버튼
    }

}