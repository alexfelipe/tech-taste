package br.com.alura.techtaste

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() = runBlocking {
        val words = listOf<String>("alex, ", "felipe, ", "dsadh, dasjkdh aksd,, ")
        println(words.joinToString(separator = ""))
        assertEquals(4, 2 + 2)
    }
}