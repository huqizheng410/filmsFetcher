package com.qizheng_filmsFetcher;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class DynamoDBHelperTest {
    private DynamoDbClient dynamoDbClientMock = mock(DynamoDbClient.class);

    @Test
    public void testBatchWriteDataToDynamoDB() {
        List<Movie> movies = Arrays.asList(
                new Movie("Title1", "Genre1", "2021-01-01", "2021", "120", "English", "USA", "Director1", "5", "Description1", "url1"),
                new Movie("Title2", "Genre2", "2021-02-02", "2021", "130", "French", "France", "Director2", "4", "Description2", "url2")
        );

        assertDoesNotThrow(() -> DynamoDBHelper.batchWriteDataToDynamoDB(movies));
    }
}
