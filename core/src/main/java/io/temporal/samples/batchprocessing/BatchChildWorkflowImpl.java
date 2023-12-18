package io.temporal.samples.batchprocessing;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BatchChildWorkflowImpl implements BatchChildWorkflow {

  private final ActivityOptions options =
      ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(60)).build();
  private final BatchActivities activities =
      Workflow.newActivityStub(BatchActivities.class, options);

  @Override
  public void processBatch(List<String> batch) {
    List<Promise<String>> processRecordPromises = new ArrayList<>();

    for (String record : batch) {
      // process each record asynchronously
      Promise<String> processRecordPromise = Async.function(activities::processRecord, record);
      processRecordPromises.add(processRecordPromise);
    }

    // wait for all records to be processed
    Promise.allOf(processRecordPromises).get();
  }
}
