package akotlin
import akotlin.Util.ListNode

fun main() {

    Code203().removeElements(Util.transferListNode4Array(intArrayOf(1,2,6,3,4,5,6)),6).let {
        Util.print(it!!)
    }
}
class Code203 {

    fun removeElements(head: ListNode?, `val`: Int): ListNode? {

        if(head == null) return null

        val virtualHead = ListNode(0)
        virtualHead.next = head

        var p = virtualHead
        var c = head
        var n = head.next

        while(c !=null){
            if(c.`val` == `val`){
                p.next = n
                c = n
                n = n?.next
            }else {
                c = c.next
                p = p.next!!
                n = n?.next
            }
        }
        return virtualHead.next
    }
}