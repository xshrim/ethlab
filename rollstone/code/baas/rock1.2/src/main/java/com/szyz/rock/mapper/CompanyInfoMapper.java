package com.szyz.rock.mapper;

import com.szyz.rock.model.CompanyInfo;
import com.szyz.rock.util.Utils;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CompanyInfoMapper {
    @Select("SELECT id,name,apt,`status`,create_time AS createTime FROM company_info where id=#{id}")
    CompanyInfo selectCompanyInfoById(Integer id);
    @Insert("INSERT INTO company_info (name,apt,status ,create_time) VALUES (#{name},#{apt},#{status},now())")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int insertCompanyInfo(CompanyInfo companyInfo);
    @Update("UPDATE company_info SET status = #{status} WHERE id = #{id}")
    int updateCompanyStatus(@Param("status") byte status,@Param("id") Integer id);
    @Select("select id,name from company_info where status = 1")
    List<CompanyInfo> selectCompanyInfoList();
    @Update("update company_info set name=#{name},apt =#{apt} where id = #{id}")
    int updateCompanyInfoById(CompanyInfo companyInfo);

    class CompanyInfoProvider{

       /* public String selectCompanyInfo(Integer id ,String name){
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT id,name,apt,`status`,create_time AS createTime FROM company_info where ");
            if(id !=null && id !=0){
                sb.append(" id=").append(id);
            }else if(Utils.isNotBlank(name)){
                sb.append(" name='").append(name).append("'");
            }
            return null;
        }*/
    }

}
