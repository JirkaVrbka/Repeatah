package cz.muni.fi.pv239.repeatah.Utility

fun Int.modulo(mod: Int) : Int{
    var res = this % mod
    while(res < 0){
        res += mod
    }

    return res;
}