package com.erickson.timeline

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.erickson.timeline.smithsonian.RequestHandlerImpl.parseDate
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.erickson.timeline", appContext.packageName)
    }

    @Test
    fun testParseDate() {
        val dateString = "2000s"

        val date: Date = parseDate(dateString) ?: Date()
        val expectedDate = Calendar.getInstance().run {
            set(2000, 0, 1, 0, 0, 0)
            time
        }

        assertEquals(expectedDate.toString(), date.toString())
    }
}