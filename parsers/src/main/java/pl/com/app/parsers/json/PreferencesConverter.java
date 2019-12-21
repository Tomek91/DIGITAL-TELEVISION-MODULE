package pl.com.app.parsers.json;

import pl.com.app.dto.PreferenceDTO;

import java.util.List;


public class PreferencesConverter extends JsonConverter<List<PreferenceDTO>> {
    public PreferencesConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
