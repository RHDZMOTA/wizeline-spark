# Wizeline Spark

These are my notes and work on the **Data-engineering certification with Spark** by Wizeline Academy. 

## Assignments

The assignments are uploaded into a Google Store bucket. In this bucket you can find a `tar.gz` :warchive 
containing the code+jar for each assignment. The output of the assignments are stored in the same bucket
with the format `assigment-<i>-<id>-<j>` where `i` is the assignment number, `id` is an optional 
string identifier, and `j` is the attempt. 

Current assignments:

0. assignment-0: products in alimazon
1. assignment-1: matrix multiplication
2. assignment-2: business report
    * Best selling products by gross sales and orders
    * Best customers by gross spending and orders
3. assignment-3: dataset and dataframe operations
    * Best selling hours
    * Monthly discount
    * Client/Orders Distribution
4. assignment-5: partitioning exploring
5. assignment-6: a full-fledged report for Alimazon
6. assignment-7: join operations and shared variables
8. assignment-8: images metadata

## Procedure

Once an assignment has been completed:
1. Create the jar in the root directory of the assignment..
    * `sbt assembly`
2. Submit the job to dataproc.
    * `gcloud dataproc jobs submit spark --jar <path/to/jar> --cluster <name> --region <region>`
3. Create the `tar.gz` file for the root directory of this repo.. 
    * `tar -zcvf assignment-<i>-code-attempt-<j>.tar.gz assignment-<i>/*`
    * Where `i` is the assignment number and `j` is the attempt number.
4. Upload the tar.gz into google storage.
    * `gsutil cp path/to/tar.gz gs://de-training-output-rhdzmota`

### Dataproc

Run the following commands to submit a job into dataproc:
```bash
gcloud dataproc jobs submit spark --jar <path/to/jar> --cluster <custer-name> --region <region>
```

Where:
```text
    <path/to/jar> : is a relative path to the jar or a Google Storage reference.
    <custer-name> : name of the cluster in Google Cloud.
    <region>      : the region of the cluster (e.g. us-central1)
```

### Create a tar.gz archive

You can create a `tar.gz` archive for the contents of a given folder by running the following command:
```bash
tar -zcvf <archive-name>.tar.gz <source-folder>/*
```

Where:
```text
    <archive-name>  : is the name of the resulting tar.gz archive. 
    <source-folder> : is a relative path to the commad of the source folder.
```

### Upload a file to Google Storage

Use the following command to upload a file to Google Storage:
```bash
gsutil cp <path/to/file> gs://<bucket-name>
```

Where:
```text
    <path/to/file> : is a relative path to the file location.
    <bucket-name>  : is the name of the bucket in Google Cloud. 
```

## Authors

This is the original work of [Rodrigo Hernández Mota](https://www.linkedin.com/in/rhdzmota/) for the **Big Data Engineering with Spark** program. 


