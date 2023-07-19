package com.parking.project.parkinglot.service;

import com.parking.project.parkinglot.entity.ParkingLot;
import com.parking.project.parkinglot.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingLotRepositoryService {
    private final ParkingLotRepository parkingLotRepository;

    // self invocation test
    public void bar(List<ParkingLot> parkingLotList) {
        log.info("bar CurrentTransactionName: " + TransactionSynchronizationManager.getCurrentTransactionName());
        foo(parkingLotList);
    }

    // self invocation test
    @Transactional
    public void foo(List<ParkingLot> parkingLotList) {
        log.info("foo CurrentTransactionName: " + TransactionSynchronizationManager.getCurrentTransactionName());
        parkingLotList.forEach(parkingLot -> {
            parkingLotRepository.save(parkingLot);
            throw new RuntimeException("error"); // 예외 발생
        });
    }

    // read only test
    @Transactional(readOnly = true)
    public void startReadOnlyMethod(Long id) {
        parkingLotRepository.findById(id).ifPresent(parkingLot ->
                parkingLot.changeParkingLotAddress("서울 송파구"));
    }

    @Transactional
    public void updateAddress(Long id, String address) {
        ParkingLot entity = parkingLotRepository.findById(id).orElse(null);

        if (Objects.isNull(entity)) {
            log.error("[ParkingLotRepositoryService updateAddress] not found id: {}", id);
            return;
        }

        entity.changeParkingLotAddress(address);
    }

    // for test
    public void updateAddressWithoutTransaction(Long id, String address) {
        ParkingLot entity = parkingLotRepository.findById(id).orElse(null);

        if (Objects.isNull(entity)) {
            log.error("[ParkingLotRepositoryService updateAddress] not found id: {}", id);
            return;
        }

        entity.changeParkingLotAddress(address);
    }

    @Transactional(readOnly = true)
    public List<ParkingLot> findAll() {
        return parkingLotRepository.findAll();
    }

}
