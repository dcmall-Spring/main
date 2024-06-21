package com.dcmall.back.model;

import java.util.List;

public interface SiteDAO {
    List<SiteDTO> SelectSite(int id);
}
