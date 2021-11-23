package com.malevdb.SpringConfigurations;

import com.malevdb.Database.DatabaseConnector;
import com.malevdb.Database.SQLExecutor;
import com.malevdb.Utils.PropertyReader;
import com.malevdb.Utils.PropertyType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SQLExecutorConfiguration {
    @Bean
    @Scope("singleton")
    public SQLExecutor sqlExecutor() {
        return new SQLExecutor(DatabaseConnector.getConnection(), PropertyReader.getPropertyValue(PropertyType.DATABASE, "sql.path"));
    }
}
