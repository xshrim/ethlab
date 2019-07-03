package com.szyz.rock.mapper;

import com.szyz.rock.model.Perm;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Component;

@Component
public interface PermMapper {
    @Insert("insert into perm_info(id,att) values (#{id},#{device})")
    int insertPerm(Perm perm);
}
