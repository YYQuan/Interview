package akotlin
import akotlin.Util.ListNode

fun main() {
//    val ints = intArrayOf(0,1,2)
    val ints = intArrayOf(1,2,3,4,5)
    val node =Util.transferListNode4Array(ints)
//    Code61().rotateRight(node,2000000000).let {
//        Util.print(it!!)
//    }
    Code61().rotateRight(node,2).let {
        Util.print(it!!)
    }
}
class Code61 {
    //收尾相连后 根据 长度计算出偏移量
    fun rotateRight(head: ListNode?, k: Int): ListNode? {
        if(head == null) return null
        if(head.next == null||k<1) return head

        var pNode =head
        var rNode =head
        var length = 1
        while(rNode!!.next!=null){
            rNode=rNode.next
            length++
        }

        rNode.next =head
//        println("length $length  rNode :${rNode.`val`}")
        val offset  =length -   k%length-1

        for(i in 1 ..offset){
            pNode = pNode!!.next
        }

        var result = pNode!!.next
        pNode.next = null
        return result


    }
}