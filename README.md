# Wizeline Spark

These are my notes and work on the **Data-engineering certification with Spark**. 

## Assignments

The assignments are uploaded into a Google Store bucket. In this bucket you can find a `tar` containing
the code+jar for each assignment. The results are stored as an `<assignment-id>` folder in the same bucket. 

Current assignments:

0. Alimazon
1. Matrices

### Procedure

Once an assignment has been completed:
1. Create the fat-jar: `sbt assembly`
2. Submit the job to dataproc.
3. Create the tar: `tar -cvf assignment-code.tar assignment/*`
4. Upload the tar into google storage. 

### Dataproc

Run the following commands to submit a job into dataproc:
```
gcloud dataproc jobs submit spark --jar path/to/jar --cluster custer-name --region region
```

Where:
```text
    path/to/jar : a path in your local computer of google-storage.
    custer-name : name of the cluster in Google Cloud.
    region      : the region of the cluster (e.g. us-central1)
```

## 
