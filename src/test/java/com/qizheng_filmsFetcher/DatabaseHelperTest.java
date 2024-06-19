package com.qizheng_filmsFetcher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class DatabaseHelperTest {
    private Connection connectionMock;
    private Statement statementMock;
    private PreparedStatement preparedStatementMock;
    private DatabaseHelper dbHelper;

    @BeforeEach
    public void setUp() throws Exception {
        connectionMock = mock(Connection.class);
        statementMock = mock(Statement.class);
        preparedStatementMock = mock(PreparedStatement.class);

        when(connectionMock.createStatement()).thenReturn(statementMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(statementMock.execute(anyString())).thenReturn(true);
        when(preparedStatementMock.executeBatch()).thenReturn(new int[]{1});
        doNothing().when(preparedStatementMock).setString(anyInt(), anyString());
        doNothing().when(preparedStatementMock).addBatch();
        doNothing().when(connectionMock).commit();

        dbHelper = Mockito.spy(DatabaseHelper.class);
        doReturn(connectionMock).when(dbHelper).getConnection();
    }

    @Test
    public void testBatchInsertMovies() {
        List<Movie> movies = Arrays.asList(
                new Movie("Title1", "Genre1", "2021-01-01", "2021", "120", "English", "USA", "Director1", "5", "Description1", "url1"),
                new Movie("Title2", "Genre2", "2021-02-02", "2021", "130", "French", "France", "Director2", "4", "Description2", "url2")
        );
        assertDoesNotThrow(() -> dbHelper.batchInsertMovies(movies, "test_table"));
    }
}
