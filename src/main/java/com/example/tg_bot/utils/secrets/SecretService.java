package com.example.tg_bot.utils.secrets;

import com.example.tg_bot.utils.exceptions.ShopException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Service
public class SecretService {

    @Value("${aws.region}")
    private Region region;

    public String getDBSecrets() {
        return getSecret("shop/postgres");
    }

    public String getBotToken() {
        return getSecret("bot/token");
    }

    private String getSecret(String secretName) {
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();
        String secret;
        try {
            GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();
            GetSecretValueResponse valueResponse = client.getSecretValue(valueRequest);
            secret = valueResponse.secretString();
        } catch (Exception e) {
            throw new ShopException(e.getMessage(), e);
        } finally {
            client.close();
        }

        return secret;
    }
}
