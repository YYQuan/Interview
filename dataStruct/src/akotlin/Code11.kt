package akotlin

import java.lang.Math.max
import java.lang.Math.min

fun main() {

    var ints  = intArrayOf(1,8,6,2,5,4,8,3,7)
    var code = Code11()
    println("${code.maxArea(ints)}")

}
class Code11 {
    fun maxArea(height: IntArray): Int {
        Util.notNull(height){}?: return 0
        if(height.size <2) return 0
        var max  = Int.MIN_VALUE
        var l = 0
        var r = height.size-1
        while(l <r){
            max = max(max, (r-l)* min(height[r],height[l]))
            if(height[r] > height[l]){
                var base = height[l]
                while(l<r){
                    l++;
                    if(height[l]>base){
                        break
                    }
                }
            }else{
                var base = height[r]
                while(l<r){
                    r--;
                    if(height[r]>base){
                        break
                    }
                }
            }
        }
        return max
    }
}