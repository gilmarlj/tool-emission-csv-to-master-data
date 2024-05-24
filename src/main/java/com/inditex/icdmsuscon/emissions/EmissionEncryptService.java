package com.inditex.icdmsuscon.emissions;
import com.inditex.icdmsuscon.emissions.csv.EmissionEntityCsvParser;
import com.inditex.icdmsuscon.emissions.entity.EmissionEntity;
import com.inditex.icdmsuscon.emissions.security.CryptoConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmissionEncryptService {

    private final CryptoConverter cryptoConverter;

   
    public EmissionEncryptService(CryptoConverter cryptoConverter) {
        this.cryptoConverter = cryptoConverter;
    }

    public List<String> generateInsertStatements(String csvFilePath) throws Exception {
        List<EmissionEntity> emissionEntities = EmissionEntityCsvParser.readCsv(csvFilePath);
        List<String> insertStatements = new ArrayList<>();

        for (EmissionEntity entity : emissionEntities) {
            String insertStatement = createInsertStatement(entity);
            insertStatements.add(insertStatement);
        }

        return insertStatements;
    }

    private String createInsertStatement(EmissionEntity entity) throws Exception {
        String encryptedFactor = cryptoConverter.encrypt(entity.getFactor().toString());
        return String.format(
                "INSERT INTO icdmsuscon.emissions (id, market_id, unit_id, factor) VALUES ('%s', '%s', '%s', '%s');",
                UUID.randomUUID(), entity.getMarketId(), entity.getUnitId(), encryptedFactor
        );
    }
}
