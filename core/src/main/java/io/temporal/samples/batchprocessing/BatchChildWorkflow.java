package io.temporal.samples.batchprocessing;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import java.util.List;

@WorkflowInterface
public interface BatchChildWorkflow {
  @WorkflowMethod
  void processBatch(List<String> batch);
}
