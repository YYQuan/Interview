package akotlin

fun main() {
//    Code447().numberOfBoomerangs(arrayOf(intArrayOf(0,0),
//        intArrayOf(1,0),
//        intArrayOf(2,0))).let {
        Code447().numberOfBoomerangs(arrayOf(intArrayOf(1,1),
            intArrayOf(2,2),
            intArrayOf(3,3))).let {
            println(it)
        }

}
class Code447 {
    //思路记录下  点之间的距离  然后分析
    fun numberOfBoomerangs(points: Array<IntArray>): Int {
        if(points.size<3) return 0
        var result  = 0
        val map = HashMap<Long,Int>()
        for(i in points.indices){
            for(j in points.indices){
                if(i == j ) continue
                val dis  = calculator(points[i],points[j])
                map[dis] = map.getOrDefault(dis,0)+1
            }
            map.values.forEach {
                if(it>1) result += it *(it-1)
            }
            map.clear()
        }
        return result
    }

    fun calculator(int1 :IntArray,int2:IntArray):Long{

        val x = Math.abs(int1[0]-int2[0])
        val y = Math.abs(int1[1]-int2[1])


        return (x*x +y*y).toLong()
    }
}