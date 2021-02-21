package com.erickson.timeline.model

import android.graphics.Bitmap
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class ImageTargetTest {
    @Test
    fun testImageTargetNotifiesObserversOnBitmapLoaded() {
        var didNotify = false
        val mockCallback = object : ImageTarget.NotifyObserversCallback {
            override fun notifyObservers() {
                didNotify = true
            }
        }

        ImageTarget(mockCallback).onBitmapLoaded(null, null)

        assert(didNotify)
    }

    @Test
    fun testUpdatesImageOnBitmapLoaded() {
        val mockCallback = object : ImageTarget.NotifyObserversCallback {
            override fun notifyObservers() {
            }
        }

        val mockBitmap =
            Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)

        val subject = ImageTarget(mockCallback).also {
            it.onBitmapLoaded(mockBitmap, null)
        }

        assertEquals(mockBitmap, subject.image)
    }
}