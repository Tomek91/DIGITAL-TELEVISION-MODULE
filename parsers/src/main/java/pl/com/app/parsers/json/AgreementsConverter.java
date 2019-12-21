package pl.com.app.parsers.json;

import pl.com.app.dto.AgreementDTO;

import java.util.List;


public class AgreementsConverter extends JsonConverter<List<AgreementDTO>> {
    public AgreementsConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
