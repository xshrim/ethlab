package com.szyz.rock.service;

import com.szyz.rock.model.CompanyInfo;

import java.util.List;

public interface CompanyService {

    CompanyInfo getCompanyInfoById(Integer id);
    List<CompanyInfo> getCompanyList();

}
