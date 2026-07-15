package com.example.pocketinput

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pocketinput.network.Discovery
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@RunWith(AndroidJUnit4::class)
class DiscoveryTest {
    @Test
    fun discoverReceiver() = runBlocking {
        val receiver = withContext(Dispatchers.IO) {
            Discovery.findReceiver(5678)
        }
        println(receiver)
        assertNotNull(receiver)
    }
}