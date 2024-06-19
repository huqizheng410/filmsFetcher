package com.qizheng_filmsFetcher;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamoDBHelper {
    // Set DynamoDB connection details
    // Similarly, in future the sensitive data will move to .env file
    private static final String DYNAMODB_TABLE_NAME = "oliver_assessment";
    private static final String DYNAMODB_REGION = "us-east-1";
    private static final String ACCESS_KEY_ID = "AKIA6MA3ZTWL4KZDJ7MG";
    private static final String SECRET_ACCESS_KEY = "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";

    // Method to create a DynamoDB client
    public static DynamoDbClient createDynamoDbClient() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(ACCESS_KEY_ID, SECRET_ACCESS_KEY);
        return DynamoDbClient.builder()
                .region(Region.of(DYNAMODB_REGION))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    // Method to ensure the specified table exists in DynamoDB
    public static void ensureTableExists(DynamoDbClient dynamoDbClient) {
        try {
            DescribeTableRequest describeTableRequest = DescribeTableRequest.builder()
                    .tableName(DYNAMODB_TABLE_NAME)
                    .build();
            dynamoDbClient.describeTable(describeTableRequest);
            System.out.println("Table " + DYNAMODB_TABLE_NAME + " already exists.");
        } catch (ResourceNotFoundException e) {
            System.out.println("Table " + DYNAMODB_TABLE_NAME + " does not exist. Creating now...");
            CreateTableRequest createTableRequest = CreateTableRequest.builder()
                    .tableName(DYNAMODB_TABLE_NAME)
                    .keySchema(KeySchemaElement.builder()
                            .attributeName("elcinema_id")
                            .keyType(KeyType.HASH)
                            .build())
                    .attributeDefinitions(AttributeDefinition.builder()
                            .attributeName("elcinema_id")
                            .attributeType(ScalarAttributeType.S)
                            .build())
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                    .build();

            dynamoDbClient.createTable(createTableRequest);
            System.out.println("Table " + DYNAMODB_TABLE_NAME + " created successfully.");
        }
    }

    // Method to batch write movie data to DynamoDB
    public static void batchWriteDataToDynamoDB(List<Movie> movies) {
        DynamoDbClient dynamoDbClient = createDynamoDbClient();
        ensureTableExists(dynamoDbClient);

        List<WriteRequest> writeRequests = new ArrayList<>();
        for (Movie movie : movies) {
            Map<String, AttributeValue> item = new HashMap<>();

            // Extracting ID from the URL
            String elcinemaId = extractIdFromUrl(movie.getUrl());
            item.put("elcinema_id", AttributeValue.builder().s(elcinemaId).build());
            item.put("title", AttributeValue.builder().s(movie.getTitle()).build());

            // Adding movie attributes if not "NA"
            if (!movie.getGenre().equals("NA")) item.put("genre", AttributeValue.builder().s(movie.getGenre()).build());
            if (!movie.getReleaseDate().equals("NA")) item.put("releaseDate", AttributeValue.builder().s(movie.getReleaseDate()).build());
            if (!movie.getReleaseYear().equals("NA")) item.put("releaseYear", AttributeValue.builder().s(movie.getReleaseYear()).build());
            if (!movie.getDuration().equals("NA")) item.put("duration", AttributeValue.builder().s(movie.getDuration()).build());
            if (!movie.getLanguage().equals("NA")) item.put("language", AttributeValue.builder().s(movie.getLanguage()).build());
            if (!movie.getCountry().equals("NA")) item.put("country", AttributeValue.builder().s(movie.getCountry()).build());
            if (!movie.getDirector().equals("NA")) item.put("director", AttributeValue.builder().s(movie.getDirector()).build());
            if (!movie.getRating().equals("NA")) item.put("rating", AttributeValue.builder().s(movie.getRating()).build());
            if (!movie.getDescription().equals("NA")) item.put("description", AttributeValue.builder().s(movie.getDescription()).build());

            writeRequests.add(WriteRequest.builder().putRequest(PutRequest.builder().item(item).build()).build());

            // Batch write to DynamoDB in chunks of 25, adjust according to the system / performance
            if (writeRequests.size() == 25) {
                Map<String, List<WriteRequest>> requestItems = new HashMap<>();
                requestItems.put(DYNAMODB_TABLE_NAME, new ArrayList<>(writeRequests));
                BatchWriteItemRequest batchWriteItemRequest = BatchWriteItemRequest.builder()
                        .requestItems(requestItems)
                        .build();
                try {
                    dynamoDbClient.batchWriteItem(batchWriteItemRequest);
                    System.out.println("Batch written to DynamoDB");
                } catch (Exception e) {
                    System.err.println("Failed to batch write items to DynamoDB: " + e.getMessage());
                }
                writeRequests.clear();
            }
        }

        // Write remaining items if any
        if (!writeRequests.isEmpty()) {
            Map<String, List<WriteRequest>> requestItems = new HashMap<>();
            requestItems.put(DYNAMODB_TABLE_NAME, new ArrayList<>(writeRequests));
            BatchWriteItemRequest batchWriteItemRequest = BatchWriteItemRequest.builder()
                    .requestItems(requestItems)
                    .build();
            try {
                dynamoDbClient.batchWriteItem(batchWriteItemRequest);
                System.out.println("Batch written to DynamoDB");
            } catch (Exception e) {
                System.err.println("Failed to batch write items to DynamoDB: " + e.getMessage());
            }
        }
    }

    // Method to extract ID from the URL
    private static String extractIdFromUrl(String url) {
        String[] parts = url.split("/");
        return parts[parts.length - 1];
    }
}
