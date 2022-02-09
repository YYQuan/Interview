package akotlin
import akotlin.Util.ListNode
import java.util.*
import kotlin.collections.ArrayList

fun main() {
    Util.transferListNode4Array(intArrayOf(1,2,3,4,5)).let {
        Util.print(it!!)

        Code328().oddEvenList(it).let {
            Util.print(it!!)
        }
    }
}
class Code328 {
    fun oddEvenList(head: ListNode?): ListNode? {

        if(head == null) return null
        var node = head
        val  list = LinkedList<ListNode>()
        while(node!=null){
            list.addLast(node)
            node = node.next
        }
        var isFlag = true
        val listJ = ArrayList<ListNode>()
        val listO= ArrayList<ListNode>()
        while(list.isNotEmpty()){
            val tmp = list.removeFirst()
            if(isFlag){
                if(listJ.lastIndex>=0) {
                    listJ[listJ.lastIndex].next  = tmp
                }
                listJ.add(tmp)
            }else{
                if(listO.lastIndex>=0) {
                    listO[listO.lastIndex].next  = tmp
                }

                listO.add(tmp)
            }
            isFlag =!isFlag
        }
        if(listJ.lastIndex>=0)  {
            listJ[listJ.lastIndex].next = if(listO.lastIndex>=0 )listO[0] else null
        }
        if(listO.lastIndex>=0) {
            listO[listO.lastIndex].next = null
        }
        return head
    }
}