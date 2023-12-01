package com.dicoding.appstorysub2.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.appstorysub2.data.local.entity.StoryItemEntity
import com.dicoding.appstorysub2.data.repository.auth.AuthRepository
import com.dicoding.appstorysub2.data.repository.story.StoryRepository
import com.dicoding.appstorysub2.ui.adapter.MainAdapter
import com.dicoding.appstorysub2.util.DataDummy
import com.dicoding.appstorysub2.util.MainDispatcherRule
import com.dicoding.appstorysub2.util.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    @Mock
    private lateinit var authRepository: AuthRepository

    @Test
    fun whenGetStoryShouldNotNullAndReturnData() = runTest {
        val dummyStories = DataDummy.generateDummyStoryEntity()
        val data = PagingData.from(dummyStories)
        val expectedStory = MutableLiveData<PagingData<StoryItemEntity>>()
        expectedStory.value = data

        `when`(storyRepository.getAllStory()).thenReturn(flowOf(data))
        val viewModel = MainViewModel(storyRepository, authRepository)

        val actualStory = viewModel.getAllStory().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = MainAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
        assertEquals(dummyStories[0], differ.snapshot()[0])
    }

    @Test
    fun whenGetAllStoryEmptyShouldReturnNoData() = runTest {
        val data = PagingData.from(emptyList<StoryItemEntity>())
        val expectedStory = MutableLiveData<PagingData<StoryItemEntity>>()
        expectedStory.value = data

        `when`(storyRepository.getAllStory()).thenReturn(flowOf(data))
        val viewModel = MainViewModel(storyRepository, authRepository)

        val actualStory = viewModel.getAllStory().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = MainAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        assertEquals(0, differ.snapshot().size)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {

    override fun onInserted(position: Int, count: Int) {}

    override fun onRemoved(position: Int, count: Int) {}

    override fun onMoved(fromPosition: Int, toPosition: Int) {}

    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}