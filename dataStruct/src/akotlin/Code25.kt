package akotlin
import akotlin.Util.ListNode

fun main() {

    val ints = intArrayOf(1,2,3,4,5,)
//    val ints = intArrayOf(1,2)
    val code = Code25()
    code.reverseKGroup(Util.transferListNode4Array(ints),3).let {
        Util.print(it!!)
    }
}

// 流程参考 code25.png
// 核心就是 每一个翻转块都是要做k次的 指向翻转,
// 表面上看起来是k-1次
// 这个是因为每个翻转周期过了之后 是需要把翻转块头的指向给改变的
// 另外要注意 指针移动后 指向的位置
class Code25 {
    fun reverseKGroup(head: ListNode?, k: Int): ListNode? {
        if(head == null )  return null
        if(k <= 1 )  return head
        var virtualHead = ListNode(0)
        virtualHead.next = head
        var pKNode= virtualHead
        var pNode = virtualHead
        var cNode:ListNode? = head
        var nNode:ListNode? = head.next
        while(cNode!=null){
            var tmpNode:ListNode? = pNode
            for(i in 1..k){
                tmpNode = tmpNode?.next
                if(tmpNode == null){
                    return virtualHead.next
                }
            }
            for(i in 1..k){
                cNode!!.next =pNode
                pNode = cNode
                cNode = nNode
                nNode  = nNode?.next
            }
            val pKNextTmp = pKNode.next
            pKNode.next = pNode
            pKNextTmp!!.next = cNode
            pKNode = pKNextTmp
            pNode = pKNextTmp
        }
        return  virtualHead.next
    }
}