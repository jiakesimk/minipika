package mapper

import kt.User
import org.jiakesiws.minipika.components.annotation.Batch
import org.jiakesiws.minipika.components.annotation.QueryOf
import org.jiakesiws.minipika.components.annotation.Update
import org.jiakesiws.minipika.framework.factory.Factorys

interface UserMapper {

  companion object {
    val mapper = Factorys.forMapper(UserMapper::class.java)!!
  }

  @QueryOf("""
    select * from website_user_info where 1=1
    #IF NOT_EMPTY(username)
      and username = #{username}
    #END
  """)
  fun findUser(username: String): User

  @QueryOf("""
    select * from website_user_info where 1=1
    #IF NOT_EMPTY(username)
      and username = #{username}
    #END
  """)
  fun findUserList(username: String): List<User>

  @Batch("""
    insert into website_user_info (username, `password`) values (?, ?) 
    #FOREACH user : users
      #{user.username},#{user.password}
    #END
  """)
  fun addBatch(users: List<User>, name: String): IntArray

  @Update("""
    update website_user_info set username = #{newName}
    where username = #{name}
  """)
  fun update(name:String, newName:String)

}
