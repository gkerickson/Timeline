package com.erickson.timeline

import androidx.lifecycle.MutableLiveData
import com.erickson.timeline.model.DataViewModel
import com.erickson.timeline.smithsonian.RequestHandler
import com.erickson.timeline.smithsonian.RequestHandlerImpl.DataRequestCallback
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.mockito.stubbing.Answer

class MockitoInstrumentedTest {

    private val testLiveData = MutableLiveData<String>()

    fun getCallIfValueIsNull(handler: RequestHandler) {
        if(testLiveData.value == null) {
            handler.getData(object : DataRequestCallback() {
                override fun withData(data: Map<String, DataViewModel.ViewData>) {
                    testLiveData.value = "YAY"
                }
            })
        }
    }

//    @Test
//    fun testGetCallIfValueIsNull() {
//        testLiveData.value = null
//        val handler: RequestHandler = mock {
//            on { getData(any()) } doAnswer Answer<Void> { invocation ->
//                val callback: DataRequestCallback = invocation!!.arguments[0] as DataRequestCallback
//                callback.onResponse(mock(), mock {
//                    on { body() } doReturn null
//                })
//                null
//            }
//        }
//    }

//    private fun setupGetIdsWithBodyMock(mockBody: RequestDefinitions.Body<RequestDefinitions.SearchData>) {
//        val call = mock<Call<RequestDefinitions.Body<RequestDefinitions.SearchData>>> {}
//        val mockResponse = mock<Response<RequestDefinitions.Body<RequestDefinitions.SearchData>>> {
//            on { body() } doReturn mockBody
//        }
//
//        doAnswer {
//            (it.arguments[0] as? Callback<RequestDefinitions.Body<RequestDefinitions.SearchData>>)?.onResponse(
//                call, mockResponse
//            )
//        }.`when`(call).enqueue(any())
//
//        Mockito.`when`(smith.getIds(Smithsonian.apiKey, Smithsonian.query))
//            .thenReturn(call)
//    }


}