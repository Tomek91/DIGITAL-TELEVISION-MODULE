package pl.com.app.parsers.json;

import pl.com.app.dto.UserDTO;

import java.util.List;


public class UsersConverter extends JsonConverter<List<UserDTO>> {
    public UsersConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
