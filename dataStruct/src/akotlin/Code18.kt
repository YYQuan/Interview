package akotlin

fun main() {
    val ints = intArrayOf(1,0,-1,0,-2,2)
    val code = Code18()
    code.fourSum(ints,0).forEach {
        Util.printArray(it.toIntArray())
    }
}
class Code18 {
    fun fourSum(nums: IntArray, target: Int): List<List<Int>> {

        nums.sort()
//        Util.printArray(nums)
        val result = arrayListOf<List<Int>>()
        var l  = 0

        while(l<nums.size-3){
//            println("four -->  l ${l} ${nums[l]}  target ${target - nums[l]}")

            val threeResult = threeSum(nums,target - nums[l],l+1)
            threeResult.forEach {
                threeList ->
                result.add( arrayListOf<Int>().let {
                    it.addAll(threeList)
                    it.add(nums[l])
                    it })
            }
            do {
                l++
            }while(l<nums.size-3&&nums[l]==nums[l-1])
        }
        return result
    }
    fun threeSum(nums: IntArray, target: Int,start:Int): List<List<Int>> {
        val result  = arrayListOf<List<Int>>()
        var l = start
//        println("three -->  start $start  target $target")

        while(l<nums.size-2){

            val twoResult = twoSum(nums,target-nums[l],l+1)
            twoResult.forEach { twoList ->
                result.add(  arrayListOf<Int>().let {
                    it.addAll(twoList)
                    it.add(nums[l])
                    it
                })
            }
            do {
                l++
            }while (l<nums.size-2&&nums[l]== nums[l-1])

        }

        return result
    }
    fun twoSum(nums: IntArray, target: Int,start:Int): List<List<Int>> {
        val result = arrayListOf<List<Int>>()
        var l = start
        var r = nums.size-1
//        println("two -->  start $start  target $target")

        while(l<r){
//            println(" l $l  r $r ")
            nums[l+1]?.let {
                if(nums[l]==nums[l+1]&&nums[l]*2 == target){
                    result.add(arrayListOf<Int>().let {
                        it.add(nums[l])
                        it.add(nums[l])
                        it
                    })
                }
                while(l<r &&nums[l]== nums[l+1]){
                    l++
                }
            }
            if(l>=r) break

            nums[r-1]?.let {
                if(nums[r] == nums[r-1]&&nums[r]*2 == target){
                  result.add(arrayListOf<Int>().let {
                      it.add(nums[r])
                      it.add(nums[r])
                      it
                  })
                }
                while(l<r && nums[r] == nums[r-1]){
                    r--
                }
            }
            if(l>=r ) break
            val sum = nums[r]+nums[l]
            if(sum == target){
                result.add(arrayListOf<Int>().let {
                    it.add(nums[l])
                    it.add(nums[r])
                    it
                })
                r--
                l++
            }else if(sum <target){
                l++
            }else{
                r--
            }
        }
        return result
    }
}