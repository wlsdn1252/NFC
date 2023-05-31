package com.example.fragmentpractice3.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fragmentpractice3.databinding.ItemViewBinding
import com.example.fragmentpractice3.datas.ReData


// 리사이클러뷰.어댑터를 상속받을 때 <>안에 뷰홀더를 받아야함
// 그래서 이너클래스로 뷰홀더를 만들었다.
// 이너클래스라서 사용할 때는 클래스이름.클래스이름을 쓴다.
// WordAdapter는 Word데이터클래스를 변경가능한 리스트 형태로 받는다.
class Adapters(
    val list: MutableList<ReData>,
    // 클릭리스너의 속성을 ItemClickListener(인터페이스)로 정의
    private val itemClickListener: ItemClickListener? = null
    ) : RecyclerView.Adapter<Adapters.WordViewHolder>() {


    //WordAdapter클래스가 들고있는 list의 데이터 개수를 알려준다.
    // 즉 Word데이터 클래스의에 들어있는 데이터 수???
    override fun getItemCount(): Int {
        return list.size
    }

    // 뷰홀더를 만들 때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        // 뷰를 그리기위한 인플레이터 선언
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //
        val binding = ItemViewBinding.inflate(inflater, parent,false)
        return WordViewHolder(binding)
    }


    // onCreateViewHolder에서 만든 뷰홀더로 화면 UI와 데이터를 연결
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = list[position]
        holder.bind(word)
        // 클릭메서드 처리를 위한 구문
        holder.itemView.setOnClickListener{itemClickListener?.onClick(word)}

    }

    // 뷰홀더클래스의 속성은 리사이클러뷰.뷰홀더를 속성으로 받는다.
    class WordViewHolder(private val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(word:ReData){
            binding.apply {
                mainPageMedi.text =word.mediName
                text11.text = word.textVal
                mainPageEdit.text = "편집"
            }
        }
    }

    // 클릭리스너 구현을위한 인터페이스 설계
    // 리사이클러뷰의 클릭리스너는 어댑터에서 구현해야함
    // 메인액티비티에서 onClick함수 사용 예정
    interface ItemClickListener{
        fun onClick(word:ReData)
    }


}