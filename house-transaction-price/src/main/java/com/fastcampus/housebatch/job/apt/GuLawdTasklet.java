package com.fastcampus.housebatch.job.apt;

import com.fastcampus.housebatch.core.repository.LawdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.List;

/**
 * ExecutionContext에 저장할 데이터
 * 1. guLawdCdList
 * 2. guLawdCd
 * 3. itemCount - 남아있는 구 코드의 개수
 * */
@RequiredArgsConstructor
public class GuLawdTasklet implements Tasklet {

    private static final String KEY_GU_LAWD_CD = "guLawdCd";
    private static final String KEY_ITEM_COUNT = "itemCount";
    private static final String KEY_GU_LAWD_CD_LIST = "guLawdCdList";

    private final LawdRepository lawdRepository;
    private List<String> guLawdCdList;
    private int itemCount;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ExecutionContext executionContext = getExecutionContext(chunkContext);

        //데이터가 있으면 다음 스텝을 실행하고, 데이터가 없으면 종료되도록 한다.
        //데이터가 있으면 -> CONTINUABLE
        initGuLawdCdList(executionContext);
        initItemCount(executionContext);

        if (itemCount == 0) {
            contribution.setExitStatus(ExitStatus.COMPLETED);
            return RepeatStatus.FINISHED;
        }

        itemCount--;
        executionContext.putInt(KEY_ITEM_COUNT, itemCount);
        executionContext.putString(KEY_GU_LAWD_CD, guLawdCdList.get(itemCount));

        contribution.setExitStatus(new ExitStatus("CONTINUABLE"));
        return RepeatStatus.FINISHED;
    }

    private ExecutionContext getExecutionContext(ChunkContext chunkContext) {
        //chunkContext.getStepContext().getJobExecutionContext() --> 이 JobExecutionContext은 ReadOnly

        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        return stepExecution.getJobExecution().getExecutionContext();
    }

    private void initGuLawdCdList(ExecutionContext executionContext) {
        if (executionContext.containsKey(KEY_GU_LAWD_CD_LIST)) {
            guLawdCdList = (List<String>) executionContext.get(KEY_GU_LAWD_CD_LIST);
        } else {
            guLawdCdList = lawdRepository.findDistinctGuLawdCd();
            executionContext.put(KEY_GU_LAWD_CD_LIST, guLawdCdList);
            executionContext.putInt(KEY_ITEM_COUNT, guLawdCdList.size());
        }
    }

    private void initItemCount(ExecutionContext executionContext) {
        if (executionContext.containsKey(KEY_ITEM_COUNT)) {
            itemCount = executionContext.getInt(KEY_ITEM_COUNT);
        } else {
            itemCount = guLawdCdList.size();
        }
    }
}
