import com.crawler.entity.LogEntity;
import org.apache.catalina.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

public class MybatisTest {

    @Test
    public void testSave() throws Exception {
        //创建sessionFactory对象
        SqlSessionFactory sf = new SqlSessionFactoryBuilder().
                build(Resources.getResourceAsStream("mybatis-config.xml"));
        //获取session对象
        SqlSession session = sf.openSession();
        //创建实体对象
        LogEntity logEntity = new LogEntity();
        logEntity.setUsername("toby");
        logEntity.setOperation("w");
        //保存数据到数据库中
        session.insert("com.toby.mybatis.domain.UserMapper.add", logEntity);
        //提交事务,这个是必须要的,否则即使sql发了也保存不到数据库中
        session.commit();
        //关闭资源
        session.close();
    }
}
