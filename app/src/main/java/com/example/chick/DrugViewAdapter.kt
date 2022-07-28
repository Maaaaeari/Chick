import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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
    override fun onBindViewHolder(holder: DrugViewAdapter.DrugViewHolder, position: Int) {
        // 뷰에 데이터 출력 (리사이클러 뷰 아이템 정보)
        holder.txtName.text = drugAllList!![position].medName
        if(drugAllList!![position].daysOfWeek=="월화수목금토일"){
            holder.txtRotation.text = "매일" + "  1회 "+drugAllList!![position].eatNumber+"정"
        }else{
            holder.txtRotation.text = drugAllList!![position].daysOfWeek + "  1회 "+drugAllList!![position].eatNumber+"정"
        }
        var time = drugAllList!![position].ampm + " "+drugAllList!![position].alarmHour+":"+drugAllList!![position].alarmMin
        holder.txtTime.text = time

        // 복용 여부에 따른 색상 변경


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
        val txtName : TextView = itemView.findViewById(R.id.txtMainDrugName)    // 약이름
        val txtRotation : TextView = itemView.findViewById(R.id.txtMainDrugRotation)    // 복용주기
        val txtTime : TextView = itemView.findViewById(R.id.txtMainDrugTime)    // 복용시간
        val icDrug : ImageView = itemView.findViewById(R.id.icMainDrug)     // 약 아이콘
        val btnEdit : ImageButton = itemView.findViewById(R.id.btnMainEdit)     // 알람 수정 버튼
        val btnEat : Button = itemView.findViewById(R.id.btnMainEat)        // 복용 버튼
    }

}