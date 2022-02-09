package akotlin
import akotlin.Util.TreeNode

fun main() {
    Util.transferTreeNode4Array(arrayOf(1,2,3,null,5,null,null)).let {
        Code257().binaryTreePaths(it).let {
            it.forEach {
                println(it)
            }
        }
    }
}
class Code257 {
    fun binaryTreePaths(root: TreeNode?): List<String> {
        val result = mutableListOf<String>()
        binaryTreePaths(root,"",result)
        return result
    }
    fun binaryTreePaths(root: TreeNode?,s:String ,result: MutableList<String>) {
        if(root ==null){
//            result.add(s)
            return
        }
        if(root.left ==null && root.right ==null) {
            result.add((s+"->${root.`val`}").substring(2))
            return
        }

        binaryTreePaths(root.left,s+"->${root.`val`}",result)
        binaryTreePaths(root.right,s+"->${root.`val`}",result)

    }
}