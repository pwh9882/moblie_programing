package com.example.mobile_programing.views.adapters.helpers

import android.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_programing.views.adapters.RoutineAdapter
import java.util.Collections

class ItemTouchHelperCallback(private val adapter: RoutineAdapter) : ItemTouchHelper.Callback() {
    override fun isLongPressDragEnabled(): Boolean {
        return true // 롱클릭 드래그를 활성화
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN // 드래그 방향 설정
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // 스와이프 방향 설정

        return makeMovementFlags(dragFlags, swipeFlags) // 드래그와 스와이프 허용
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {

        adapter.onItemMove(viewHolder.absoluteAdapterPosition, target.absoluteAdapterPosition)
        return true
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // 스와이프된 아이템의 위치를 가져옵니다.
        val position = viewHolder.adapterPosition
        val routine = adapter.routineList[position]

        // AlertDialog를 생성하여 삭제를 확인합니다.
        AlertDialog.Builder(adapter.activity).apply {
            setTitle("삭제 확인") // 제목 설정
            setMessage("이 항목을 삭제하시겠습니까?") // 메시지 설정
            setPositiveButton("예") { dialog, which ->
//                 '예'를 선택하면 아이템 삭제
//                adapter.removeAt(position)
                // ViewModel에서 아이템 삭제 로직을 수행합니다.
                routine.let { adapter.viewModel.deleteRoutine(it) }
            }
            setNegativeButton("아니오") { dialog, which ->
                // '아니오'를 선택하면 아무 일도 하지 않고, 스와이프된 아이템을 원래 위치로 되돌립니다.
                adapter.notifyItemChanged(position)
            }
            setCancelable(false) // 바깥쪽을 터치해도 창이 닫히지 않도록 설정
            create().show() // 대화상자 표시
        }
    }
}
