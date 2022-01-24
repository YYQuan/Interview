package akotlin

fun main() {
    Code52().totalNQueens(4).let {
        println(it)
    }
}
class Code52 {
    fun totalNQueens(n: Int): Int {
        val array = Array(n){Array(n){'.'} }
        return totalNQueens(0,array)
    }

    fun totalNQueens(row:Int ,status :Array<Array<Char>>): Int {
        if(row>= status.size){
            return 1
        }
        var result = 0
        for(i in row until status.size){
            for (j in  status.indices){
                if(isLegal(i,j,status)){
                    status[i][j] ='Q'
                    result += totalNQueens(row + 1, status)
                    status[i][j] ='.'
                }
            }
            break

        }
        return result
    }

    fun isLegal(row:Int,coloum:Int,status:Array<Array<Char>>):Boolean{

        for(i in 0 until row){
            if (status[i][coloum] == 'Q') return false
        }
        val vL = row -coloum
        val vR = row +coloum
        for( i in 0 until row){
            for(j in status.indices){
                if(i -j == vL && status[i][j]=='Q') return false
                if(i +j == vR && status[i][j]=='Q') return false
            }
        }
        return true
    }

}