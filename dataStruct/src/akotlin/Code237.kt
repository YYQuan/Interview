package akotlin
import  akotlin.Util.ListNode

fun main() {
    Util.transferListNode4Array(intArrayOf(4,5,1,9)).let {
        Util.print(it!!)

        Code237().deleteNode(it)
        Util.print(it!!)
    }
}
class Code237 {
    fun deleteNode(node: ListNode?) {

        if(node == null) return

        var cNode = node
        var nNode = node.next
        println("${cNode?.`val`}   ${nNode?.`val`}")

        while(nNode!=null){

            println("${cNode?.`val`}   ${nNode?.`val`}")
            cNode?.`val` = nNode!!.`val`
            nNode.next?.let {
                cNode = nNode
                nNode = nNode?.next
            }?: run{
                cNode!!.next = null
                nNode = null
            }



        }

    }
}