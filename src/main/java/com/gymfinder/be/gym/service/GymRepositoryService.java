package com.gymfinder.be.gym.service;

import com.gymfinder.be.gym.entity.Gym;
import com.gymfinder.be.gym.repository.GymRepository;
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
public class GymRepositoryService {
    private final GymRepository gymRepository;

    // self invocation test
    public void bar(List<Gym> gymList) {
        log.info("bar CurrentTransactionName: " + TransactionSynchronizationManager.getCurrentTransactionName());
        foo(gymList);
    }

    // self invocation test
    @Transactional
    public void foo(List<Gym> gymList) {
        log.info("foo CurrentTransactionName: " + TransactionSynchronizationManager.getCurrentTransactionName());
        gymList.forEach(gym -> {
            gymRepository.save(gym);
            throw new RuntimeException("error"); // 예외 발생
        });
    }

    // read only test
    @Transactional(readOnly = true)
    public void startReadOnlyMethod(Long id) {
        gymRepository.findById(id).ifPresent(gym ->
                gym.changeGymAddress("서울 송파구"));
    }

    @Transactional
    public void updateAddress(Long id, String address) {
        Gym entity = gymRepository.findById(id).orElse(null);

        if (Objects.isNull(entity)) {
            log.error("[GymRepositoryService updateAddress] not found id: {}", id);
            return;
        }

        entity.changeGymAddress(address);
    }

    // for test
    public void updateAddressWithoutTransaction(Long id, String address) {
        Gym entity = gymRepository.findById(id).orElse(null);

        if (Objects.isNull(entity)) {
            log.error("[GymRepositoryService updateAddress] not found id: {}", id);
            return;
        }

        entity.changeGymAddress(address);
    }

    @Transactional(readOnly = true)
    public List<Gym> findAll() {
        return gymRepository.findAll();
    }

}
