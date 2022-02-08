package akotlin
import akotlin.Util.ListNode
fun main() {

    Util.transferListNode4Array(intArrayOf(4,2,1,3)).let {
        Util.print(it!!)

        Code148().sortList(it).let {
            Util.print(it!!)
        }
//        Util.print(it!!)
    }
}
class Code148 {
    fun sortList(head: ListNode?): ListNode? {
        if(head == null) return head
        val virtualHead = ListNode(Int.MIN_VALUE)
        virtualHead.next=head

        var p1 = virtualHead
        var c1 = head
        var n1 = head.next

        while(c1!=null){

            if(c1.`val` <p1.`val`){
                var p2  =virtualHead
                var c2 = p2.next

                while(c1.`val`>=c2!!.`val`){
                    p2  = p2.next!!
                    c2 = c2.next
                }

                p2.next = c1
                c1.next = c2

                p1.next = n1
                c1 = n1
                n1 = n1?.next

            }else{
                p1 = p1.next!!
                c1 = c1.next
                n1 = n1?.next
            }


        }
        return virtualHead.next


    }
}