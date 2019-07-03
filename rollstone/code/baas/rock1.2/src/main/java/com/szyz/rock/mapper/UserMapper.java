package com.szyz.rock.mapper;

import com.szyz.rock.model.User;
import com.szyz.rock.util.Utils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserMapper {

    @Select(value = "SELECT user_id AS userId,user_name AS userName,pwd ,phone,email,ethaddr ethAddr,`level`, company_id AS companyId,create_time createTime ,status FROM user_tbl where user_name = #{userName}")
    User selectUserByUserName(String userName);
    @Select(value = "SELECT user_id AS userId,user_name AS userName ,real_name as realName,phone,email,idcard,idcard_photo AS idcardPhoto,company_apt AS companyApt ,company_name as companyName, birthday,sex,`level`, company_id AS companyId,`status` ,create_time as createTime,addr FROM user_tbl where user_id = #{userId}")
    User selectUserByUserId(Integer userId);
    @SelectProvider(type = UserProvider.class,method = "selectUserInfo")
    User selectUserInfo(User user,int type);
    @Insert("INSERT INTO user_tbl(user_name,pwd,phone,email,`status`,create_time) values(#{userName},#{pwd},#{phone},#{email},#{status},now())")
    int insertUser(User user);
    @Update("UPDATE user_tbl SET phone =#{phone} ,real_name=#{realName} ,email =#{email} ,idcard=#{idcard} , idcard_photo =#{idcardPhoto} ,company_id=#{companyId}, company_apt =#{companyApt} ,company_name=#{companyName}, addr =#{addr} , birthday =#{birthday} , sex =#{sex} ,`level`=#{level} , `status`= #{status} WHERE user_id =#{userId} ")
    int updateUserInfo(User user);
    @Update("update user_tbl set `status` = #{status}  where user_id = #{userId}")
    int updateUserStatus(@Param("status") byte status,@Param("userId") Integer userId);
    @UpdateProvider(type=UserProvider.class ,method = "updateUserInfoSelective")
    int updateUserInfoSelective(User user );
    @Update("UPDATE user_tbl SET phone =#{phone}  ,email =#{email} , addr =#{addr} , birthday =#{birthday} , sex =#{sex}  WHERE user_id =#{userId} ")
    int updateUserInfoNoAuth(User user);
    @Update("UPDATE user_tbl SET real_name=#{realName} ,idcard=#{idcard} , idcard_photo =#{idcardPhoto} ,company_id=#{companyId}, company_apt =#{companyApt} ,company_name=#{companyName}, level`=#{level} WHERE user_id =#{userId} ")
    int updateUserInfoAuth(User user);

    @SelectProvider(type=UserProvider.class, method = "selectUserInfoAuth")
    List<User> selectUserInfoAuth(User user);

    @Select("select user_id userId,user_name userName from user_tbl")
    List<User> selectAllUser();

    class UserProvider{
        public String selectUserInfo(User user,int type){
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT user_id AS userId,user_name AS userName ,real_name as realName,phone,email,idcard,idcard_photo AS idcardPhoto,company_apt AS companyApt ,company_name as companyName, birthday,sex,`level`, company_id AS companyId,`status` ,create_time as createTime FROM user_tbl where ");
            if(type ==0){
                if(user.getUserId()!=null && user.getUserId()!=0){
                    sb.append(" user_id='").append(user.getUserId()).append("'");
                }else if (Utils.isNotBlank(user.getUserName())){
                    sb.append(" user_name = '").append(user.getUserName()).append("'");
                }else if(Utils.isNotBlank(user.getPhone())){
                    sb.append(" phone='").append( user.getPhone()).append("'");
                }else if (Utils.isNotBlank(user.getEmail())){
                    sb.append(" email='").append(user.getEmail()).append("'");
                }
            }else{
                sb.append(" 1!=1 ");
                if(user.getUserId()!=null && user.getUserId()!=0){
                    sb.append(" or user_id='").append(user.getUserId()).append("'");
                }
                if (Utils.isNotBlank(user.getPhone())) {
                    sb.append(" or phone='").append(user.getPhone()).append("'");
                }
                if(Utils.isNotBlank(user.getEmail())){
                    sb.append(" or email='").append(user.getEmail()).append("'");
                }

            }

            return sb.toString();
        }

        public String selectUserInfoAuth(User user){
            /*StringBuffer sb = new StringBuffer();
            sb.append("SELECT user_id AS userId,user_name AS userName ,real_name as realName,phone,email,idcard,idcard_photo AS idcardPhoto,company_apt AS companyApt ,company_name as companyName, birthday,sex,`level`, company_id AS companyId,`status` ,create_time as createTime FROM user_tbl where ");
            sb.append(" status =").append(1);

            if(user.getLevel() == 1){ //超管
                sb.append(" and (level=2 or level=6)");
            }else if(user.getLevel() == 2){
                sb.append(" and (level =3 or level =4 or level = 5)");
                sb.append(" and company_id=" ).append(user.getCompanyId());
            }
            return sb.toString();*/

            StringBuffer sb = new StringBuffer();
            sb.append("SELECT user_id AS userId,user_name AS userName ,real_name as realName,phone,email,idcard,idcard_photo AS idcardPhoto,company_apt AS companyApt ,company_name as companyName, birthday,sex,`level`, company_id AS companyId,`status` ,create_time as createTime ,addr FROM user_temp_info where ");
            sb.append(" status !=").append(3);

            if(user.getLevel() == 1){ //超管
                sb.append(" and (level=2 or level=6)");
            }else if(user.getLevel() == 2){
                sb.append(" and (level =3 or level =4 or level = 5)");
                sb.append(" and company_id=" ).append(user.getCompanyId());
            }
            return sb.toString();
        }

        public String updateUserInfoSelective(User user){
            return new SQL(){
                {
                    UPDATE("user_tbl");
                    if(user.getBirthday() !=null){
                        SET("birthday=#{birthday}");
                    }
                    if (user.getSex()!=0){
                        SET("sex = #{sex}");
                    }
                    if (user.getStatus()!=0){
                        SET("status=#{status}");
                    }
                    if(Utils.isNotBlank(user.getHash())){
                        SET("hash=#{hash}");
                    }
                    if(Utils.isNotBlank(user.getPhone())){
                        SET("phone=#{phone}");
                    }
                    if(Utils.isNotBlank(user.getRealName())){
                        SET("real_name = #{realName}");
                    }
                    if(Utils.isNotBlank(user.getIdcard()))
                        SET("idcard=#{idcard}");
                    if(Utils.isNotBlank(user.getIdcardPhoto()))
                        SET("idcard_photo=#{idcardPhoto}");
                    if(user.getCompanyId()!=null && user.getCompanyId()!=0)
                        SET("company_id=#{companyId}");
                    if(Utils.isNotBlank(user.getAddr()))
                        SET("addr=#{addr}");
                    if(user.getLevel()!=0)
                        SET("level=#{level}");

                    if(Utils.isNotBlank(user.getEthAddr()))
                        SET("ethaddr=#{ethAddr}");

                    WHERE("user_id=#{userId}");
                }
            }.toString();
        }
    }

    @Insert("INSERT INTO user_temp_info (user_id,user_name ,phone,email,real_name,idcard,idcard_photo,company_id,company_name,company_apt,addr,level,status,create_time,sex,birthday)" +
            " values(#{userId},#{userName},#{phone},#{email},#{realName},#{idcard},#{idcardPhoto},#{companyId},#{companyName},#{companyApt},#{addr},#{level},#{status},now(),#{sex},#{birthday})")
    int insertUserTempInfo(User user);

    @Delete("delete from user_temp_info where user_id = #{id}")
    int deleteUserTempInfoById(Integer id);

    @Select(value = "SELECT user_id AS userId,user_name AS userName ,real_name as realName,phone,email,idcard,idcard_photo AS idcardPhoto,company_apt AS companyApt ,company_name as companyName, birthday,sex,`level`, company_id AS companyId,`status` ,create_time as createTime ,addr FROM user_temp_info where user_id = #{userId}")
    User selectUserTempInfoByUserId(Integer userId);
    @Update("update user_temp_info set status = #{status} where user_id = #{id}")
    int updateUserTempInfoStatusById(@Param("status") Integer status ,@Param("id") Integer id);

    @Update("UPDATE user_temp_info SET phone =#{phone} ,real_name=#{realName} ,email =#{email} ,idcard=#{idcard} , idcard_photo =#{idcardPhoto} ,company_id=#{companyId}, company_apt =#{companyApt} ,company_name=#{companyName}, addr =#{addr} , birthday =#{birthday} , sex =#{sex} ,`level`=#{level} , `status`= #{status} WHERE user_id =#{userId} ")
    int updateUserTempInfo(User user);

}
;