# file-upload-download

# Description:
This is a spring boot microservice that performs full CRUD operations to a MinIO Object Store using the MinIO Java API. 
The operations are:
1. Read all available Buckets
2. Read all available objects per Bucket
3. Upload object to specific Bucket
4. Delete object from specific Bucket

The microservice comes with its Dockerfile and Docker-Compose.

# Dependencies:
1. Java 11
2. Spring Boot
3. Docker

# Instructions to run the program:
1. Clone the project from: https://github.com/gdimitriou/file-upload-download.git
2. Import it to your favorite IDE
3. Download the dependencies
4. Run the command: "docker-compose up --build"
5. Open FileUploadDownload.html and enjoy!


