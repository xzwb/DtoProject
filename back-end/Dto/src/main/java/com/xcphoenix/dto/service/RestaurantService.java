package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.Restaurant;

import java.io.IOException;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/12 下午9:10
 */
public interface RestaurantService {

    /**
     * 是否是新商家 - 即：有无开店记录
     * @param userId 用户 id
     * @return true or false
     */
    boolean isNewShop(Integer userId);

    /**
     * 店铺名是否可用
     * @param name 店铺名
     * @return 可用情况
     */
    boolean isNameUsable(String name);

    /**
     * 添加店铺
     * @param restaurant 店铺信息
     * @return 店铺
     */
    Restaurant addNewRestaurant(Restaurant restaurant) throws IOException;

}