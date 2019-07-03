package com.szyz.rock.service.impl;

import com.szyz.rock.mapper.CompanyInfoMapper;
import com.szyz.rock.model.CompanyInfo;
import com.szyz.rock.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyInfoMapper companyInfoMapper;

    @Override
    public CompanyInfo getCompanyInfoById(Integer id) {
        return companyInfoMapper.selectCompanyInfoById(id);
    }

    @Override
    public List<CompanyInfo> getCompanyList() {
        return companyInfoMapper.selectCompanyInfoList();
    }
}
