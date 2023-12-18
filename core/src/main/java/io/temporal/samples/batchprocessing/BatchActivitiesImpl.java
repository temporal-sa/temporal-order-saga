/*
 *  Copyright (c) 2020 Temporal Technologies, Inc. All Rights Reserved
 *
 *  Copyright 2012-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *  Modifications copyright (C) 2017 Uber Technologies, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"). You may not
 *  use this file except in compliance with the License. A copy of the License is
 *  located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 *  or in the "license" file accompanying this file. This file is distributed on
 *  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *  express or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */

package io.temporal.samples.batchprocessing;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchActivitiesImpl implements BatchActivities {
  private static final Logger log = LoggerFactory.getLogger(BatchActivitiesImpl.class);

  @Override
  public List<String> createSingleBatch(int batchSize, int readUntilLine, int offset) {
    FileWordReader fileWordReader = new FileWordReader();
    List<String> records =
        fileWordReader.readWordsFromFile("words_alpha.txt", readUntilLine, offset);

    if (offset > readUntilLine || records.isEmpty()) {
      return new ArrayList<>(); // Return an empty list if offset is beyond readUntilLine or no
      // records
    }

    int start =
        0; // Start from the first record (offset is already considered in readWordsFromFile)
    int end =
        Math.min(
            batchSize,
            records.size()); // Ensure not to exceed the batchSize and the size of records
    log.info("Creating a batch of size {}", end - start);

    return new ArrayList<>(records.subList(start, end));
  }

  @Override
  public List<String> processBatch(List<String> batch) {
    log.info("Processing batch of size {}", batch.size());
    for (String record : batch) {
      log.info("Processing record {}", record);
      // turn record into uppercase
      // sleep for 10ms
      try {
        int delayMs = 10;

        // 20% chance of delayMs being 1000ms
        if (Math.random() < 0.05) {
          delayMs = 1000;
        }

        Thread.sleep(delayMs);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      batch.set(batch.indexOf(record), record.toUpperCase());
    }
    return batch;
  }

  public String processRecord(String record) {
    log.info("Processing record {}", record);
    // turn record into uppercase
    // sleep for 10ms
    try {
      int delayMs = 10;

      // 20% chance of delayMs being 1000ms
      if (Math.random() < 0.05) {
        delayMs = 1000;
      }

      Thread.sleep(delayMs);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return record.toUpperCase();
  }
}
