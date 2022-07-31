import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.view.menu.MenuView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.chick.*
import com.example.chick.MainFragment.Companion.showDialogGoalDone
import kotlin.math.log


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
                var curPos : Int = adapterPosition      // 터치된 어댑터의 포지션
                var drug : DrugAll = drugAllList.get(curPos)        // 터치된 위치의 데이터 가져오기

                val intent = Intent(MainFragment.ApplicationContext(), EditAlarmActivity::class.java)
                intent.putExtra("intent_id", drug.medId)
                MainFragment.ApplicationContext().startActivity(intent)
            }
            // 복용 버튼
            btnEat.setOnClickListener {
                var curPos : Int = adapterPosition      // 터치된 어댑터의 포지션
                var drug : DrugAll = drugAllList.get(curPos)        // 터치된 위치의 데이터 가져오기
                MainFragment?.eatDrug(drug.medId!!, drug.eatDone!!,drug.eatNumber!!, drug.currentNumber!!, drug.totalNumber!!)

//                Handler(Looper.getMainLooper()).postDelayed({
//                    //실행할 코드
//                    if(drug.goalDone == 1){
//                        showDialogGoalDone()
//                    }
//                }, 500)

//                if(drug.currentNumber==0 && (drug.totalNumber!! <= (drug.currentNumber!!+drug.eatNumber!!))){
//                    Handler(Looper.getMainLooper()).postDelayed({
//                    //실행할 코드
//                        showDialogGoalDone()
//                    }, 500)
//                }
                drug = drugAllList.get(curPos)
                if(drug.goalDone ==1){
                    Handler(Looper.getMainLooper()).postDelayed({
                        //실행할 코드
                        showDialogGoalDone()
                    }, 500)
                }
                val intent = Intent(MainFragment.ApplicationContext(), MainActivity::class.java)
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION)
                MainFragment.ApplicationContext().startActivity(intent)

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

        //리사이클러뷰 복용 시간 데이터 출력
        if(drugAllList!![position].alarmMin!! < 10 ){
            var time = drugAllList!![position].ampm + " "+drugAllList!![position].alarmHour+":"+ "0" + drugAllList!![position].alarmMin
            holder.txtTime.text = time  // 약 복용시간
        }
        else{
            var time = drugAllList!![position].ampm + " "+drugAllList!![position].alarmHour+":"+ drugAllList!![position].alarmMin
            holder.txtTime.text = time  // 약 복용시간
        }

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