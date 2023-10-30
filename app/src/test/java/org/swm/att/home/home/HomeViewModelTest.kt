package org.swm.att.home.home

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.repository.AttMenuRepository
import org.swm.att.domain.repository.AttOrderRepository
import org.swm.att.domain.repository.AttPosUserRepository
import org.swm.att.home.main.home.HomeViewModel

class HomeViewModelTest {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var tempMenuItem: OrderedMenuVO

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    
    @Before
    fun setUp() {
        val menuRepositoryMock = mockk<AttMenuRepository>()
        val orderRepositoryMock = mockk<AttOrderRepository>()
        val userRepositoryMock = mockk<AttPosUserRepository>()
        val context = mockk<Context>()

        homeViewModel = HomeViewModel(menuRepositoryMock, userRepositoryMock, orderRepositoryMock, context)
        tempMenuItem = OrderedMenuVO(
            id = 1,
            name = "test",
            price = 1,
            count = 3,
            options = listOf()
        )
    }

    @Test
    fun `setSelectedMenusVO, be same count of selectedMenu and menuMap of value` ()  {
        val testSelectedMenusVO = OrderedMenusVO(
            menus = mutableListOf(
                tempMenuItem
            )
        )

        homeViewModel.setSelectedMenusVO(testSelectedMenusVO)
        val testKey = testSelectedMenusVO.menus?.get(0)
        assertEquals(homeViewModel.selectedMenuMap.value?.get(testKey), testKey?.count)
    }

    @Test
    fun `addSelectedMenu, check added`() {
        homeViewModel.addSelectedMenu(tempMenuItem)
        assertEquals(homeViewModel.selectedMenuMap.value?.get(tempMenuItem), 1)
    }

    @Test
    fun `minusSelectedMenuItem, check decreased`() {
        homeViewModel.addSelectedMenu(tempMenuItem)
        homeViewModel.minusSelectedMenuItem(tempMenuItem)
        assertEquals(homeViewModel.selectedMenuMap.value?.get(tempMenuItem), null)
    }

    @Test
    fun `plusSelectedMenuItem, check increased`() {
        homeViewModel.addSelectedMenu(tempMenuItem)
        assertEquals(homeViewModel.selectedMenuMap.value?.get(tempMenuItem), 1)
    }

    @Test
    fun `deletedAllMenuItem, check deleted all`() {
        homeViewModel.addSelectedMenu(tempMenuItem)
        homeViewModel.deletedAllMenuItem()
        assertEquals(homeViewModel.selectedMenuMap.value?.keys?.size, 0)
    }

    @Test
    fun `getOrderedMenusVO, check orderedMenusVO`() {
        homeViewModel.addSelectedMenu(tempMenuItem)
        val result = homeViewModel.getOrderedMenusVO()
        assertEquals(result.menus?.get(0), tempMenuItem)
    }

}