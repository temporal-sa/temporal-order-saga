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

Start an execution (the `arg` parameter is the number of records to process):

```bash
# process 20000 records
./gradlew -q execute -PmainClass=io.temporal.samples.batchprocessing.Caller -Parg=20000
```
