package akotlin

fun main() {
//    val ints = intArrayOf(-1,2,1,-4)
//    val ints = intArrayOf(0,0,0)
//    val ints = intArrayOf(0,2,1,-3)
    val ints = intArrayOf(1,1,-1,-1,3)
    val code = Code16()
    println("${code.threeSumClosest(ints,-1)}")
}
class Code16 {
    fun threeSumClosest(nums: IntArray, target: Int): Int {

        if(nums.isEmpty()) throw IllegalStateException()
        nums.sort()
        var resultCom = Int.MAX_VALUE
        var result = 0
//        nums.forEach {
//            print("$it ")
//        }
//        println()
        var l = 0
        while(l<nums.size-2){
            var twoSum = twoSumColoest(nums,target-nums[l],l+1)
            if(resultCom> Math.abs(target - nums[l]-twoSum)){
                resultCom  = Math.abs(target - nums[l]-twoSum)
                result =  nums[l]+twoSum
            }
//            println( " ${nums[l]}  ${target-nums[l]}  $twoSum")
            do {
                l++
            }while(l<nums.size-2&&nums[l]== nums[l-1])
        }
        return result
    }

    fun  twoSumColoest(nums:IntArray,target:Int,start:Int):Int{
        var resultCom = Int.MAX_VALUE
        var result = 0
        var l  = start
        var r = nums.size-1

        while(l<r){
            nums[l+1]?.let {
                if(nums[l] == nums[l+1]){

                    if(resultCom>Math.abs(target - nums[l]*2) ){
                        result= nums[l]*2
                        resultCom = Math.abs(target - nums[l]*2)
                    }

                    do {
                        l++
                    }while(l<r&&nums[l] == nums[l+1])
                }
            }
            if(l >= r) break
            nums[r-1]?.let {
                if(nums[r] == nums[r-1]){
                    if(resultCom > Math.abs(target - 2*nums[r])){
                        resultCom = Math.abs(target - 2*nums[r])
                        result = 2*nums[r]
                    }
                    do {
                        r--
                    }while(l<r&&nums[r]==nums[r-1])
                }
            }
            if(l >= r) break
            val sum = nums[l] +nums[r]
            if(resultCom >Math.abs(target - sum)){
                resultCom = Math.abs(target - sum)
                result = sum
            }
//            println( " --- >>> $l ${nums[l]}  ,$r ${nums[r]}")
            if(sum == target) {
                l++
                r--
            }else if( sum <target){
                l++
            }else{
                r--
            }
        }
        return result
    }
}