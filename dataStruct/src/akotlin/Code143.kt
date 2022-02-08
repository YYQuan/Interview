package akotlin
import akotlin.Util.ListNode

fun main() {

//    Util.transferListNode4Array(intArrayOf(1,2,3,4,5)).let {
    Util.transferListNode4Array(intArrayOf(1,2,3,4)).let {
        Util.print(it!!)
        Code143().reorderList(it)
        Util.print(it!!)
    }
}


class Code143 {
    fun reorderList(head: ListNode?): Unit {
        if(head ==null) return

        val list = ArrayList<ListNode>()

//        list.add(head)
        var node = head
        while(node!=null){
            list.add(node)
            node = node.next
        }
        node = head
        for(i in 1..list.size/2){
            val nodeR = list[list.size-i]
            val nodeL = list[i]
//            println("i $i  ${nodeR.`val`}  ${nodeL.`val`}")
            node!!.next= nodeR
            nodeR.next=nodeL
            node = nodeL

        }
        val nodeLast =list[list.size/2]
//        println("nodeLast ${nodeLast.`val`}  ${list.size/2}  ${list.size}")
        nodeLast.next = null
        return
    }
}