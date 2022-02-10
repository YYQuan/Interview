package akotlin

fun main() {
    Code417().pacificAtlantic( arrayOf(
        intArrayOf(1,2,2,3,5),
        intArrayOf(3,2,3,4,4,),
        intArrayOf(2,4,5,3,1),
        intArrayOf(6,7,1,4,5),
        intArrayOf(5,1,1,2,4),
    )).let {
        it.forEach {
            it.forEach {
                print("$it " )
            }
            println()
        }
    }
}
class Code417 {
    // 从后往前 计算
    fun pacificAtlantic(heights: Array<IntArray>): List<List<Int>> {

        val dp = Array(heights.size){BooleanArray(heights.first().size)}
        val isRead = Array(heights.size){BooleanArray(heights.first().size)}

        val result  = mutableListOf<List<Int>>()


        for(i in heights.lastIndex downTo  0){
            for(j in heights.first().lastIndex downTo  0){

                if(canToTopOrLeft(heights,dp,isRead,i,j).let {
                        isRead.forEach {
                            it.fill(false)
                        }
                        it
                    }&&canToBottomOrRight(heights,dp,isRead,i,j).let {
                        isRead.forEach {
                            it.fill(false)
                        }
                        it }){
                    dp[i][j] = true
                }

            }
        }


        dp.forEachIndexed { indexR, booleans ->
            booleans.forEachIndexed { indexC, b ->
                if(b) result.add(intArrayOf(indexR,indexC).toList())
            }
        }
//        dp.forEach {
//            it.forEach {
//                print("$it   ")
//            }
//            println()
//        }
        return result
    }

    fun canToTopOrLeft(heights:Array<IntArray>,dp:Array<BooleanArray>,isRead:Array<BooleanArray>,row:Int,colum:Int):Boolean{

        if(row == 0 || colum == 0) return true
        if(isRead[row][colum]) return false
        isRead[row][colum] = true

        if(heights[row][colum]>=heights[row-1][colum]) {
            if(dp[row-1][colum]) return true
            canToTopOrLeft(heights,dp,isRead,row-1,colum).let {
                if(it) return true
            }
        }
        if(heights[row][colum]>=heights[row][colum-1]) {
            if(dp[row][colum-1]) return true
            canToTopOrLeft(heights,dp,isRead,row,colum-1).let {
                if(it) return true
            }
        }

        if(row<heights.size-1){
            if(heights[row][colum]>=heights[row+1][colum]) {
                if(dp[row+1][colum]) return true
                canToTopOrLeft(heights,dp,isRead,row+1,colum).let {
                    if(it) return true
                }
            }
        }

        if(colum< heights.first().size-1){
            if(heights[row][colum]>=heights[row][colum+1]) {
                if(dp[row][colum+1]) return true
                canToTopOrLeft(heights,dp,isRead,row,colum+1).let {
                    if(it) return true
                }
            }
        }

        return false
    }

    fun canToBottomOrRight(heights:Array<IntArray>,dp:Array<BooleanArray>,isRead:Array<BooleanArray>,row:Int,colum:Int):Boolean{

        if(row == heights.size-1 || colum == heights.first().size-1) return true
        if(isRead[row][colum]) return false
        isRead[row][colum] = true
        if(row>0){
            if(heights[row][colum]>=heights[row-1][colum]) {
                if(dp[row-1][colum]) return true
                canToBottomOrRight(heights,dp,isRead,row-1,colum).let {
                    if(it) return true
                }
            }
        }
        if(colum>0){
            if(heights[row][colum]>=heights[row][colum-1]) {
                if(dp[row][colum-1]) return true
                canToBottomOrRight(heights,dp,isRead,row,colum-1).let {
                    if(it) return true
                }
            }
        }

        if(row<heights.size-1){
            if(heights[row][colum]>=heights[row+1][colum]) {
                if(dp[row+1][colum]) return true
                canToBottomOrRight(heights,dp,isRead,row+1,colum).let {
                    if(it) return true
                }
            }
        }

        if(colum< heights.first().size-1){
            if(heights[row][colum]>=heights[row][colum+1]) {
                if(dp[row][colum+1]) return true
                canToBottomOrRight(heights,dp,isRead,row,colum+1).let {
                    if(it) return true
                }
            }
        }

        return false
    }
}