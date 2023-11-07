package cn.akfang.berry.common.utils;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;
import java.util.function.BiConsumer;

public class DBBatchUtil {
    public static <M extends BaseMapper<T>, T> void batchUpdate(SqlSessionTemplate sqlSessionTemplate, List<T> updateList, M baseMapper, BiConsumer<M, T> consumer, int batchSize) {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        // XxxMapper 为 对应的mapper文件
        int size = updateList.size();
        try {
            for (int i = 0; i < size; i++) {
                consumer.accept(baseMapper, updateList.get(i));
                if (i > 0 && i % batchSize == 0) {
                    session.commit();
                    session.clearCache();
                }
            }
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }
}
