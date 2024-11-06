package com.act.Gakos.dto.address;

public class KebeleSearchDto {
    private String woredaName;
    private String kebeleName;
    private String zoneName;
    private String regionName;
    private Long countryId;

    public KebeleSearchDto(String woredaName, String kebeleName, String zoneName, String regionName, Long countryId) {
        this.woredaName = woredaName;
        this.kebeleName = kebeleName;
        this.zoneName = zoneName;
        this.regionName = regionName;
        this.countryId = countryId;
    }



    // Getters and Setters
}
