# Batch Processing Sample

Takes a list of words (words_alpha.txt) and processes them in batches of 50. Each batch is processed in parallel executing child workflows. A maximum of 4 child workflows can be executed in parallel.

## Configuration

The sample is configured by default to connect to a [local Temporal Server](https://docs.temporal.io/cli#starting-the-temporal-server) running on localhost:7233.

To instead connect to Temporal Cloud, set the following environment variables, replacing them with your own Temporal Cloud credentials:

```bash
TEMPORAL_ADDRESS=testnamespace.sdvdw.tmprl.cloud:7233
TEMPORAL_NAMESPACE=testnamespace.sdvdw
TEMPORAL_CERT_PATH="/path/to/file.pem"
TEMPORAL_KEY_PATH="/path/to/file.key"
````

## Run a Workflow

Start a worker:

```bash
./gradlew -q execute -PmainClass=io.temporal.samples.batchprocessing.Worker
```

Heck, run a whole bunch of workers (you'll need a bunch to keep [sync match rate](https://community.temporal.io/t/suggested-metrics-to-autoscale-temporal-workers-on/5870/3) high): 
```bash
for i in {1..10}; do 
    ./gradlew -q execute -PmainClass=io.temporal.samples.batchprocessing.Worker < /dev/null > "temporal_batch_output_$i.txt" 2>&1 &
done
wait
```

Start an execution (the `arg` parameter is the number of records to process):

```bash
# process 20000 records
./gradlew -q execute -PmainClass=io.temporal.samples.batchprocessing.Caller -Parg=20000
```

## Tweak Configuration

`BatchParentWorkflowImpl.java` contains

```
    // process x records in a single batch (parallel activity executions)
    private static final int BATCH_SIZE = 50;

    // run up to x child workflows in parallel
    private static final int WINDOW_SIZE = 4;

    // continue as new every x workflow executions (to keep event history size small)
    private static final int CONTINUE_AS_NEW_THRESHOLD = 500;
```

`Worker.java` contains
```
    // set activities per second across *all* workers
    // prevents resource exhausted errors

    WorkerOptions options =
        WorkerOptions.newBuilder().setMaxTaskQueueActivitiesPerSecond(150).build();
```