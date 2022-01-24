package akotlin

fun main() {
//    val chars =Array(4){
//        charArrayOf()
//    }.let {
//        it[0] = charArrayOf('X', 'X', 'X', 'X')
//        it[1] = charArrayOf('X', 'O', 'O', 'X')
//        it[2] = charArrayOf('X', 'X', 'O', 'X')
//        it[3] = charArrayOf('X', 'O', 'X', 'X')
//        it
//    }


    val chars =Array(3){
        charArrayOf()
    }.let {
        it[0] = charArrayOf('O','X','O')
        it[1] = charArrayOf('X','O','X')
        it[2] = charArrayOf('O','X','O')
        it
    }
    chars.forEach {
        it.forEach {
            println(it)
        }
    }

    Code130().solve(chars)
    chars.forEach {
        println()
        it.forEach {
            print("$it ")
        }
    }
}
class Code130 {

    fun solve(board: Array<CharArray>): Unit {
        val isRead = Array(board.size){Array(board.first().size){false} }
        val pairs = mutableListOf<Pair<Int,Int>>()
        for(i in board.indices){
            for( j in board.first().indices){
                if(!isRead[i][j]&&board[i][j]=='O'){
                    println("$i  $j")
                    expand(board,isRead,pairs,i,j)
                    pairs.forEach {
                        println(" pair ${it.first}   ${it.second}")
                    }
                    change(board,pairs)
                }else{
                    isRead[i][j] = true
                }

            }
        }
    }
    private fun change(board: Array<CharArray>, pairs: MutableList<Pair<Int, Int>>) {
        var needChange = true
        val rLast = board.lastIndex
        val cLast = board.first().lastIndex
        for(i in 0 until pairs.size){
            val pair = pairs[i]
            if(pair.first== rLast||pair.first == 0||pair.second  == cLast ||pair.second == 0){
                needChange = false
                break
            }
        }

        if(needChange){
            pairs.forEach {
                board[it.first][it.second] = 'X'
            }
        }
        pairs.clear()

    }
    private fun expand(board: Array<CharArray>, isRead: Array<Array<Boolean>>,pairs: MutableList<Pair<Int, Int>>,row:Int ,coloum:Int) {

        if(isRead[row][coloum])  return
        else isRead[row][coloum] = true

        if(board[row][coloum] == 'X') return
        else pairs.add(Pair(row,coloum))

        if(row>0){
            expand(board,isRead,pairs,row-1,coloum)
        }
        if(row<board.lastIndex){
            expand(board,isRead,pairs,row+1,coloum)
        }

        if(coloum>0){
            expand(board,isRead,pairs,row,coloum-1)
        }
        if(coloum<board.first().lastIndex){
            expand(board,isRead,pairs,row,coloum+1)
        }
    }


}