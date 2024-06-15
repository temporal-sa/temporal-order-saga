# Order Saga Sample

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
./gradlew -q execute -PmainClass=io.temporal.samples.ordersaga.Worker
```

```bash
./gradlew -q execute -PmainClass=io.temporal.samples.ordersaga.splittrafficsubtract.Caller
```

## How to trigger Saga compensations
* Uncomment the exceptions in OrderActivitiesImpl to see how the Saga handles failures
