package com.example.tgbot.utils.db;

import com.example.tgbot.utils.secrets.SecretService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Slf4j
@Configuration
@Profile("default")
@RequiredArgsConstructor
public class DatabasePropertiesService {

    private final SecretService secretService;

    @Bean
    public DataSource configureDB() {
        log.info("Configuring DB");
        var jsonObject = new JSONObject(secretService.getDBSecrets());
        String host = jsonObject.getString("host");
        String port = jsonObject.getString("port");
        String dbname = jsonObject.getString("dbname");

        var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://" + host + ":" + port + "/" + dbname);
        dataSource.setUsername(jsonObject.getString("username"));
        dataSource.setPassword(jsonObject.getString("password"));
        log.info("DB is configured");

        return dataSource;
    }
}
