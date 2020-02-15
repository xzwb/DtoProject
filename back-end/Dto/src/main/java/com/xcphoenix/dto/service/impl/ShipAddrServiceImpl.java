package com.xcphoenix.dto.service.impl;

import ch.hsr.geohash.GeoHash;
import com.xcphoenix.dto.bean.dao.ShipAddr;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.mapper.ShipAddrMapper;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.service.GeoService;
import com.xcphoenix.dto.service.ShipAddrService;
import com.xcphoenix.dto.utils.ContextHolderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/9/3 下午10:00
 */
@Service
public class ShipAddrServiceImpl implements ShipAddrService {

    private ShipAddrMapper shipAddrMapper;
    private GeoService geoService;
    private int precision = 12;

    @Autowired
    public ShipAddrServiceImpl(ShipAddrMapper shipAddrMapper, GeoService geoService) {
        this.shipAddrMapper = shipAddrMapper;
        this.geoService = geoService;
    }

    @Override
    public ShipAddr addShipAddr(ShipAddr shipAddr) {
        // 获取 countryId
        String cityCode = geoService.getAreaCode(shipAddr.getAddrLat(), shipAddr.getAddrLng());
        shipAddr.setCountryCode(cityCode);
        // 获取 geoHash
        GeoHash geoHash = GeoHash.withCharacterPrecision(
                shipAddr.getAddrLat().doubleValue(), shipAddr.getAddrLat().doubleValue(), precision);
        shipAddr.setGeohash(geoHash.toBase32());
        shipAddrMapper.addShipAddr(shipAddr);
        return shipAddr.dataConvert();
    }

    @Override
    public void delShipAddr(Long shipAddrId) {
        Long userId = ContextHolderUtils.getLoginUserId();
        if (shipAddrMapper.delShipAddr(shipAddrId, userId) != 1) {
            throw new ServiceLogicException(ErrorCode.ADDR_NOT_FOUND);
        }
    }

    @Override
    public ShipAddr updateShipAddr(ShipAddr shipAddr) {
        shipAddr.setUserId(ContextHolderUtils.getLoginUserId());
        if (shipAddr.getAddrLat() != null && shipAddr.getAddrLng() != null) {
            GeoHash geoHash = GeoHash.withCharacterPrecision(
                    shipAddr.getAddrLat().doubleValue(), shipAddr.getAddrLat().doubleValue(), precision);
            shipAddr.setGeohash(geoHash.toBase32());

            String cityCode = geoService.getAreaCode(shipAddr.getAddrLat(), shipAddr.getAddrLng());
            shipAddr.setCountryCode(cityCode);
        }
        shipAddrMapper.updateShipAddr(shipAddr);
        return getAddrMsgById(shipAddr.getShipAddrId());
    }

    @Override
    public ShipAddr getAddrMsgById(Long shipAddrId) {
        Long userId = ContextHolderUtils.getLoginUserId();
        ShipAddr shipAddr = shipAddrMapper.getAddrById(shipAddrId, userId);
        if (shipAddr == null) {
            throw new ServiceLogicException(ErrorCode.ADDR_NOT_FOUND);
        }
        return shipAddr.dataConvert();
    }

    @Override
    public List<ShipAddr> getAddresses() {
        Long userId = ContextHolderUtils.getLoginUserId();
        List<ShipAddr> shipAddrList = shipAddrMapper.getAddrByUserId(userId);
        shipAddrList.forEach(shipAddr -> shipAddr = shipAddr.dataConvert());
        return shipAddrList;
    }

    @Override
    public ShipAddr getDefaultAddress() {
        Long userId = ContextHolderUtils.getLoginUserId();
        return shipAddrMapper.getDefaultAddr(userId);
    }

}
