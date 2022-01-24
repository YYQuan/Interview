package akotlin
import  akotlin.Util.TreeNode
import java.util.*

fun main() {

//    Util.transferTreeNode4Array(arrayOf(1,2,3)).let {
    Util.transferTreeNode4Array(arrayOf(0,1,null)).let {
        Code129().sumNumbers(it).let {
            println(it)
        }
    }
}
class Code129 {
    fun sumNumbers(root: TreeNode?): Int {
        if(root ==null ) return 0
        val result = LinkedList<Int>()
        var v = 0
        sumNumbers(root,LinkedList(),result)
        result.forEach {
            v +=it
        }
        return v

    }
    fun sumNumbers(root: TreeNode,list:LinkedList<Int>,result:LinkedList<Int>) {
        if(root.left == null&&root.right == null){
            var agin = 10
            var v = root.`val`
            for(i in list.size-1 downTo 0){
                var value = list[i]
                v += value*agin
                agin*=10
            }
            println("v :$v ")
            result.add(v)
            return
        }

        list.addLast(root.`val`)
        root.left?.let {
            sumNumbers(root.left!!,list,result)
        }
        root.right?.let {
            sumNumbers(root.right!!, list, result)
        }
        list.removeLast()

    }
}