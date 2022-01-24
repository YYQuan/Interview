package akotlin

fun main() {
    Code62().uniquePaths(23,12).let {
        println(it)
    }
}
class Code62 {
    fun uniquePaths(m: Int, n: Int): Int {
        val board = Array(m) { Array(n) { 0 } }
        for(i in 0 until  n){
            board[0][i] = 1
        }

        for(i in 1 until m){
            for(j in 0 until n){
                if(j == 0) board[i][j] = board[i-1][j]
                else  board[i][j] = board[i][j-1]+board[i-1][j]
            }
        }
        return board[m-1][n-1]
    }

}