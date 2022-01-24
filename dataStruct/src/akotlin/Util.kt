package akotlin

import java.util.*

fun main() {
//    val t1 = Util.TreeNode(1)
//    val t2 = Util.TreeNode(2)
//    val t3 = Util.TreeNode(3)
//    val t4 = Util.TreeNode(4)
//    t1.left = t2
//    t1.right = t3
//    t2.left = t4
//    Util.print(t1)

//    val ints = arrayOf(1,2,3,4,null,null,null,5,null,null,null,null,null,null,null)
    val ints = arrayOf(1,2,3,null,4,null,null)
    Util.transferTreeNode4Array(ints).let {
//        Util.print(it!!)
        println("${it?.`val`}  ${it?.left?.`val`}  ${it?.left?.left?.`val`}  ${it?.left?.right?.`val`}  ")
    }
}
class Util {

    companion object{


        inline fun <R>  allNull(vararg args:Any?,  block :(()->R))= when{
            args.filterNotNull().size ==0 -> block()
            else -> null
        }
        inline fun <R>  hasNull(vararg args:Any?,  block :(()->R))= when{
            args.filterNotNull().size ==args.size -> null
            else -> block()
        }
        inline fun <R>  notAllNull(vararg args:Any?,  block :(()->R))= when{
            args.filterNotNull().isNotEmpty() -> block()
            else -> null

        }
        inline fun <R>  notNull(vararg args:Any?,  block :(()->R))= when{
            args.filterNotNull().size ==args.size -> block()
            else -> null

        }
        fun transferListNode4Array( array:IntArray):ListNode?{
            if(array.isEmpty()) throw IllegalStateException();
            val virtualHead = ListNode(Int.MIN_VALUE)
            var node:ListNode? = virtualHead

            array.forEach {
                node?.next = ListNode(it)
                node = node?.next
            }
            return virtualHead.next!!
        }
        fun print(node :ListNode){
            var n:ListNode? = node
            val s = StringBuilder()
            while(Util.notNull(n){true}?:false){
                s.append("${n?.`val`} ->")
                n?.let {
                    n = n?.next
                }
            }
            println("listNode : $s")

        }
        fun printArray(ints :IntArray){
            ints.forEach {
                print("$it ,")
            }
            println()
        }
        fun print(treeNode:TreeNode){
            val queue = LinkedList<TreeNode?>()
            queue.add(treeNode)
            var depth = 0
            while(!queue.isEmpty()){
                if(queue.filterNotNull().isEmpty()) break
                depth++
                val builder = StringBuilder()
                var size = queue.size
                for(i in 1..size){
                    val t1 = queue.removeFirst()
                    builder.append("${t1?.`val`?:"null"}   ")
                    queue.add(t1?.left)
                    queue.add(t1?.right)
                }
                print("---->> $depth : $builder")
                println()
            }
        }
        fun transferTreeNode4Array(array:Array<Int?>):TreeNode?{
            if(array.isEmpty()) return   null

            var arrayQueue = LinkedList<Int?>()
            array.forEach {
                arrayQueue.addLast(it)
            }
            var queueNode = LinkedList<TreeNode?>()
            var queueNext = LinkedList<Int?>()
            var result :TreeNode?= null
            queueNode.add(TreeNode(arrayQueue.removeFirst()!!).let {
                result = it
                it
            })
            queueNext.add(arrayQueue.removeFirst())
            queueNext.add(arrayQueue.removeFirst())
            while(!queueNext.isEmpty()){
                val queueNodeSize = queueNode.size
                for(index in 0 until queueNodeSize){
                    val t1 = queueNode.removeFirst()
                    t1?.left = if (queueNext[2 * index] == null) null else TreeNode(queueNext[2 * index]!!)
                    t1?.right = if( queueNext[2*index+1] == null) null else TreeNode(queueNext[2*index+1]!!)
                    queueNode.addLast(t1?.left)
                    queueNode.addLast(t1?.right)
                }
                val size = queueNext.size
                queueNext.clear()
                if(arrayQueue.isEmpty()) break
                for(i in 1..2*size){
                    queueNext.add(arrayQueue.removeFirst())
                }
            }
            return result
        }
    }

    class ListNode(var `val`: Int) {
        var next: ListNode? = null
    }

    class TreeNode(var `val`: Int) {
             var left: TreeNode? = null
             var right: TreeNode? = null

    }

}