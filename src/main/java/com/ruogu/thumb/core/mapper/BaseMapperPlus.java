package com.ruogu.thumb.core.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.ruogu.thumb.common.pojo.PageParam;
import com.ruogu.thumb.common.pojo.PageResult;
import com.ruogu.thumb.util.MyBatisUtils;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/***
 *@author ruogu
 *@create 2025/4/18 14:08
 **/
public interface BaseMapperPlus<T> extends BaseMapper<T> {
    default PageResult<T> selectPage(PageParam pageParam, @Param("ew") Wrapper<T> queryWrapper) {
        // MyBatis Plus 查询
        IPage<T> mpPage = MyBatisUtils.buildPage(pageParam);
        selectPage(mpPage, queryWrapper);
        // 转换返回
        return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
    }

    default T selectOne(String field, Object value) {
        return selectOne(new QueryWrapper<T>().eq(field, value));
    }

    default T selectOne(SFunction<T, ?> field, Object value) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field, value));
    }

    default T selectOne(String field1, Object value1, String field2, Object value2) {
        return selectOne(new QueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    default Long selectCount() {
        return selectCount(new QueryWrapper<T>());
    }

    default Long selectCount(String field, Object value) {
        return selectCount(new QueryWrapper<T>().eq(field, value));
    }

    default Long selectCount(SFunction<T, ?> field, Object value) {
        return selectCount(new LambdaQueryWrapper<T>().eq(field, value));
    }

    default List<T> selectList() {
        return selectList(new QueryWrapper<>());
    }

    default List<T> selectList(String field, Object value) {
        return selectList(new QueryWrapper<T>().eq(field, value));
    }

    default List<T> selectList(SFunction<T, ?> field, Object value) {
        return selectList(new LambdaQueryWrapper<T>().eq(field, value));
    }

    default List<T> selectList(String field, Collection<?> values) {
        return selectList(new QueryWrapper<T>().in(field, values));
    }

    default List<T> selectList(SFunction<T, ?> field, Collection<?> values) {
        return selectList(new LambdaQueryWrapper<T>().in(field, values));
    }

    /**
     * 逐条插入，适合少量数据插入，或者对性能要求不高的场景
     * <p>
     * 如果大量，请使用 {@link com.baomidou.mybatisplus.extension.service.impl.ServiceImpl#saveBatch(Collection)} 方法
     * 使用示例，可见 RoleMenuBatchInsertMapper、UserRoleBatchInsertMapper 类
     *
     * @param entities 实体们
     */
    default void insertBatch(Collection<T> entities) {
        entities.forEach(this::insert);
    }

    default void updateBatch(T update) {
        update(update, new QueryWrapper<>());
    }

    default void updateBatch(Collection<T> entities) {
        entities.forEach(this::updateById);
    }
}