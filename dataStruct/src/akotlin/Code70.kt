package akotlin

fun main() {

    Code70().climbStairs(3).let {
        println(it)
    }
}
class Code70 {
    fun climbStairs(n: Int): Int {
        if(n==1) return 1
        if(n==2) return 2
        val array = IntArray(n+1)
        array[1]=1
        array[2]=2
        var i = 3
        while(i<=n){
            array[i]= array[i-1]+array[i-2]
            i++
        }
        return array[n]
    }
}