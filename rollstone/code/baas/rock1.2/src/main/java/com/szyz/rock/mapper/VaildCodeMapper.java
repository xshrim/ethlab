package com.szyz.rock.mapper;

import com.szyz.rock.model.VaildCode;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface VaildCodeMapper {

    @Select("SELECT `code` FROM vaild_code WHERE sms = #{v} ORDER BY create_time DESC limit 1")
    String selectVaildCode(String v);

    @Insert("INSERT INTO vaild_code (sms,code,create_time) VALUES(#{sms},#{code},now())")
    int insertVaildCode(VaildCode vaildCode);
}
