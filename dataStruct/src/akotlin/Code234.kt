package akotlin
import akotlin.Util.ListNode
import java.util.*
import kotlin.collections.ArrayList

fun main() {
    Code234().isPalindrome(Util.transferListNode4Array(intArrayOf(1,2,2,1)).let {
        Util.print(it!!)
        it
    }).let {
        println(it)
    }
}

class Code234 {
    fun isPalindrome(head: ListNode?): Boolean {

        if(head == null) return false
        val virtualHead = ListNode(0)
        virtualHead.next =head
        var p = virtualHead
        var c = head
        // 注意用 link list  会超时, 因为后面有步用下标来找元素的操作
        val queue = ArrayList<Int>()
        while(c!=null){
            queue.add(c.`val`)
            p = p.next!!
            c = c.next
        }

        for(i in 0..queue.size/2){
            if(queue[i]!=queue[queue.size-1-i]) return false
        }
        return true
    }
}