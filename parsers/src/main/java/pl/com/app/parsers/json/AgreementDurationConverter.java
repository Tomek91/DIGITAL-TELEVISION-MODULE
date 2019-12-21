package pl.com.app.parsers.json;

import pl.com.app.dto.AgreementDurationDTO;

import java.util.List;


public class AgreementDurationConverter extends JsonConverter<List<AgreementDurationDTO>> {
    public AgreementDurationConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
