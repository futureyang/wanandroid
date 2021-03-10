package com.future.wanandroid.ui.user.ranking

import com.future.wanandroid.network.ApiTool
import javax.inject.Inject

/**
 * Created by yangqc on 2021/1/28
 *
 */
class RankingRepository @Inject constructor() {
    suspend fun getPointsRank(page: Int) =
        ApiTool.apiService.getPointsRank(page).apiData()
}