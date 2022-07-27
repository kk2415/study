package com.fastcampus.housebatch.core.service;

import com.fastcampus.housebatch.core.entity.Lawd;
import com.fastcampus.housebatch.core.repository.LawdRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LawdService {

    private final LawdRepository lawdRepository;

    @Transactional
    public void upsert(Lawd lawd) {
        Lawd findLawd = lawdRepository.findByLawdCd(lawd.getLawdCd())
                .orElseGet(Lawd::new);

        findLawd.setLawdCd(lawd.getLawdCd());
        findLawd.setLawdDong(lawd.getLawdDong());
        findLawd.setExist(lawd.getExist());

        lawdRepository.save(findLawd);
        System.out.println(findLawd);
    }

}
