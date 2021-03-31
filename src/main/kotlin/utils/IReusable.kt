package utils

interface IReusable : IRestartable {
    fun checkFinalState()
}