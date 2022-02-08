package akotlin
import akotlin.Util.ListNode

fun main() {
    Util.transferListNode4Array(intArrayOf(1,2,3,4,5)).let {
        Code206().reverseList(it).let {
            Util.print(it!!)
        }
    }
}
class Code206 {
    fun reverseList(head: ListNode?): ListNode? {
        if(head == null) return null
        val virtualHead = ListNode(0)
        virtualHead.next =head

        var p = virtualHead
        var c = head
        var n = c.next

        while(c !=null){

            c.next = p

            p = c
            c = n
            n = n?.next

        }
        head.next = null
        return p
    }
}