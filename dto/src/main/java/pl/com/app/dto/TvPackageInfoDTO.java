package pl.com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TvPackageInfoDTO {
    private TvPackageDTO tvPackageDTO;
    private List<ChannelDTO> channelsDTO;
}





