package com.fastcampus.housebatch.core.service;

import com.fastcampus.housebatch.core.dto.AptDealDto;
import com.fastcampus.housebatch.core.entity.Apt;
import com.fastcampus.housebatch.core.entity.AptDeal;
import com.fastcampus.housebatch.core.repository.AptDealRepository;
import com.fastcampus.housebatch.core.repository.AptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AptDealDto를 Apt, AptDeal 엔티티로 전환한다.
 * */
@Service
@RequiredArgsConstructor
public class AptDealService {

    private final AptRepository aptRepository;
    private final AptDealRepository aptDealRepository;

    @Transactional
    public void upsert(AptDealDto aptDealDto) {
        Apt apt = getAptOrNew(aptDealDto);
        aptRepository.save(apt);

        AptDeal aptDeal = getAptDealOrNew(apt, aptDealDto);
        updateOrSaveAptDeal(aptDeal, aptDealDto);
    }

    private Apt getAptOrNew(AptDealDto aptDealDto) {
        return aptRepository.findByAptNameAndJibun(aptDealDto.getAptName(), aptDealDto.getJibun())
                .orElseGet(() -> Apt.from(aptDealDto));
    }

    private AptDeal getAptDealOrNew(Apt apt, AptDealDto aptDealDto) {
        return aptDealRepository.findByAptAndExclusiveAreaAndDealDateAndDealAmountAndFloor(
                apt, aptDealDto.getExclusiveArea(), aptDealDto.getDealDate(), aptDealDto.getDealAmount(), aptDealDto.getFloor()
        ).orElseGet(() -> AptDeal.of(aptDealDto, apt));
    }

    private void updateOrSaveAptDeal(AptDeal aptDeal, AptDealDto aptDealDto) {
        aptDeal.setDealCanceled(aptDealDto.isDealCanceled());
        aptDeal.setDealCanceledDate(aptDealDto.getDealCanceledDate());
        aptDealRepository.save(aptDeal);
    }

}
