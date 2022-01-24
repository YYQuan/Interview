package akotlin

import java.lang.StringBuilder

fun main() {

    Code51().solveNQueens(4).forEach {
        println()
        it.forEach {
            print("$it , ")
        }
    }
}
class Code51 {

    fun solveNQueens(n: Int): List<List<String>> {
        val status = Array(n) { Array(n) { '.' } }
        val result = mutableListOf<List<String>>()
        solveNQueens(0,status,result)
        return result
    }

    fun solveNQueens(row:Int ,status :Array<Array<Char>>,result: MutableList<List<String>> ){
        if(row>=status.size) {
            val tmpList = mutableListOf<String>()
            status.forEach { chars->
                var builder= StringBuilder()
                for(i in status.indices){
                    builder.append(chars[i])
                }
                tmpList.add(builder.toString())
            }
            result.add(tmpList)
            return
        }
        for(i in row until status.size){
            for(j in status.indices){
                val isLegal = isLegal(i,j,status)
                if(isLegal){
                    status[i][j] = 'Q'
                    solveNQueens(i+1,status,result)
                    status[i][j] = '.'
                }
            }
            return
        }
    }

    fun isLegal(row:Int, coloum:Int  ,status :Array<Array<Char>>):Boolean{

        // 竖 左斜 右斜
        for(i in  status.indices){
            if(status[i][coloum] == 'Q')return false
        }

        val vL = row - coloum
        val vR = row + coloum
        for(i in 0 until row){
            for(j in status.indices){
                if(i -j == vL&&status[i][j] == 'Q'){
                    return false
                }
                if(i +j == vR&&status[i][j] == 'Q'){
                    return false
                }
            }
        }


        return true

    }

}